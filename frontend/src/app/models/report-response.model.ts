import { UserResponse } from "./user-response.model";

export type ReportStatus =
  | 'PENDING'
  | 'REVIEWED'
  | 'DISMISSED'
  | 'ACTIONED';

export type ReportTarget =
  | 'POST'
  | 'COMMENT'
  | 'USER';


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
