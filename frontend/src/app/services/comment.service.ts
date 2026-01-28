import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ApiResponse } from "../models/api-response.model";
import { CommentResponse } from "../models/comment-response.model";
import { CommentRequest } from "../models/comment-request.model";

@Injectable({
    providedIn: 'root',
})
export class CommentService {
    private baseUrl = 'http://localhost:8080/api/posts';
    private http = inject(HttpClient);

    getComments(postId: string): Observable<ApiResponse<CommentResponse[]> | null> {
        return this.http.get<ApiResponse<CommentResponse[]>>(
            `${this.baseUrl}/${postId}/comments`
        );
    }

    createComment(postId: string, commentRequest: CommentRequest): Observable<ApiResponse<CommentResponse> | null> {
        return this.http.post<ApiResponse<CommentResponse>>(
            `${this.baseUrl}/${postId}/comments`,
            commentRequest
        );
    } 
}