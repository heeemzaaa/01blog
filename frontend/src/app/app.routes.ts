import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { RegisterComponent } from './pages/register/register';
import { authGuard } from './guards/auth-guard';
import { guestGuard } from './guards/guest-guard';

export const routes: Routes = [
    {
        path: '',
        loadComponent: () => import('./pages/homePage/homePage').then((m) => m.HomeLayout),
        loadChildren: () => import('./pages/homePage/app.router').then((m) => m.routes),
        canActivate: [authGuard]
    },
    { path: 'login', component: Login, canActivate: [guestGuard] },
    { path: 'register', component: RegisterComponent, canActivate: [guestGuard] },
];
