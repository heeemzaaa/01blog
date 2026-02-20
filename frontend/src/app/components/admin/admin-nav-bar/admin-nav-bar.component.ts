import { CommonModule } from "@angular/common";
import { Component, inject } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { MatIconModule } from "@angular/material/icon";
import { Router } from "@angular/router";
import { AuthService } from "../../../services/auth.service";


@Component({
    selector: 'admin-nav-bar',
    standalone: true,
    imports: [MatIconModule, CommonModule, ReactiveFormsModule],
    templateUrl: './admin-nav-bar.component.html',
    styleUrl: './admin-nav-bar.component.css',
})
export class AdminNavBar {
    private router = inject(Router);
    private authService = inject(AuthService);

    toHome() {
        if (this.router.url !== '/') {
            this.router.navigate(['/']);
        }
    }

    toNotifications() {
        if (this.router.url !== '/notifications') {
            this.router.navigate(['/notifications']);
        }
    }

    logout() {
        this.authService.logout();
        this.router.navigate(['/login']);
    }
}