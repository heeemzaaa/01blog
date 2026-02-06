import { CommonModule } from '@angular/common';
import { Component, computed, effect, inject, input, Input, signal } from '@angular/core';
import { CommentResponse } from '../../models/comment-response.model';
import { ReportService } from '../../services/report.service';
import { CommentService } from '../../services/comment.service';
import { MatIconModule } from '@angular/material/icon';
import { ReportDialogComponent } from '../report-dialog/report-dialogcomponent';
import { MatDialog } from '@angular/material/dialog';
import { ReportTarget } from '../../models/report-target.enum';
import { MatMenuModule } from '@angular/material/menu';
import { FormsModule, NgModel } from '@angular/forms';

@Component({
  selector: 'app-comment',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatMenuModule, FormsModule],
  templateUrl: './comment.component.html',
  styleUrl: './comment.component.css',
})
export class CommentComponent {
  comment = input<CommentResponse>();
  postId = input<string>();
  commentState = signal<CommentResponse | null>(null);
  private dialog = inject(MatDialog);
  private reportService = inject(ReportService);
  private commentService = inject(CommentService);
  showEditPopup = signal(false);
  editForm = {
    content: '',
    commentImage: null as File | null
  };


  constructor() {
    effect(() => {
      const c = this.comment();
      if (c) {
        this.commentState.set(c);
      }
    });
  }

  reportComment() {
    const dialogRef = this.dialog.open(ReportDialogComponent, {
      width: '400px',
      data: {
        targetType: ReportTarget.COMMENT,
        targetId: this.comment()?.id,
        subjectLabel: this.comment()?.content,
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


  openEditPopup() {
    this.showEditPopup.set(true);
  }

  closeEditPopup() {
    this.showEditPopup.set(false);
  }

  onImageSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.editForm.commentImage = input.files[0];
    }
  }

  editComment() {
    const comment = this.commentState();
    if (!comment) return;

    const formData = new FormData();
    formData.append(
      'data',
      new Blob(
        [JSON.stringify({
          content: this.editForm.content,
        })],
        { type: 'application/json' }
      )
    );

    if (this.editForm.commentImage) {
      formData.append('commentImage', this.editForm.commentImage);
    }


    this.commentService.updateComment(comment.id, formData).subscribe({
      next: (res) => {
        if (res?.success) {
          this.commentState.set(res.data);
          this.closeEditPopup();
        }
      },

      error: (err) => {
        console.error(err);
      }
    })
  }


  deleteComment() {
    const comment = this.commentState();
    const postId = this.postId();

    if (!comment || !postId) return;

    this.commentService.deleteComment(comment.id, postId).subscribe({
      next: (res) => {
        if (res?.success) {
          this.commentState.set(null);
        }
      },

      error: (err) => {
        console.error(err);
      }
    })
  }


}
