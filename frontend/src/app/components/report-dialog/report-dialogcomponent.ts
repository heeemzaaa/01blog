import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ReportTarget } from '../../models/report-target.enum';

export interface ReportDialogData {
  targetType: ReportTarget;
  targetId: string;
  subjectLabel: string;
}

@Component({
  selector: 'app-report-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './report-dialog.component.html',
  styleUrl: './report-dialog.component.css',
})
export class ReportDialogComponent {

  reason = '';
  description = '';

  constructor(
    private dialogRef: MatDialogRef<ReportDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ReportDialogData
  ) {}

  cancel() {
    this.dialogRef.close();
  }

  submit() {
    this.dialogRef.close({
      targetType: this.data.targetType,
      targetId: this.data.targetId,
      reason: this.reason,
      description: this.description,
    });
  }
}
