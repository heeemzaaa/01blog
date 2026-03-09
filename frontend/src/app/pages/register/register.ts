import { Component, signal } from '@angular/core';
import { Router, RouterLink } from "@angular/router";
import { AuthService } from "../../services/auth.service";
import { FormsModule } from "@angular/forms";

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class RegisterComponent {

  part = 1;

  formData = {
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    about: '',
    password: ''
  };

  selectedImage: File | null = null;
  imagePreview: string | null = null;

  errorMessage = signal<string | null>(null);
  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  onNext() {
    if (this.part == 1) {
      if (!this.formData.firstName || !this.formData.lastName || !this.formData.email || !this.formData.password) {
        this.errorMessage.set('Please fill in all fields before proceeding.');
        return;
      }
      this.part = 2; this.errorMessage.set('');
    }
  }
  onBack() {
    if (this.part == 2) {
      this.part = 1; this.errorMessage.set('');;
    }
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;

    if (!input.files || input.files.length === 0) return;

    this.selectedImage = input.files[0];

    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result as string;
    };
    reader.readAsDataURL(this.selectedImage);
  }

  removeImage() {
    this.selectedImage = null;
    this.imagePreview = null;
  }

  onRegister() {
    if (!this.formData.email || !this.formData.password || !this.formData.username) {
      this.errorMessage.set('Please fill in all required fields');
      return;
    }

    const payload = new FormData();

    payload.append('firstName', this.formData.firstName);
    payload.append('lastName', this.formData.lastName);
    payload.append('username', this.formData.username);
    payload.append('email', this.formData.email);
    payload.append('password', this.formData.password);
    if (this.formData.about && this.formData.about.trim() !== '') {
      payload.append('about', this.formData.about.trim());
    }
    if (this.selectedImage) {
      payload.append('profileImage', this.selectedImage);
    }

    this.authService.register(payload).subscribe({
      next: (response) => {
        if (response.data.token) {
          this.authService.saveToken(response.data.token);
        }
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.errorMessage.set(err.error.message);
      }
    });
  }
}
