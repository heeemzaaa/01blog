import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.css'
})
export class AdminDashboard {

  activeTab = signal<'users' | 'posts' | 'reports'>('users');

  setTab(tab: 'users' | 'posts' | 'reports') {
    this.activeTab.set(tab);
  }

}
