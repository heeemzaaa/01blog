import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth'; 
import { MeResponse } from '../../models/me-response.model'; 
import { CommonModule } from '@angular/common';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule], 
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class HomeComponent implements OnInit {
  currentUser$: Observable<any> | undefined;

  constructor(private authService: AuthService) {}

  ngOnInit() {
      this.currentUser$ = this.authService.currentUser$;
  }
}