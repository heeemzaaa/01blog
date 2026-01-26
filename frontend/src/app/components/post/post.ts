import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { PostResponse } from '../../models/post-response.model';

@Component({
  selector: 'PostComponent',
  imports: [MatIconModule, CommonModule],
  templateUrl: './post.html',
  styleUrl: './post.css',
})
export class Post {
  @Input() post!: PostResponse;

  toggleLike() {
    this.post.isLiked = !this.post.isLiked;
  }
}
