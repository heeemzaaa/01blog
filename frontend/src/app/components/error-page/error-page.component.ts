import { Component, inject } from "@angular/core";
import { Router } from "@angular/router";


@Component({
    selector: 'app-comment',
    standalone: true,
    imports: [],
    templateUrl: './error-page.component.html',
    styleUrl: './error-page.component.css',
})
export class ErrorPage {
    private router = inject(Router);

    toHomePage() {
        this.router.navigate(['/']);
    }
}