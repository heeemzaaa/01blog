import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  email = '';
  password = '';
  errorMessage = signal<string | null>(null);

  constructor(private authService: AuthService, private router: Router) { }

  onLogin() {
    if (!this.email || !this.password) {
      this.errorMessage.set('Please fill in all fields');
      return;
    }

    const credentials = {
      email: this.email,
      password: this.password
    };

    this.authService.login(credentials).subscribe({
      next: (response) => {

        if (response.data.token) {
          this.authService.saveToken(response.data.token);
        }

        this.router.navigate(['/']);
      },

      error: (err) => {
        console.log('err :>> ', err);
        this.errorMessage.set(err.error.message);
      }
    })
  }
}
