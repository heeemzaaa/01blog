import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { SearchService } from '../../services/search.service';
import { switchMap } from 'rxjs';

@Component({
    selector: 'app-search',
    standalone: true,
    imports: [RouterLink],
    templateUrl: './search.component.html',
    styleUrl: './search.component.css',
})
export class SearchComponent {

    route = inject(ActivatedRoute);
    searchService = inject(SearchService);
    router = inject(Router);

    results = signal<any>(null);
    loading = signal<boolean>(false);
    currentPage = signal<number>(0);
    totalPages = signal<number>(0);

    constructor() {
        this.route.queryParams.subscribe(params => {
            const query = params['q'];
            if (!query) return;

            this.currentPage.set(0);
            this.loadResults(query, 0);
        });
    }

    loadResults(query: string, page: number) {
        this.loading.set(true);

        this.searchService.search(query, page).subscribe({
            next: (res) => {
                this.results.set(res);
                const usersPages = res.users?.totalPages ?? 0;
                const postsPages = res.posts?.totalPages ?? 0;
                this.totalPages.set(Math.max(usersPages, postsPages));
                this.currentPage.set(page);
                this.loading.set(false);
            },
            error: (err) => {
        if (err.error.status == 401 || err.error.message == "User not found") {
                    localStorage.removeItem('token');
                    this.router.navigate(['/login']);
                    return
                }
            }

        });
    }


    nextPage(query: string) {
        if (this.currentPage() < this.totalPages() - 1) {
            this.loadResults(query, this.currentPage() + 1);
        }
    }

    previousPage(query: string) {
        if (this.currentPage() > 0) {
            this.loadResults(query, this.currentPage() - 1);
        }
    }

}
