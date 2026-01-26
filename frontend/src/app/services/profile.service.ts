import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { ApiResponse } from "../models/api-response.model";
import { ProfileResponse } from "../models/profile-response.model";

@Injectable({
    providedIn: 'root',
})
export class ProfileService {

    private http = inject(HttpClient);
    private baseUrl = 'http://localhost:8080/api/profile';

    getProfile(userId: string): Observable<ApiResponse<ProfileResponse>> {
        return this.http.get<ApiResponse<ProfileResponse>>(
            `${this.baseUrl}/${userId}`
        );
    }

    updateProfile(userId: string, data: FormData) {
        return this.http.put<any>(
            `${this.baseUrl}/${userId}`,
            data
        );
    }
}
