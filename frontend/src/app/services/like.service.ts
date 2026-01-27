import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root',
})
export class LikeService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/posts';

  toggleLike(postId: string): Observable<ApiResponse<string>> {
    return this.http.post<ApiResponse<string>>(
      `${this.baseUrl}/${postId}/like`,
      {}
    );
  }
}
