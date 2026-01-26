import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'PostComponent',
  imports: [MatIconModule, CommonModule],
  templateUrl: './post.html',
  styleUrl: './post.css',
})
export class Post {
  isLiked = false;

  toggleLike() {
    this.isLiked = !this.isLiked;
  }
}
