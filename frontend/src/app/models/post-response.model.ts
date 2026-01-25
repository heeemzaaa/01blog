export interface PostResponse {
  id: string;
  title: string;
  content: string;
  createdAt: string;
  updatedAt?: string;

  author: {
    id: string;
    username: string;
    firstName: string;
    lastName: string;
    profileImage?: string;
  };

  medias: {
    id: string;
    url: string;
    type: 'IMAGE' | 'VIDEO';
  }[];

  likesCount: number;
  commentsCount: number;
  likedByMe: boolean;
}
