import { Component, computed, inject, signal, effect, input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { toSignal } from '@angular/core/rxjs-interop';


import { Post } from '../post/post.component';
import { CommentComponent } from '../comment/comment.component';

import { PostService } from '../../services/post.service';
import { CommentService } from '../../services/comment.service';

import { PostResponse } from '../../models/post-response.model';
import { CommentResponse } from '../../models/comment-response.model';
import { CommentRequest } from '../../models/comment-request.model';

@Component({
  selector: 'app-post-details',
  standalone: true,
  imports: [CommonModule, Post, CommentComponent],
  templateUrl: './post-details.component.html',
  styleUrl: './post-details.component.css',
})
export class PostDetailsComponent {

  private route = inject(ActivatedRoute);
  private postService = inject(PostService);
  private commentService = inject(CommentService);
  private paramMapSignal = toSignal(this.route.paramMap);

  readonly postId = computed(() =>
    this.paramMapSignal()?.get('id') ?? ''
  );


  /* -------------------- STATE -------------------- */

  post = signal<PostResponse | null>(null);
  comments = signal<CommentResponse[]>([]);

  commentRequest = signal<CommentRequest>({
    content: ''
  });

  loadingComments = signal(false);
 



  /* -------------------- EFFECTS -------------------- */

  constructor() {

    // Load post
    effect(() => {
      const id = this.postId();
      if (!id) return;

      this.postService.getPostById(id).subscribe({
        next: res => this.post.set(res.data),
        error: () => console.error('Failed to load post'),
      });
    });

    // Load comments
    effect(() => {
      const id = this.postId();
      if (!id) return;

      this.fetchComments(id);
    });
  }

  onPostUpdated(updatedPost: PostResponse) {
    this.post.set(updatedPost);
  }


  /* -------------------- COMMENTS -------------------- */

  fetchComments(postId: string) {
    this.loadingComments.set(true);

    this.commentService.getComments(postId).subscribe({
      next: res => {
        this.comments.set(res?.data ?? []);
        this.loadingComments.set(false);
      },
      error: err => {
        console.error('Error:', err);
        this.loadingComments.set(false);
      }
    });
  }

  addComment() {
    const postId = this.postId();
    const request = this.commentRequest();

    if (!postId || !request.content.trim()) return;

    this.commentService.createComment(postId, request).subscribe({
      next: res => {
        if (!res?.data) return;

        this.comments.update(prev => [...prev, res.data]);

        this.post.update(post =>
          post
            ? { ...post, commentsCount: post.commentsCount + 1 }
            : post
        );
        this.commentRequest.set({ content: '' });
      },
      error: err => console.error('Error:', err),
    });
  }


  onCommentChange(value: string) {
    this.commentRequest.update(req => ({
      ...req,
      content: value
    }));
  }
}
