import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { MatIconModule } from '@angular/material/icon';
import { Post } from '../post/post.component';
import { PostService } from '../../services/post.service';
import { PostResponse } from '../../models/post-response.model';
import { signal } from '@angular/core';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, MatIconModule, Post],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class Profile {

  router = inject(Router);
  route = inject(ActivatedRoute);

  currentUser = inject(AuthService).currentUser;
  private postService = inject(PostService);

  posts = signal<PostResponse[]>([]);

  constructor() {
    const userId = this.route.snapshot.paramMap.get('id');
    if (userId) {
      this.loadProfilePosts(userId);
    }
  }

  loadProfilePosts(userId: string) {
    this.postService.getProfilePosts(userId).subscribe(res => {
      if (res.success) {
        this.posts.set(res.data);
      }
    });
  }

}
