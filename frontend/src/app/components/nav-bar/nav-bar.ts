import { Component, inject, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-nav-bar',
  standalone: true,
  imports: [MatIconModule, RouterLink, CommonModule],
  templateUrl: './nav-bar.html',
  styleUrl: './nav-bar.css',
})

export class NavBar {
  isMenuOpen = signal<boolean>(false);
  router = inject(Router);
  currentUser = inject(AuthService).currentUser;
  authService = inject(AuthService);

  toggleMenuOpen() {
    this.isMenuOpen.set(!this.isMenuOpen());
  }

  closeMenu() {
    this.isMenuOpen.set(false);
  }

  logout() {
  this.authService.logout();
  this.closeMenu();
  this.router.navigate(['/login']);
}

  toAddPost() {
    if (this.router.url !== '/add-post') {
      this.router.navigate(['/add-post']);
    }
  }

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
}
