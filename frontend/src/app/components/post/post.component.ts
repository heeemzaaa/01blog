import { CommonModule } from '@angular/common';
import { Component, Input, inject } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { PostResponse } from '../../models/post-response.model';
import { LikeService } from '../../services/like.service';
import { MatMenuModule } from '@angular/material/menu';
import { MatDialog } from '@angular/material/dialog';
import { ReportTarget } from '../../models/report-target.enum';
import { ReportService } from '../../services/report.service';
import { ReportDialogComponent } from '../report-dialog/report-dialogcomponent';
import { Router, RouterLink } from '@angular/router';


@Component({
  selector: 'app-post',
  imports: [MatIconModule, CommonModule, MatMenuModule, RouterLink],
  templateUrl: './post.component.html',
  styleUrl: './post.component.css',
})
export class Post {
  @Input() post!: PostResponse;
  @Input() showActions = true;
  private dialog = inject(MatDialog);
  private reportService = inject(ReportService);
  private likeService = inject(LikeService);
  router = inject(Router);

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
    const dialogRef = this.dialog.open(ReportDialogComponent, {
      width: '400px',
      data: {
        targetType: ReportTarget.POST,
        targetId: this.post.id,
        subjectLabel: this.post.title,
      },
    });

    dialogRef.afterClosed().subscribe(result => {
      if (!result) return;

      this.reportService.createReport(result).subscribe({
        next: () => {
          console.log('Report sent successfully');
        },
        error: () => {
          console.error('Error while reporting');
        },
      });
    });
  }

}
