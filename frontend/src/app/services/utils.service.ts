// utils.service.ts
import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class UtilsService {

    timeAgo(date: string): string {
        if (!date) return 'just now';
        const seconds = Math.floor((Date.now() - new Date(date).getTime()) / 1000);


        if (seconds < 60) return 'just now';
        if (seconds < 3600) return `${Math.floor(seconds / 60)} min ago`;
        if (seconds < 86400) return `${Math.floor(seconds / 3600)} hours ago`;
        return `${Math.floor(seconds / 86400)} days ago`;
    }
}
