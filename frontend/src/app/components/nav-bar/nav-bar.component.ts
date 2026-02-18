import { Component, HostListener, inject, signal } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { debounceTime, distinctUntilChanged, filter, switchMap } from 'rxjs/operators';
import { SearchService } from '../../services/search.service';


@Component({
  selector: 'app-nav-bar',
  standalone: true,
  imports: [MatIconModule, RouterLink, CommonModule, ReactiveFormsModule],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.css',
})

export class NavBar {
  router = inject(Router);
  authService = inject(AuthService);
  searchService = inject(SearchService);

  currentUser = this.authService.currentUser;

  isMenuOpen = signal<boolean>(false);

  searchControl = new FormControl('');
  searchResults = signal<any>(null);
  loading = signal<boolean>(false);
  showDropdown = signal<boolean>(false);

  constructor() {
    this.searchControl.valueChanges
      .pipe(
        debounceTime(400),
        distinctUntilChanged(),
        filter(value => !!value && value.trim().length >= 2),
        switchMap(value => {
          this.loading.set(true);
          return this.searchService.search(value!.trim(), 0);
        })
      )
      .subscribe(res => {
        this.searchResults.set(res.data); // because of ResponseData
        this.loading.set(false);
        this.showDropdown.set(true);
      });
  }

  @HostListener('document:click', ['$event'])
  closeDropdown(event: Event) {
    const clickedInside = (event.target as HTMLElement)
      .closest('.search-wrapper');

    if (!clickedInside) {
      this.showDropdown.set(false);
    }
  }

  toggleMenuOpen() {
    this.isMenuOpen.set(!this.isMenuOpen());
  }

  closeMenu() {
    this.isMenuOpen.set(false);
  }

  logout() {
    this.authService.logout();
    this.closeMenu();
    this.router.navigate(['/login']);
  }

  toAddPost() {
    if (this.router.url !== '/add-post') {
      this.router.navigate(['/add-post']);
    }
  }

  toHome() {
    if (this.router.url !== '/') {
      this.router.navigate(['/']);
    }
  }

  toNotifications() {
    if (this.router.url !== '/notifications') {
      this.router.navigate(['/notifications']);
    }
  }
}
