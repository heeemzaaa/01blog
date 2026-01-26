import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, MatIconModule],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile {
  router = inject(Router);
  currentUser = inject(AuthService).currentUser;
}
