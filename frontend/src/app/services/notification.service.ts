import { HttpClient } from "@angular/common/http";
import { inject, Injectable, signal } from "@angular/core";
import { Observable } from "rxjs";
import { ApiResponse } from "../models/api-response.model";
import { NotificationResponse } from "../models/notification-response.model";

@Injectable({
    providedIn: 'root',
})
export class NotificationService {
    private baseUrl = 'http://localhost:8080/api/notifications';
    private http = inject(HttpClient);
    notifications = signal<NotificationResponse[]>([]);


    getMyNotifications(): Observable<ApiResponse<NotificationResponse[]>> {
        return this.http.get<ApiResponse<NotificationResponse[]>>(
            `${this.baseUrl}`,
        );
    }

    fetchMyNotifications() {
        this.getMyNotifications().subscribe({
            next: (res) => {
                console.log('res.data :>> ', res.data);
                this.notifications.set(res.data);
            },
            error: (err) => {
                console.error('Error while fetching notifications', err);
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