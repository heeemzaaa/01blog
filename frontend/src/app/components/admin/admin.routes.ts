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
        path: 'reports/:id',
        loadComponent: () =>
          import('./report-details/admin-report-details.component')
            .then(m => m.AdminReportDetails),
        canActivate: [adminGuard]
      }

    ]
  }
];
