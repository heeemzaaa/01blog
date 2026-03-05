import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { AdminService } from '../../../services/admin.service';
import { ReportResponse } from '../../../models/report-response.model';
import { MatIconModule } from '@angular/material/icon';
import { ToastService } from '../../../services/toast.service';

@Component({
  selector: 'app-admin-report-details',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './admin-report-details.component.html',
  styleUrl: './admin-report-details.component.css'
})
export class AdminReportDetails {

  route = inject(ActivatedRoute);
  router = inject(Router);
  toast = inject(ToastService)
  adminService = inject(AdminService);

  report = signal<ReportResponse | null>(null);

  constructor() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadReport(id);
    }
  }

  loadReport(id: string) {
    this.adminService.getReportById(id).subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.report.set(res.data);
        }
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("Error loading the report !")
      }
    });
  }

  goBack() {
    this.router.navigate(['/admin']);
  }

  viewTarget() {
    if (!this.report()) return;

    const targetType = this.report()!.targetType;
    const targetId = this.report()!.targetId;

    if (targetType === 'POST') {
      this.router.navigate(['/post', targetId]);
    }

    if (targetType === 'COMMENT') {
      // Navigate to post details (comment lives inside post)
      this.router.navigate(['/post', targetId]);
    }

    if (targetType === 'USER') {
      this.router.navigate(['/profile', targetId]);
    }
  }

  /* ================= TARGET ACTIONS ================= */

  hideTargetPost() {
    if (!this.report()) return;

    this.adminService.hidePost(this.report()!.targetId).subscribe(() => {
      this.markAsActioned();
    });
  }

  deleteTargetPost() {
    if (!this.report()) return;

    this.adminService.deletePost(this.report()!.targetId).subscribe(() => {
      this.markAsActioned();
    });
  }

  hideTargetComment() {
    if (!this.report()) return;

    this.adminService.hideComment(this.report()!.targetId).subscribe(() => {
      this.markAsActioned();
    });
  }

  deleteTargetComment() {
    if (!this.report()) return;

    this.adminService.deleteComment(this.report()!.targetId).subscribe(() => {
      this.markAsActioned();
    });
  }

  banTargetUser() {
    if (!this.report()) return;

    this.adminService.banUser(this.report()!.targetId).subscribe(() => {
      this.markAsActioned();
    });
  }

  /* ================= REPORT DECISIONS ================= */

  dismissReport() {
    if (!this.report()) return;

    this.adminService.dismissReport(this.report()!.id).subscribe(() => {
      this.report.update(r => r ? { ...r, status: 'DISMISSED' } : null);
    });
  }

  markAsActioned() {
    if (!this.report()) return;

    this.adminService.actionReport(this.report()!.id).subscribe(() => {
      this.report.update(r => r ? { ...r, status: 'ACTIONED' } : null);
    });
  }

}
