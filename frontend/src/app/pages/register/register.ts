import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth';

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
    password: '',
    profileImage: ''
  };

  errorMessage = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  onFileSelected(event: any) {
    const file = event.target.files[0];

    if (file) {
      const reader = new FileReader();

      reader.onload = () => {
        this.formData.profileImage = reader.result as string;
      };

      reader.readAsDataURL(file);
    }
  }

  removeImage() {
    this.formData.profileImage = '';
  }

  onNext() {
    if (this.part == 1) {
      if (!this.formData.firstName || !this.formData.lastName || !this.formData.email || !this.formData.password) {
        this.errorMessage = 'Please fill in all fields before proceeding.';
        return;
      }
      this.part = 2;
      this.errorMessage = '';
    }
  }

  onBack() {
    if (this.part == 2) {
      this.part = 1;
      this.errorMessage = '';
    }
  }

  onRegister() {
    if (!this.formData.email || !this.formData.password || !this.formData.username || !this.formData.firstName || !this.formData.lastName) {
      this.errorMessage = 'Please fill in all required fields';
      return;
    }

    // Call the Service
    this.authService.register(this.formData).subscribe({
      next: (response) => {
        console.log('Registration successful');

        // Save token immediately so they don't have to login again
        if (response.data.token) {
          this.authService.saveToken(response.data.token);
        }

        // Go to Home Page
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = 'Registration failed. Email or Username might be taken.';
      }
    });
  }
}