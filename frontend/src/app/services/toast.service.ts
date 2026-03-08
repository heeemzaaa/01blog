import { Injectable, signal } from '@angular/core';

export type ToastType = 'success' | 'error';

export interface ToastData {
  message: string;
  type: ToastType;
}

@Injectable({
  providedIn: 'root'
})
export class ToastService {

  toast = signal<ToastData | null>(null);

  show(message: string, type: ToastType) {
    this.toast.set({ message, type });

    setTimeout(() => {
      this.toast.set(null);
    }, 3000);
  }

  showSuccess(message: string) {
    this.show(message, 'success');
  }

  showError(message: string) {
    this.show(message, 'error');
  }
}
