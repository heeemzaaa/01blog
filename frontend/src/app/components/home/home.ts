import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth'; 
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, MatIconModule], 
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class HomeComponent {
  // currentUser$: Observable<any> | undefined;
  currentUser = inject(AuthService).currentUser;

  
}