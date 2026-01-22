export interface AuthResponse {
    token: string;
    id: string
    email: string;
    username: string;
    firstName: string;
    lastName: string;
    about?: string;
    role: 'USER' | 'ADMIN';
    profileImage?: string;
}