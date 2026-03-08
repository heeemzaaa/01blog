import { CommonModule } from '@angular/common';
import { Component, effect, inject, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { NotificationResponse } from '../../models/notification-response.model';
import { NotificationService } from '../../services/notification.service';
import { NotificationType } from '../../models/notification-type.enum';
import { UtilsService } from '../../services/utils.service';
import { Router } from '@angular/router';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-notifications',
  standalone: true,
  imports: [MatIconModule, CommonModule],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css',
})
export class Notifications {
  private notificationService = inject(NotificationService);
  private utilsService = inject(UtilsService);
  private router = inject(Router);
  private toast = inject(ToastService);
  notifications = this.notificationService.notifications;


  constructor() {
    effect(() => {
      this.notificationService.fetchMyNotifications();
    })
  }

  timeAgo(date: string): string {
    return this.utilsService.timeAgo(date);
  }


  markAsRead(notificationId: string) {
    this.notificationService.markAsRead(notificationId).subscribe({
      next: () => {
        this.notifications.update(list =>
          list.map(n =>
            n.id === notificationId ? { ...n, seen: true } : n
          )
        );
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError('Failed to mark notification as read');
      },
    });
  }



  markAllAsRead() {
    this.notificationService.markAllAsRead().subscribe({
      next: () => {
        this.notifications.update(list =>
          list.map(n => ({ ...n, seen: true }))
        );
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError('Failed to mark all as read');
      },
    });
  }

  deleteNotification(notificationId: string) {
    this.notificationService.deleteNotification(notificationId).subscribe({
      next: () => {
        this.notifications.update(list =>
          list.filter(n => n.id !== notificationId)
        );
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("Error while deleting the notification");
      }
    })
  }


  getUnreadCount() {
    return this.notifications().filter(n => !n.seen).length;
  }

  getNotificationText(n: NotificationResponse): string {
    const fullName = `${n.actor.firstName} ${n.actor.lastName}`;

    switch (n.type) {
      case NotificationType.LIKE:
        return `${fullName} liked your post`;
      case NotificationType.COMMENT:
        return `${fullName} commented on your post`;
      case NotificationType.NEW_POST:
        return `${fullName} published a new post`;
      case NotificationType.REPORT_CREATED:
        return `${fullName} created a report`;
      case NotificationType.REPORT_UPDATE:
        return `${fullName} updated a report`;
      case NotificationType.FOLLOW:
        return `${fullName} subscribed to your account`
      default:
        return 'You have a new notification';
    }
  }



}
