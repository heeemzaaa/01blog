import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';
import { UserResponse } from '../models/me-response.model';

@Injectable({ providedIn: 'root' })
export class SubscribeService {

  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/connection';

  getSubscribers(userId: string): Observable<ApiResponse<UserResponse[]>> {
    return this.http.get<ApiResponse<UserResponse[]>>(
      `${this.baseUrl}/subscribers/${userId}`
    );
  }

  getSubscribed(userId: string): Observable<ApiResponse<UserResponse[]>> {
    return this.http.get<ApiResponse<UserResponse[]>>(
      `${this.baseUrl}/subscribed/${userId}`
    );
  }
}
