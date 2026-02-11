import { Routes } from '@angular/router';
import { AdminLayout } from './admin-layout.component';
import { AdminDashboard } from './dashboard/admin-dashboard.component';
import { adminGuard } from '../../guards/admin-guard';

export const ADMIN_ROUTES: Routes = [
  {
    path: '',
    component: AdminLayout,
    canActivate: [adminGuard],
    children: [
      {
        path: '',
        component: AdminDashboard
      },
      {
        path: 'users',
        loadComponent: () =>
          import('./users/users.component').then(m => m.UsersComponent)
      },
      {
        path: 'posts',
        loadComponent: () =>
          import('./posts/posts.component').then(m => m.PostsComponent)
      },
      {
        path: 'reports',
        loadComponent: () =>
          import('./reports/reports.component').then(m => m.ReportsComponent)
      }
    ]
  }
];
