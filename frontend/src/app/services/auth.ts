import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { AuthResponse } from '../models/auth.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
    private baseUrl = 'http://localhost:8080/api/auth';

    constructor(private http: HttpClient) {}

    login(credentials: any): Observable<ApiResponse<AuthResponse>> {
        return this.http.post<ApiResponse<AuthResponse>>(
            `${this.baseUrl}/login`,
            credentials
        );
    }

    register(userData: any): Observable<ApiResponse<AuthResponse>> {
        return this.http.post<ApiResponse<AuthResponse>>(
            `${this.baseUrl}/register`,
            userData
        );
    }

    saveToken(token: string) {
        localStorage.setItem('token', token);
    }

    getToken(): string | null {
        return localStorage.getItem('token');
    }
}
