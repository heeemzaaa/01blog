import { UserResponse } from "./user-response.model";
import { PostMediaResponse } from "./post-media-response.model";



export interface PostResponse {
    id: string;
    user: UserResponse;

    title: string;
    content: string;
    medias: PostMediaResponse[];

    createdAt: string;

    liked: boolean;
    likesCount: number;
    commentsCount: number;
    myPost: boolean;
}
