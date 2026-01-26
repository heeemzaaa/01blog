import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { Post } from '../post/post';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, MatIconModule, Post],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class HomeComponent {
  currentUser = inject(AuthService).currentUser;
  isLiked = false;

  
}