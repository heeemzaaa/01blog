import { Component, computed, inject, signal, effect } from '@angular/core';
import { Post } from "../post/post.component";
import { ActivatedRoute } from '@angular/router';
import { PostResponse } from '../../models/post-response.model';
import { PostService } from '../../services/post.service';

@Component({
  selector: 'app-post-details',
  imports: [Post],
  templateUrl: './post-details.component.html',
  styleUrl: './post-details.component.css',
})
export class PostDetailsComponent {
  private route = inject(ActivatedRoute);
  private postService = inject(PostService);


  readonly postId = computed(() =>
    this.route.snapshot.paramMap.get('id')
  );

  readonly post = signal<PostResponse | null>(null);

  constructor() {
    effect(() => {
      const id = this.postId();
      if (!id) return;

      this.postService.getPostById(id).subscribe({
        next: res => this.post.set(res.data),
        error: () => console.error('Failed to load post'),
      });
    });
  }

}
