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
}
