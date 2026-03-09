import { CommonModule } from '@angular/common';
import { Component, computed, effect, inject, Input, input, signal } from '@angular/core';
import { CommentResponse } from '../../models/comment-response.model';
import { ReportService } from '../../services/report.service';
import { CommentService } from '../../services/comment.service';
import { MatIconModule } from '@angular/material/icon';
import { ReportDialogComponent } from '../report-dialog/report-dialogcomponent';
import { MatDialog } from '@angular/material/dialog';
import { ReportTarget } from '../../models/report-target.enum';
import { MatMenuModule } from '@angular/material/menu';
import { FormsModule } from '@angular/forms';
import { UtilsService } from '../../services/utils.service';
import { AuthService } from '../../services/auth.service';
import { ToastService } from '../../services/toast.service';
import { Router } from '@angular/router';

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
  private utilsService = inject(UtilsService);
  private authService = inject(AuthService);
  private toast = inject(ToastService);
  private router = inject(Router);

  currentUser = this.authService.currentUser;

  isAdmin = computed(() => this.currentUser()?.role === 'ADMIN');

  showEditPopup = signal(false);

  @Input() postOwnerId!: string;
  isPostOwner = computed(() =>
    this.currentUser()?.id === this.postOwnerId
  );

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

  timeAgo(date: string): string {
    return this.utilsService.timeAgo(date);
  }

  /* ================= USER ACTIONS ================= */

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
      this.reportService.createReport(result).subscribe();
    });
  }

  openEditPopup() {
    const comment = this.commentState();
    if (!comment) return;

    this.editForm.content = comment.content;
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
        [JSON.stringify({ content: this.editForm.content })],
        { type: 'application/json' }
      )
    );

    if (this.editForm.commentImage) {
      formData.append('commentImage', this.editForm.commentImage);
    }

    this.commentService.updateComment(comment.id, formData).subscribe({
      next: (res) => {
        if (res?.success) {
          this.toast.showSuccess("Comment updated successfully !");
          this.commentState.set(res.data);
          this.closeEditPopup();
        }
      },
      error: (err) => {
        if (err.error.status == 401 || err.error.message == "User not found") {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("Comment not updated due to some errors, please try again later !")
      }
    });
  }

  deleteComment() {
    const comment = this.commentState();
    const postId = this.postId();
    if (!comment || !postId) return;

    this.commentService.deleteComment(comment.id, postId).subscribe({
      next: () => {
        this.toast.showSuccess("Comment deleted successfully !");
        this.commentState.set(null);
      },
      error: (err) => {
        if (err.error.status == 401 || err.error.message == "User not found") {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("Error deleting the comment, please try again later !")
      }
    });
  }

  /* ================= ADMIN ACTIONS ================= */

  adminHideComment() {
    const comment = this.commentState();
    if (!comment) return;

    this.commentService.hideComment(comment.id).subscribe({
      next: () => {
        this.toast.showSuccess("Comment hidden successfully !");
        this.commentState.update(c =>
          c ? { ...c, visible: false } : c
        );
      },
      error: (err) => {
        if (err.error.status == 401 || err.error.message == "User not found") {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("Error hidding the comment !")
      }
    });
  }

  adminRestoreComment() {
    const comment = this.commentState();
    if (!comment) return;

    this.commentService.restoreComment(comment.id).subscribe({
      next: () => {
        this.toast.showSuccess("Comment restored successfully !");
        this.commentState.update(c =>
          c ? { ...c, visible: true } : c
        );
      },
      error: (err) => {
        if (err.error.status == 401 || err.error.message == "User not found") {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("Error restoring the comment !")
      }
    });
  }

  adminDeleteComment() {
    const comment = this.commentState();
    if (!comment) return;

    this.commentService.adminDeleteComment(comment.id).subscribe({
      next: () => {
        this.toast.showSuccess("Comment deleted successfully !");
        this.commentState.set(null);
      },
      error: (err) => {
        if (err.error.status == 401 || err.error.message == "User not found") {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.toast.showError("Error deleting the comment !")
      }
    });
  }

  toProfile(userId: string) {
    this.router.navigate([`/profile/${userId}`])
  }

  showDeletePopup = signal(false);

  openDeletePopup() {
    this.showDeletePopup.set(true);
  }

  closeDeletePopup() {
    this.showDeletePopup.set(false);
  }

  confirmDelete() {
    this.closeDeletePopup();
    this.deleteComment();
  }
}
