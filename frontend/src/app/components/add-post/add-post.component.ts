import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MediaPreview } from '../../models/media-preview.model';
import { PostRequest } from '../../models/post-request.model';
import { PostService } from '../../services/post.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-add-post',
  standalone: true,
  imports: [CommonModule, MatIconModule, FormsModule],
  templateUrl: './add-post.component.html',
  styleUrl: './add-post.component.css',
})
export class AddPost {
  postService = inject(PostService);
  router = inject(Router);
  toast = inject(ToastService);
  selectedFiles = signal<File[]>([]);
  mediaPreviews = signal<MediaPreview[]>([]);
  title = signal('');
  content = signal('');

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files) return;

    const files = Array.from(input.files);

    for (const file of files) {
      if (this.selectedFiles().length >= 5) {
        this.toast.showError('Maximum 5 media files allowed');
        break;
      }

      this.selectedFiles.update(files => [...files, file]);

      const reader = new FileReader();
      reader.onload = () => {
        this.mediaPreviews.update(previews => [
          ...previews,
          {
            url: reader.result as string,
            type: file.type.startsWith('image') ? 'image' : 'video',
          },
        ]);
      };
      reader.readAsDataURL(file);
    }

    input.value = '';
  }

  removeMedia(index: number) {
    this.selectedFiles.update(files =>
      files.filter((_, i) => i !== index)
    );

    this.mediaPreviews.update(previews =>
      previews.filter((_, i) => i !== index)
    );
  }

  private buildFormData() {
    const formData = new FormData();

    const postRequest: PostRequest = {
      title: this.title(),
      content: this.content(),
    };

    const postJson = new Blob(
      [JSON.stringify(postRequest)],
      { type: 'application/json' }
    );

    formData.append('post', postJson);

    this.selectedFiles().forEach(file => {
      formData.append('medias', file);
    });

    return formData;
  }

  addPost() {
    if (this.title() === '' || this.content() === '') {
      this.toast.showError("The title and the content are required !");
      return;
    }
    const formData = this.buildFormData();

    this.postService.createPost(formData).subscribe({
      next: () => {
        this.toast.showSuccess("Post created successfully !");

        this.title.set('');
        this.content.set('');
        this.selectedFiles.set([]);
        this.mediaPreviews.set([]);
        this.router.navigate(['/']);
      },
      error: (err) => {
        if (err.error.status == 401 || err.error.message == "User not found") {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
          return
        }
        this.selectedFiles.set([]);
        this.mediaPreviews.set([]);
        this.toast.showError("Post creation failed, Please try again later !")
      }
    })
  }
}
