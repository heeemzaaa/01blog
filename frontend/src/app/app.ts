import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth.service';
import { MatIconModule } from '@angular/material/icon';
import { ToastComponent } from './components/toast/toast.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MatIconModule, ToastComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  constructor(private authService: AuthService) {
    this.authService.getMe().subscribe();
  }

}
