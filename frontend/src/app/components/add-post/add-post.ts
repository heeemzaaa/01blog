import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-add-post',
  imports: [MatIconModule],
  templateUrl: './add-post.html',
  styleUrl: './add-post.css',
})
export class AddPost {
  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;

    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];
    console.log(file);
  }

  addPost() {
    
  }
}
