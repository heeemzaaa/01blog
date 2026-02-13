import { UserResponse } from "./user-response.model";

export interface CommentResponse {
    id: string;
    user: UserResponse;
    content: string;
    commentedAt: string;
    visible: boolean;
    reviewed: boolean;
    myComment: boolean;
}
