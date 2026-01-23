import { Component, inject } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nav-bar',
  standalone: true,
  imports: [MatIconModule],
  templateUrl: './nav-bar.html',
  styleUrl: './nav-bar.css',
})

export class NavBar {
  router = inject(Router);

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
