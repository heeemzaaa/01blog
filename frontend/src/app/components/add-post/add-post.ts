import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MediaPreview } from '../../models/media-preview.model';

@Component({
  selector: 'app-add-post',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './add-post.html',
  styleUrl: './add-post.css',
})
export class AddPost {

  selectedFiles = signal<File[]>([]);
  mediaPreviews = signal<MediaPreview[]>([]);

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files) return;

    const files = Array.from(input.files);

    for (const file of files) {
      if (this.selectedFiles().length >= 5) {
        console.log('Maximum 5 media files allowed');
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

  addPost() {
    console.log('Post submit');
    console.log(this.selectedFiles());
  }
}
