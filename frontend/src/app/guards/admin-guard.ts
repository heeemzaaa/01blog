import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { AuthService } from "../services/auth.service";
import { Role } from "../models/user-role.model";

export const adminGuard: CanActivateFn = () => {

    const authService = inject(AuthService);
    const router = inject(Router);

    const user = authService.currentUser();

    if (user && user.role === Role.ADMIN) {
        return true;
    }

    router.navigate(['/']);
    return false;
};
