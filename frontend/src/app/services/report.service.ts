import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ReportRequest } from '../models/report-request.model';
import { ApiResponse } from '../models/api-response.model';

@Injectable({
  providedIn: 'root',
})
export class ReportService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/reports';

  createReport(request: ReportRequest): Observable<ApiResponse<any>> {
    return this.http.post<ApiResponse<any>>(this.baseUrl, request);
  }
}
