import { Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { RegisterComponent } from './pages/register/register';
import { authGuard } from './guards/auth-guard';
import { guestGuard } from './guards/guest-guard';
import { SearchComponent } from './components/search/search.component';

export const routes: Routes = [

    {
        path: '',
        loadComponent: () =>
            import('./pages/homePage/homePage').then((m) => m.HomeLayout),
        loadChildren: () =>
            import('./pages/homePage/app.router').then((m) => m.routes),
        canActivate: [authGuard]
    },

    {
        path: 'admin',
        loadChildren: () =>
            import('./components/admin/admin.routes').then(m => m.ADMIN_ROUTES)
    },

    { path: 'login', component: Login, canActivate: [guestGuard] },
    { path: 'register', component: RegisterComponent, canActivate: [guestGuard] },
    {
        path: 'search',
        component: SearchComponent
    }

];
