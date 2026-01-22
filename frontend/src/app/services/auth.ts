import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
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
    public currentUser$ = this.currentUserSubject.asObservable();
    

    constructor(private http: HttpClient) { }

    getMe(): Observable<ApiResponse<MeResponse> | null> {
        const token = this.getToken();

        if (!token) {
            this.currentUserSubject.next(null);
            return of(null);
        }

        return this.http.get<ApiResponse<MeResponse>>(`${this.baseUrl}/me`).pipe(
            tap(response => {
                if (response.success && response.data) {
                    const userWithToken = { ...response.data, token: token };
                    this.currentUserSubject.next(userWithToken);
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