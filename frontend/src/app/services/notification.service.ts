import { HttpClient } from "@angular/common/http";
import { inject, Injectable, signal } from "@angular/core";
import { Observable } from "rxjs";
import { ApiResponse } from "../models/api-response.model";
import { NotificationResponse } from "../models/notification-response.model";
import { ToastService } from "./toast.service";
import { Router } from "@angular/router";

@Injectable({
    providedIn: 'root',
})
export class NotificationService {
    private baseUrl = 'http://localhost:8080/api/notifications';
    private http = inject(HttpClient);
    private toast = inject(ToastService);
    private router = inject(Router);
    notifications = signal<NotificationResponse[]>([]);


    getMyNotifications(): Observable<ApiResponse<NotificationResponse[]>> {
        return this.http.get<ApiResponse<NotificationResponse[]>>(
            `${this.baseUrl}`,
        );
    }

    fetchMyNotifications() {
        this.getMyNotifications().subscribe({
            next: (res) => {
                this.notifications.set(res.data);
            },
            error: (err) => {
                if (err.error.status == 401) {
                    localStorage.removeItem('token');
                    this.router.navigate(['/login']);
                    return
                }
                this.toast.showError('Error while fetching notifications');
            },
        });
    }

    markAsRead(notificationId: string): Observable<ApiResponse<string>> {
        return this.http.put<ApiResponse<string>>(
            `${this.baseUrl}/${notificationId}/read`,
            null
        );
    }

    markAllAsRead(): Observable<ApiResponse<string>> {
        return this.http.put<ApiResponse<string>>(
            `${this.baseUrl}/read-all`,
            null,
        );
    }

    deleteNotification(notificationId: string): Observable<ApiResponse<string>> {
        return this.http.delete<ApiResponse<string>>(
            `${this.baseUrl}/${notificationId}`
        );
    }
}