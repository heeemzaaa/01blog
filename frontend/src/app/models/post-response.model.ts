import { UserResponse } from "./me-response.model";
import { PostMediaResponse } from "./post-media-response.model";



export interface PostResponse {
    id: string;
    user: UserResponse;

    title: string;
    content: string;
    medias: PostMediaResponse[];

    createdAt: string;

    isLiked: boolean;
    likesCount: number;
    commentsCount: number;
}
