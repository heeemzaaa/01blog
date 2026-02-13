import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ApiResponse } from "../models/api-response.model";
import { UserResponse } from "../models/user-response.model";
import { PostResponse } from "../models/post-response.model";
import { CommentResponse } from "../models/comment-response.model";
import { AdminDashboardResponse } from "../models/admin-dashboard-response.model";
import { ReportResponse } from "../models/report-response.model";

@Injectable({
    providedIn: 'root',
})
export class AdminService {

    private http = inject(HttpClient);
    private basedUrl = 'http://localhost:8080/api/admin';

    // ======================== USERS ========================

    getAllUsers(): Observable<ApiResponse<UserResponse[]>> {
        return this.http.get<ApiResponse<UserResponse[]>>(
            `${this.basedUrl}/users`
        );
    }

    banUser(userId: string): Observable<ApiResponse<string>> {
        return this.http.put<ApiResponse<string>>(
            `${this.basedUrl}/users/${userId}/ban`,
            {}
        );
    }

    unbanUser(userId: string): Observable<ApiResponse<string>> {
        return this.http.put<ApiResponse<string>>(
            `${this.basedUrl}/users/${userId}/unban`,
            {}
        );
    }

    deleteUser(userId: string): Observable<ApiResponse<string>> {
        return this.http.delete<ApiResponse<string>>(
            `${this.basedUrl}/users/${userId}`
        )
    }

    // ======================== POSTS ========================

    getAllPosts(): Observable<ApiResponse<PostResponse[]>> {
        return this.http.get<ApiResponse<PostResponse[]>>(
            `${this.basedUrl}/posts`
        );
    }

    hidePost(postId: string): Observable<ApiResponse<string>> {
        return this.http.put<ApiResponse<string>>(
            `${this.basedUrl}/posts/${postId}/hide`,
            {}
        );
    }

    restorePost(postId: string): Observable<ApiResponse<string>> {
        return this.http.put<ApiResponse<string>>(
            `${this.basedUrl}/posts/${postId}/restore`,
            {}
        );
    }

    deletePost(postId: string): Observable<ApiResponse<string>> {
        return this.http.delete<ApiResponse<string>>(
            `${this.basedUrl}/posts/${postId}`
        );
    }

    // ======================== COMMENTS ========================

    getAllComments(): Observable<ApiResponse<CommentResponse[]>> {
        return this.http.get<ApiResponse<CommentResponse[]>>(
            `${this.basedUrl}/comments`
        );
    }

    hideComment(commentId: string): Observable<ApiResponse<string>> {
        return this.http.put<ApiResponse<string>>(
            `${this.basedUrl}/comments/${commentId}/hide`,
            {}
        );
    }

    deleteComment(commentId: string): Observable<ApiResponse<string>> {
        return this.http.delete<ApiResponse<string>>(
            `${this.basedUrl}/comments/${commentId}`
        );
    }

    // ======================== REPORTS ========================

    getAllReports(): Observable<ApiResponse<ReportResponse[]>> {
        return this.http.get<ApiResponse<ReportResponse[]>>(
            `${this.basedUrl}/reports`
        );
    }

    getReportById(id: string): Observable<ApiResponse<ReportResponse>> {
        return this.http.get<ApiResponse<ReportResponse>>(
            `${this.basedUrl}/reports/${id}`
        )
    }

    reviewReport(reportId: string): Observable<ApiResponse<string>> {
        return this.http.put<ApiResponse<string>>(
            `${this.basedUrl}/reports/${reportId}/review`,
            {}
        );
    }

    resolveReport(reportId: string): Observable<ApiResponse<string>> {
        return this.http.put<ApiResponse<string>>(
            `${this.basedUrl}/reports/${reportId}/resolve`,
            {}
        );
    }

    dismissReport(reportId: string): Observable<ApiResponse<string>> {
        return this.http.put<ApiResponse<string>>(
            `${this.basedUrl}/reports/${reportId}/dismiss`,
            null
        )
    }

    actionReport(reportId: string): Observable<ApiResponse<string>> {
        return this.http.put<ApiResponse<string>>(
            `${this.basedUrl}/reports/${reportId}/action`,
            null
        )
    }

    // ======================== DASHBOARD ========================

    getDashboardStats(): Observable<ApiResponse<AdminDashboardResponse>> {
        return this.http.get<ApiResponse<AdminDashboardResponse>>(
            `${this.basedUrl}/dashboard`
        );
    }
}
