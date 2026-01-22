import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { RegisterComponent } from './pages/register/register';
import { HomeComponent } from './pages/home/home';
import { authGuard } from './guards/auth-guard';
import { guestGuard } from './guards/guest-guard';

export const routes: Routes = [
    { path: '', component: HomeComponent, canActivate: [authGuard] }, 
    { path: 'login', component: Login, canActivate: [guestGuard] },
    { path: 'register', component: RegisterComponent, canActivate: [guestGuard] },
];
