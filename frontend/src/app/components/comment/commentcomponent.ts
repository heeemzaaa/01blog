import { CommonModule } from '@angular/common';
import { Component, computed, inject, Input, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommentResponse } from '../../models/comment-response.model';
import { LikeService } from '../../services/like.service';
import { ReportService } from '../../services/report.service';
import { CommentService } from '../../services/comment.service';
import { CommentRequest } from '../../models/comment-request.model';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-comment',
  imports: [CommonModule, MatIconModule],
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css',
})
export class CommentComponent {
  @Input() comment!: CommentResponse;
  openMenu = signal(false);

  toggleOpen() {
    this.openMenu.update(prev => !prev);
  }
}
