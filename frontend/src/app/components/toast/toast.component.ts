import { Component, inject, computed, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css']
})
export class ToastComponent {

  private toastService = inject(ToastService);
  toast = this.toastService.toast;

  constructor() {
    effect(() => {
      console.log("TOAST SIGNAL VALUE:", this.toast());
    });
  }


}
