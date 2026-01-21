export interface User {
    id: string,
    firstName: string,
    lastName: string,
    userName: string,
    email: string,
    profileImage?: string,
    role: 'USER' | 'ADMIN'
}