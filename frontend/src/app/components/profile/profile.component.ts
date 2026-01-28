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
import { SubscribeService } from '../../services/subscribe.service';
import { UserResponse } from '../../models/user-response.model';

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
  private subscribeService = inject(SubscribeService);
  showConnectionsPopup = signal(false);
  popupTitle = signal<'Followers' | 'Following'>('Followers');
  connections = signal<UserResponse[]>([]);
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

  get isFollowing() {
    return this.profile()?.following;
  }

  get isMyProfile() {
    return this.profile()?.myProfile;
  }


  loadProfile(userId: string) {
    this.profileService.getProfile(userId).subscribe(res => {
      if (res.success) {
        this.profile.set(res.data);
      }
    });
  }

  loadPosts(userId: string) {
    this.postService.getProfilePosts(userId).subscribe(res => {
      if (res.success) {
        console.log('res.data :>> ', res.data);
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
    const profile = this.profile();
    if (!profile) return;

    const formData = new FormData();

    formData.append(
      'data',
      new Blob(
        [JSON.stringify({
          firstName: this.editForm.firstName,
          lastName: this.editForm.lastName,
          username: this.editForm.username,
          email: this.editForm.email,
          about: this.editForm.about,
        })],
        { type: 'application/json' }
      )
    );

    if (this.editForm.profileImage) {
      formData.append('profileImage', this.editForm.profileImage);
    }

    this.profileService.updateProfile(profile.id, formData)
      .subscribe({
        next: (res) => {
          if (res.success) {
            this.profile.set(res.data);
            this.closeEditPopup();
          }
        },
        error: (err) => {
          console.error('Update failed', err);
        }
      });
  }

  openFollowers() {
    const profile = this.profile();
    if (!profile) return;

    this.popupTitle.set('Followers');
    this.showConnectionsPopup.set(true);

    this.subscribeService.getSubscribers(profile.id)
      .subscribe(res => {
        if (res.success) {
          this.connections.set(res.data);
        }
      });
  }

  openFollowing() {
    const profile = this.profile();
    if (!profile) return;

    this.popupTitle.set('Following');
    this.showConnectionsPopup.set(true);

    this.subscribeService.getSubscribed(profile.id)
      .subscribe(res => {
        if (res.success) {
          this.connections.set(res.data);
        }
      });
  }


  closeConnectionsPopup() {
    this.showConnectionsPopup.set(false);
    this.connections.set([]);
  }

  toggleFollow() {
    const profile = this.profile();
    if (!profile || profile.myProfile) return;

    if (profile.following) {
      this.unsubscribe(profile.id);
    } else {
      this.subscribe(profile.id);
    }
  }

  subscribe(userId: string) {
    this.subscribeService.subscribe(userId).subscribe(res => {
      if (res.success) {
        this.profile.update(p => p && ({
          ...p,
          following: true,
          nbr_of_followers: p.nbr_of_followers + 1
        }));
      }
    });
  }

  unsubscribe(userId: string) {
    this.subscribeService.unsubscribe(userId).subscribe(res => {
      if (res.success) {
        this.profile.update(p => p && ({
          ...p,
          following: false,
          nbr_of_followers: p.nbr_of_followers - 1
        }));
      }
    });
  }


}
