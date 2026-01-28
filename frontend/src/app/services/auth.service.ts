import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { ApiResponse } from '../models/api-response.model';
import { UserResponse } from '../models/user-response.model';

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    private baseUrl = 'http://localhost:8080/api/auth';
    http = inject(HttpClient);
    currentUser = signal<UserResponse | null>(null);


    getMe(): Observable<ApiResponse<UserResponse> | null> {
        const token = this.getToken();

        if (!token) {
            this.currentUser.set(null);
            return of(null);
        }

        return this.http.get<ApiResponse<UserResponse>>(`${this.baseUrl}/me`).pipe(
            tap(response => {
                if (response.success && response.data) {
                    const userWithToken = { ...response.data, token: token };
                    this.currentUser.set(userWithToken);
                }
            }),

            catchError(error => {
                this.logout();
                return of(null);
            })
        );
    }

    login(credentials: any): Observable<ApiResponse<UserResponse>> {
        return this.http.post<ApiResponse<UserResponse>>(
            `${this.baseUrl}/login`,
            credentials
        ).pipe(
            tap(response => {
                if (response.success && response.data) {
                    this.saveToken(response.data.token);
                    this.currentUser.set({
                        ...response.data,
                        token: response.data.token
                    });
                }
            })
        );
    }


    register(userData: any): Observable<ApiResponse<UserResponse>> {
        return this.http.post<ApiResponse<UserResponse>>(
            `${this.baseUrl}/register`,
            userData
        ).pipe(
            tap(response => {
                if (response.success && response.data) {
                    this.saveToken(response.data.token);
                    this.currentUser.set({
                        ...response.data,
                        token: response.data.token
                    });
                }
            })
        );
    }

    saveToken(token: string) {
        localStorage.setItem('token', token);
    }

    getToken(): string | null {
        return localStorage.getItem('token');
    }

    logout() {
        localStorage.removeItem('token');
        this.currentUser.set(null);
    }
}