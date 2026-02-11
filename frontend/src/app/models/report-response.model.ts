import { UserResponse } from "./user-response.model";

export type ReportTarget = 'POST' | 'COMMENT' | 'USER';

export type ReportStatus = 'PENDING' | 'REVIEWED' | 'RESOLVED';

export interface ReportResponse {
  id: string;
  reporter: UserResponse;

  targetType: ReportTarget;
  targetId: string;

  reason: string;
  description: string;

  status: ReportStatus;
  createdAt: string;
}
