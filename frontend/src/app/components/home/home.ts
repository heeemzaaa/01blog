import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { Post } from '../post/post';
import { PostService } from '../../services/post';
import { PostResponse } from '../../models/post-response.model';
import { signal } from '@angular/core';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, MatIconModule, Post],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class HomeComponent {

  currentUser = inject(AuthService).currentUser;
  private postService = inject(PostService);

  posts = signal<PostResponse[]>([]);

  constructor() {
    this.loadFeed();
  }

  loadFeed() {
    this.postService.getFeedPosts().subscribe(res => {
      if (res.success) {
        this.posts.set(res.data);
      }
    });
  }
}
