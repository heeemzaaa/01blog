import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { RegisterComponent } from './pages/register/register';

export const routes: Routes = [
    {path: 'login', component: Login},
    { path: 'register', component: RegisterComponent }, // Add this line
    {path: '', redirectTo: 'login', pathMatch: 'full'}
];
