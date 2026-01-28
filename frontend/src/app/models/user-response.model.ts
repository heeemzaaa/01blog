export interface UserResponse {
    id: string;
    firstName: string;
    lastName: string;
    username: string;
    profileImage: string;
    nbr_of_posts: number;
    nbr_of_followers: number;
    nbr_of_following: number;
    nbr_of_notifications: number;
    token: string;
}