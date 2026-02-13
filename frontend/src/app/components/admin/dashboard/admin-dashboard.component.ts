import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { AdminService } from '../../../services/admin.service';
import { AdminDashboardResponse } from '../../../models/admin-dashboard-response.model';
import { UserResponse } from '../../../models/user-response.model';
import { PostResponse } from '../../../models/post-response.model';
import { ReportResponse } from '../../../models/report-response.model';
import { Router } from '@angular/router';

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
      }
    });
  }

  loadUsers() {
    this.adminService.getAllUsers().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.users.set(res.data);
          console.log('res.data :>> ', res.data);
        }
      }
    });
  }

  loadPosts() {
    this.adminService.getAllPosts().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.posts.set(res.data);
        }
      }
    });
  }

  loadReports() {
    this.adminService.getAllReports().subscribe({
      next: (res) => {
        if (res.success && res.data) {
          this.reports.set(res.data);
        }
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
          this.users.update(users =>
            users.map(u =>
              u.id === userId ? { ...u, active: false } : u
            )
          );
        }
      }
    });
  }

  unbanUser(userId: string) {
    this.adminService.unbanUser(userId).subscribe({
      next: (res) => {
        if (res.success) {
          this.users.update(users =>
            users.map(u =>
              u.id === userId ? { ...u, active: true } : u
            )
          );
        }
      }
    });
  }

  deleteUser(userId: string) {
    this.adminService.deleteUser(userId).subscribe({
      next: (res) => {
        if (res.success) {
          this.users.update(users =>
            users.filter(u => u.id !== userId)
          );
        }
      }
    });
  }

  hidePost(postId: string) {
    this.adminService.hidePost(postId).subscribe({
      next: (res) => {
        if (res.success) {
          this.posts.update(posts =>
            posts.map(p =>
              p.id === postId ? { ...p, visible: false } : p
            )
          );
        }
      }
    });
  }

  restorePost(postId: string) {
    this.adminService.restorePost(postId).subscribe({
      next: (res) => {
        if (res.success) {
          this.posts.update(posts =>
            posts.map(p =>
              p.id === postId ? { ...p, visible: true } : p
            )
          );
        }
      }
    });
  }

  deletePost(postId: string) {
    this.adminService.deletePost(postId).subscribe({
      next: (res) => {
        if (res.success) {
          this.posts.update(posts =>
            posts.filter(p => p.id !== postId)
          );
        }
      }
    });
  }

  reviewReport(reportId: string) {
    this.adminService.reviewReport(reportId).subscribe({
      next: (res) => {
        if (res.success) {
          this.reports.update(reports =>
            reports.map(r =>
              r.id === reportId
                ? { ...r, status: 'REVIEWED' }
                : r
            )
          );
        }
      }
    });
  }

  resolveReport(reportId: string) {
    this.adminService.resolveReport(reportId).subscribe({
      next: (res) => {
        if (res.success) {
          this.reports.update(reports =>
            reports.map(r =>
              r.id === reportId
                ? { ...r, status: 'ACTIONED' }
                : r
            )
          );
        }
      }
    });
  }

}
