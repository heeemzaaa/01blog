import { Component, OnInit } from '@angular/core'; 
import { RouterOutlet } from '@angular/router';
import { AuthService } from './services/auth'; 
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-root',
  standalone: true, 
  imports: [RouterOutlet, MatIconModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  constructor(private authService: AuthService) {}

  ngOnInit() {
    this.authService.getMe().subscribe();
  }
}