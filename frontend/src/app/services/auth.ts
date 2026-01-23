import { HttpClient } from '@angular/common/http';
import { inject, Injectable, signal } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs'; // Import 'of'
import { catchError, tap } from 'rxjs/operators';      // Import operators
import { ApiResponse } from '../models/api-response.model';
import { AuthResponse } from '../models/auth.model';
import { MeResponse } from '../models/me-response.model'; // <--- IMPORT THIS

@Injectable({
    providedIn: 'root',
})
export class AuthService {
    private baseUrl = 'http://localhost:8080/api/auth';
    private currentUserSubject = new BehaviorSubject<any>(null);
    http = inject(HttpClient);
    currentUser = signal<MeResponse | null>(null);


    getMe(): Observable<ApiResponse<MeResponse> | null> {
        const token = this.getToken();

        if (!token) {
            this.currentUser.set(null);
            return of(null);
        }

        return this.http.get<ApiResponse<MeResponse>>(`${this.baseUrl}/me`).pipe(
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

    login(credentials: any): Observable<ApiResponse<AuthResponse>> {
        return this.http.post<ApiResponse<AuthResponse>>(
            `${this.baseUrl}/login`,
            credentials
        ).pipe(
            tap(response => {
                if (response.success && response.data) {
                    this.saveToken(response.data.token);
                    this.currentUserSubject.next(response.data);
                }
            })
        );
    }

    register(userData: any): Observable<ApiResponse<AuthResponse>> {
        return this.http.post<ApiResponse<AuthResponse>>(
            `${this.baseUrl}/register`,
            userData
        ).pipe(
            tap(response => {
                if (response.success && response.data) {
                    this.saveToken(response.data.token);
                    this.currentUserSubject.next(response.data);
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
        this.currentUserSubject.next(null);
    }
}