import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ApiResponse } from "../models/api-response.model";
import { PostResponse } from "../models/post-response.model";

@Injectable({
    providedIn: 'root',
})
export class PostService {

    private http = inject(HttpClient);
    private basedUrl = 'http://localhost:8080/api/posts';

    createPost(formData: FormData): Observable<ApiResponse<PostResponse>> {
        return this.http.post<ApiResponse<PostResponse>>(
            `${this.basedUrl}/create`,
            formData
        );
    }

    getFeedPosts(): Observable<ApiResponse<PostResponse[]>> {
        return this.http.get<ApiResponse<PostResponse[]>>(
            `${this.basedUrl}/feed`
        );
    }

    getProfilePosts(userId: string): Observable<ApiResponse<PostResponse[]>> {
        return this.http.get<ApiResponse<PostResponse[]>>(
            `${this.basedUrl}/profile/${userId}`
        );
    }

    getPostById(userId: string): Observable<ApiResponse<PostResponse>> {
        return this.http.get<ApiResponse<PostResponse>>(
            `${this.basedUrl}/${userId}`
        )
    }

    deletePost(postId: string): Observable<ApiResponse<string>> {
        return this.http.delete<ApiResponse<string>>(
            `${this.basedUrl}/delete/${postId}`
        )
    }

    updatePost(formData: FormData, postId: string): Observable<ApiResponse<PostResponse>> {
        return this.http.put<ApiResponse<PostResponse>>(
            `${this.basedUrl}/update/${postId}`,
            formData
        );
    }

    hidePost(postId: string) {
        return this.http.put<ApiResponse<string>>(
            `http://localhost:8080/api/admin/posts/${postId}/hide`,
            {}
        );
    }

    restorePost(postId: string) {
        return this.http.put<ApiResponse<string>>(
            `http://localhost:8080/api/admin/posts/${postId}/restore`,
            {}
        );
    }

}
