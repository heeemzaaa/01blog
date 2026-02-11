export interface AdminDashboardResponse {
  totalUsers: number;
  totalPosts: number;
  totalComments: number;
  totalReports: number;

  reportsByType: {
    POST: number;
    COMMENT: number;
    USER: number;
  };
}
