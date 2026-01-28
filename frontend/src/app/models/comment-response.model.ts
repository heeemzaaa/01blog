import { UserResponse } from "./user-response.model";

export interface CommentResponse {
    id: string,
    user: UserResponse,
    content: string,
    isMyComment: boolean,
    commentedAt: string
}