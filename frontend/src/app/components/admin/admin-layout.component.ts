import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavBar } from '../nav-bar/nav-bar.component';
import { CommonModule } from '@angular/common';
import { AdminNavBar } from './admin-nav-bar/admin-nav-bar.component';

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, AdminNavBar],
  templateUrl: './admin-layout.component.html',
})
export class AdminLayout {}
