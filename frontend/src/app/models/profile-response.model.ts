export interface ProfileResponse {
    id: string;
    firstName: string;
    lastName: string;
    username: string;
    about: string;
    profileImage: string;
    email: string;
    nbr_of_posts: number;
    nbr_of_followers: number;
    nbr_of_following: number;
    nbr_of_likes_received: number;
    isMyProfile: boolean;
    isFollowing: boolean;
}
