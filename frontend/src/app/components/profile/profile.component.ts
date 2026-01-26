import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { Post } from '../post/post.component';
import { ProfileService } from '../../services/profile.service';
import { PostService } from '../../services/post.service';
import { ProfileResponse } from '../../models/profile-response.model';
import { PostResponse } from '../../models/post-response.model';
import { signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-profile',
  imports: [CommonModule, MatIconModule, Post, FormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css',
})
export class Profile {

  private route = inject(ActivatedRoute);
  private profileService = inject(ProfileService);
  private postService = inject(PostService);
  profile = signal<ProfileResponse | null>(null);
  posts = signal<PostResponse[]>([]);
  showEditPopup = signal(false);
  editForm = {
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    about: '',
    profileImage: null as File | null
  };

  constructor() {
    const userId = this.route.snapshot.paramMap.get('id');
    if (userId) {
      this.loadProfile(userId);
      this.loadPosts(userId);
    }
  }

  loadProfile(userId: string) {
    this.profileService.getProfile(userId).subscribe(res => {
      if (res.success) {
        console.log('res.data :>> ', res.data);
        this.profile.set(res.data);
      }
    });
  }

  loadPosts(userId: string) {
    this.postService.getProfilePosts(userId).subscribe(res => {
      if (res.success) {
        this.posts.set(res.data);
      }
    });
  }

  openEditPopup() {
    const p = this.profile();
    if (!p) return;

    this.editForm = {
      firstName: p.firstName,
      lastName: p.lastName,
      username: p.username,
      email: p.email,
      about: p.about ?? '',
      profileImage: null
    };

    this.showEditPopup.set(true);
  }

  closeEditPopup() {
    this.showEditPopup.set(false);
  }

  onImageSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.editForm.profileImage = input.files[0];
    }
  }

  submitEdit() {
    console.log('Edit payload:', this.editForm);

    // 🔜 Later:
    // this.profileService.updateProfile(formData).subscribe(...)

    this.closeEditPopup();
  }
}
