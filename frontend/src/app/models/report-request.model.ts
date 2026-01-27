import { ReportTarget } from './report-target.enum';

export interface ReportRequest {
  targetType: ReportTarget;
  targetId: string;
  reason: string;
  description: string;
}
