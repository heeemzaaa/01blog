import { CommonModule } from '@angular/common';
import { Component, Input, inject } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { PostResponse } from '../../models/post-response.model';
import { LikeService } from '../../services/like.service';

@Component({
  selector: 'PostComponent',
  imports: [MatIconModule, CommonModule],
  templateUrl: './post.component.html',
  styleUrl: './post.component.css',
})
export class Post {
  @Input() post!: PostResponse;

  private likeService = inject(LikeService);

  toggleLike() {
    const previousState = this.post.liked;
    const previousCount = this.post.likesCount;

    this.post.liked = !this.post.liked;
    this.post.likesCount += this.post.liked ? 1 : -1;

    this.likeService.toggleLike(this.post.id).subscribe({
      error: () => {
        this.post.liked = previousState;
        this.post.likesCount = previousCount;
      },
    });
  }

  reportPost() {
    console.log('Report post:', this.post.id);
    // later → call backend
  }
}
