import { CommonModule } from '@angular/common';
import { Component, effect, inject, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { NotificationResponse } from '../../models/notification-response.model';
import { NotificationService } from '../../services/notification.service';
import { NotificationType } from '../../models/notification-type.enum';

@Component({
  selector: 'app-notifications',
  imports: [MatIconModule, CommonModule],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css',
})
export class Notifications {
  private notificationService = inject(NotificationService);
  notifications = this.notificationService.notifications;


  constructor() {
    effect(() => {
      this.notificationService.fetchMyNotifications();
    })
  }


  markAsRead(notificationId: string) {
    this.notificationService.markAsRead(notificationId).subscribe({
      next: () => {
        this.notifications.update(list =>
          list.map(n =>
            n.id === notificationId ? { ...n, isSeen: true } : n
          )
        );
      },
      error: (err) => {
        console.error('Failed to mark notification as read', err);
      },
    });
  }



  markAllAsRead() {
    this.notificationService.markAllAsRead().subscribe({
      next: () => {
        this.notifications.update(list =>
          list.map(n => ({ ...n, isSeen: true }))
        );
      },
      error: (err) => {
        console.error('Failed to mark all as read', err);
      },
    });
  }


  getUnreadCount() {
    return this.notifications().filter(n => !n.isSeen).length;
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

      default:
        return 'You have a new notification';
    }
  }

  timeAgo(date: string): string {
    const seconds = Math.floor((Date.now() - new Date(date).getTime()) / 1000);

    if (seconds < 60) return 'just now';
    if (seconds < 3600) return `${Math.floor(seconds / 60)} min ago`;
    if (seconds < 86400) return `${Math.floor(seconds / 3600)} hours ago`;
    return `${Math.floor(seconds / 86400)} days ago`;
  }

}
