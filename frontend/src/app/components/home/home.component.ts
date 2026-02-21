import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { Post } from '../post/post.component';
import { PostService } from '../../services/post.service';
import { PostResponse } from '../../models/post-response.model';
import { signal } from '@angular/core';
import { UserResponse } from '../../models/user-response.model';
import { ToastService } from '../../services/toast.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, MatIconModule, Post],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {

  currentUser = signal<UserResponse | null>(null);
  private authService = inject(AuthService);
  private postService = inject(PostService);
  private router = inject(Router);

  posts = signal<PostResponse[]>([]);

  constructor() {
    this.loadFeed();
  }

    toProfile(userId: string) {
    this.router.navigate([`/profile/${userId}`])
  }

  loadFeed() {
    this.postService.getFeedPosts().subscribe(res => {
      if (res.success) {
        this.posts.set(res.data);
      }
    });

    this.authService.getMe().subscribe(res => {
      if (res?.success) {
        this.currentUser?.set(res.data);
      }
    })
  }
}
