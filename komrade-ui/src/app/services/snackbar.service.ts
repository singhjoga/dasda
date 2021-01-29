import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material';

@Injectable()
export class SnackbarService {

    constructor(private snackBar: MatSnackBar) { }

    openSnackBar(message: string, action: string, snackbarType: string, duration = 5000) {
        this.snackBar.open(message, action, {
            duration,
            verticalPosition: 'top',
            panelClass: [snackbarType]
        });
    }
}
