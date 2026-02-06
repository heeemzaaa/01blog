import { CommonModule } from '@angular/common';
import {
  Component,
  EventEmitter,
  Input,
  Output,
  ViewChild,
  effect,
  inject,
  input,
  signal,
} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule, MatMenuTrigger } from '@angular/material/menu';
import { MatDialog } from '@angular/material/dialog';

import { PostResponse } from '../../models/post-response.model';
import { LikeService } from '../../services/like.service';
import { ReportService } from '../../services/report.service';
import { PostService } from '../../services/post.service';
import { ReportTarget } from '../../models/report-target.enum';
import { ReportDialogComponent } from '../report-dialog/report-dialogcomponent';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink,
    MatIconModule,
    MatMenuModule,
  ],
  templateUrl: './post.component.html',
  styleUrl: './post.component.css',
})
export class Post {
  /* ================= Inputs / Outputs ================= */

  post = input.required<PostResponse>();
  @Input() showActions = true;
  @Output() postDeleted = new EventEmitter<string>();

  @ViewChild(MatMenuTrigger) deleteMenuTrigger!: MatMenuTrigger;

  /* ================= Services ================= */

  private likeService = inject(LikeService);
  private reportService = inject(ReportService);
  private postService = inject(PostService);
  private dialog = inject(MatDialog);

  /* ================= State ================= */

  postState = signal<PostResponse | null>(null);
  selectedMedia = signal<any | null>(null);
  showEditPopup = signal(false);

  // 🔥 Media management
  existingMedias = signal<any[]>([]);
  newMedias = signal<File[]>([]);
  deletedMediaIds = signal<string[]>([]);
  newMediaPreviews = signal<{ file: File; preview: string }[]>([]);


  editForm = signal({
    title: '',
    content: '',
  });

  constructor() {
    effect(() => {
      const p = this.post();
      if (p) this.postState.set(p);
    });
  }

  /* ================= Media helpers ================= */

  mainMedia() {
    return this.selectedMedia() ?? this.postState()?.medias?.[0];
  }

  isImage(media: any) {
    return media?.mimeType?.startsWith('image') || media?.type === 'IMAGE';
  }

  isVideo(media: any) {
    return media?.mimeType?.startsWith('video') || media?.type === 'VIDEO';
  }

  selectMedia(media: any) {
    this.selectedMedia.set(media);
  }

  /* ================= Actions ================= */

  toggleLike() {
    const post = this.postState();
    if (!post) return;

    const prev = post.liked;
    const count = post.likesCount;

    this.postState.update(p =>
      p
        ? {
          ...p,
          liked: !p.liked,
          likesCount: p.likesCount + (p.liked ? -1 : 1),
        }
        : p
    );

    this.likeService.toggleLike(post.id).subscribe({
      error: () =>
        this.postState.update(p =>
          p ? { ...p, liked: prev, likesCount: count } : p
        ),
    });
  }

  reportPost() {
    const post = this.postState();
    if (!post) return; const dialogRef = this.dialog.open(ReportDialogComponent, {
      width: '400px',
      data: {
        targetType: ReportTarget.POST,
        targetId: post.id,
        subjectLabel: post.title,
      },
    });
    dialogRef.afterClosed().subscribe(result => {
      if (!result) return;
      this.reportService.createReport(result).subscribe();
    });
  }

  /* ================= Delete post ================= */

  confirmDelete() {
    this.deleteMenuTrigger.closeMenu();
    this.deletePost();
  }

  closeDeleteMenu() {
    this.deleteMenuTrigger.closeMenu();
  }

  deletePost() {
    const post = this.postState();
    if (!post) return;

    this.postService.deletePost(post.id).subscribe({
      next: () => this.postDeleted.emit(post.id),
    });
  }

  /* ================= Edit ================= */

  openEditPopup() {
    const post = this.postState();
    if (!post) return;

    this.editForm.set({
      title: post.title,
      content: post.content,
    });

    this.existingMedias.set([...post.medias]);
    this.newMedias.set([]);
    this.deletedMediaIds.set([]);
    this.selectedMedia.set(null);

    this.showEditPopup.set(true);
  }

  closeEditPopup() {
    this.showEditPopup.set(false);
  }

  removeExistingMedia(id: string) {
    this.existingMedias.update(m => m.filter(x => x.id !== id));
    this.deletedMediaIds.update(ids => [...ids, id]);
  }

  removeNewMedia(index: number) {
    const item = this.newMediaPreviews()[index];
    URL.revokeObjectURL(item.preview);

    this.newMediaPreviews.update(list =>
      list!.filter((_, i) => i !== index)
    );
  }



  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files) return;

    const previews = this.newMediaPreviews();
    const maxAllowed =
      5 - this.existingMedias().length - previews.length;


    for (const file of Array.from(input.files).slice(0, maxAllowed)) {
      const preview = URL.createObjectURL(file);

      this.newMediaPreviews.update(p => [
        ...p!,
        { file, preview },
      ]);
    }

    input.value = '';
  }


  submitEdit() {
    const post = this.postState();
    if (!post) return;

    const formData = new FormData();

    formData.append(
      'post',
      new Blob(
        [JSON.stringify(this.editForm())],
        { type: 'application/json' }
      )
    );

    this.newMediaPreviews().forEach(item =>
      formData.append('medias', item.file)
    );


    formData.append(
      'deletedMediaIds',
      new Blob(
        [JSON.stringify(this.deletedMediaIds())],
        { type: 'application/json' }
      )
    );

    this.postService.updatePost(formData, post.id).subscribe({
      next: res => {
        if (res?.success) {
          this.postState.set(res.data);
          this.closeEditPopup();
        }
      },

      error: (err) => {
        console.log(err);
      }
    });

    this.newMediaPreviews.set([]);
  }
}
