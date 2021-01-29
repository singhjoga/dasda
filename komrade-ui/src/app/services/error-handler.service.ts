import { Injectable } from '@angular/core';
import { ErrorDetail } from '../models/app-errors-warnings.model';
import { SnackbarService } from './snackbar.service';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  constructor(
    private sb: SnackbarService
  ) { }

  handleError(err) {
    console.log(err.error);
    if (err.error.error_description) {
      console.log(err.status);
      this.sb.openSnackBar(err.error.error_description, '', 'error-snackbar');
      return throwError(err.error.error_description);
    }

    const error = err.error.errorDetail;

    switch (error.code) {
      case 99999: {
        // Internal Error
        console.log(error);
        this.sb.openSnackBar(error.message, '', 'error-snackbar');
        return throwError(error);
        break;
      }
      case 10001: {
        // Validation Error
        console.log(error);
        this.sb.openSnackBar(error.message, '', 'error-snackbar');
        return throwError(error);
        break;
      }
      case 10002: {
        // Resource Not Found Error
        console.log(error);
        this.sb.openSnackBar(error.message, '', 'error-snackbar');
        return throwError(error);
        break;
      }
      case 10003: {
        // Access Denied Error
        console.log(error);
        this.sb.openSnackBar(error.message, '', 'error-snackbar');
        return throwError(error);
        break;
      }
      default: {
        // Default Error
        console.log(error);
        this.sb.openSnackBar(error.message, '', 'error-snackbar');
        return throwError(error);
        break;
      }
    }
  }
}
