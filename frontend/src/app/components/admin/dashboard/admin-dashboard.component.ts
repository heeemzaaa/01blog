import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { AdminService } from '../../../services/admin.service';
import { AdminDashboardResponse } from '../../../models/admin-dashboard-response.model';
import { UserResponse } from '../../../models/user-response.model';
import { PostResponse } from '../../../models/post-response.model';
import { ReportResponse } from '../../../models/report-response.model';
import { Router } from '@angular/router';
import { ToastService } from '../../../services/toast.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboard {

  adminService = inject(AdminService);
  router = inject(Router);
  toast = inject(ToastService);

  activeTab = signal<'users' | 'posts' | 'reports'>('users');

  dashboardStats = signal<AdminDashboardResponse | null>(null);
  users = signal<UserResponse[]>([]);
  posts = signal<PostResponse[]>([]);
  reports = signal<ReportResponse[]>([]);

  constructor() {
    this.loadDashboardStats();
    this.loadUsers();
    this.loadPosts();
    this.loadReports();
  }

  setTab(tab: 'users' | 'posts' | 'reports') {
    this.activeTab.set(tab);
  }

  loadDashboardStats() {
    this.adminService.getDashboardStats().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.dashboardStats.set(res.data);
        }
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("Error loading the admin dashboard !")
      }
    });
  }

  loadUsers() {
    this.adminService.getAllUsers().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.users.set(res.data);
        }
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("Error loading the users !")
      }
    });
  }

  loadPosts() {
    this.adminService.getAllPosts().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.posts.set(res.data);
        }
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("Error loading the posts !")
      }
    });
  }

  loadReports() {
    this.adminService.getAllReports().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.reports.set(res.data);
        }
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("Error loading the reports !")
      }
    });
  }

  openReport(reportId: string) {
    this.router.navigate(['/admin/reports', reportId]);
  }

  banUser(userId: string) {
    this.adminService.banUser(userId).subscribe({
      next: (res) => {
        if (res.success) {
          this.toast.showSuccess("the user is banned !")
          this.users.update(users =>
            users.map(u =>
              u.id === userId ? { ...u, active: false } : u
            )
          );
        }
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("The user is not banned due to some error !")
      }
    });
  }

  unbanUser(userId: string) {
    this.adminService.unbanUser(userId).subscribe({
      next: (res) => {
        if (res.success) {
          this.toast.showSuccess("The user is unbanned !")
          this.users.update(users =>
            users.map(u =>
              u.id === userId ? { ...u, active: true } : u
            )
          );
        }
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("The user is not unbanned due to some error !")
      }
    });
  }

  deleteUser(userId: string) {
    this.adminService.deleteUser(userId).subscribe({
      next: (res) => {
        if (res.success) {
          this.toast.showSuccess("The user is deleted !")
          this.users.update(users =>
            users.filter(u => u.id !== userId)
          );
        }
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("The user is not deleted due to some error !")
      }
    });
  }

  hidePost(postId: string) {
    this.adminService.hidePost(postId).subscribe({
      next: (res) => {
        if (res.success) {
          this.toast.showSuccess("The post is hidden !")
          this.posts.update(posts =>
            posts.map(p =>
              p.id === postId ? { ...p, visible: false } : p
            )
          );
        }
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("The post is not hidden due to some error !")
      }
    });
  }

  restorePost(postId: string) {
    this.adminService.restorePost(postId).subscribe({
      next: (res) => {
        if (res.success) {
          this.toast.showSuccess("The post is restored !")
          this.posts.update(posts =>
            posts.map(p =>
              p.id === postId ? { ...p, visible: true } : p
            )
          );
        }
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("The post is not restored due to some error !")
      }
    });
  }

  deletePost(postId: string) {
    this.adminService.deletePost(postId).subscribe({
      next: (res) => {
        if (res.success) {
          this.toast.showSuccess("The post is deleted !")
          this.posts.update(posts =>
            posts.filter(p => p.id !== postId)
          );
        }
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("The post is not deleted due to some error !")
      }
    });
  }

  reviewReport(reportId: string) {
    this.adminService.reviewReport(reportId).subscribe({
      next: (res) => {
        if (res.success) {
          this.toast.showSuccess("The report is reviewed !")
          this.reports.update(reports =>
            reports.map(r =>
              r.id === reportId
                ? { ...r, status: 'REVIEWED' }
                : r
            )
          );
        }
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("The report is not reviewed due to some error !")
      }
    });
  }

  resolveReport(reportId: string) {
    this.adminService.resolveReport(reportId).subscribe({
      next: (res) => {
        if (res.success) {
          this.toast.showSuccess("The report is resolved !")
          this.reports.update(reports =>
            reports.map(r =>
              r.id === reportId
                ? { ...r, status: 'ACTIONED' }
                : r
            )
          );
        }
      },
      error: (err) => {
        if (err.error.status == 401) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("The report is not resolved due to some error !")
      }
    });
  }

}
