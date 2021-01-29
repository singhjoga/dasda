import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { ErrorDetail } from '../models/app-errors-warnings.model';
import { ErrorHandlerService } from './error-handler.service';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {
  constructor(
    private httpClient: HttpClient,
    private eh: ErrorHandlerService,
  ) { }


  public post = <T>(url: string, body: any, headers?: HttpHeaders): any => {
    return this.httpClient.post(url, body, { headers })
      .pipe(
        catchError((error: ErrorDetail) => {
          return this.eh.handleError(error);
        }),
        map((_: T) => _)
      );
  }

  public get = <T>(url: string, headers?: HttpHeaders): Observable<T> => {
    return this.httpClient.get<T>(url, { headers })
      .pipe(
        catchError((error: ErrorDetail) => {
          return this.eh.handleError(error);
        }),
        map((_: T) => _)
      );
  }

  public put = <T>(url: string, body: any, headers?: HttpHeaders): any => {
    return this.httpClient.post<T>(url, body, { headers })
      .pipe(
        catchError((error: ErrorDetail) => {
          return this.eh.handleError(error);
        }),
        map((_: T) => _)
      );
  }

  public patch = <T>(url: string, body: any, headers?: HttpHeaders): any => {
    return this.httpClient.patch<T>(url, body, { headers })
      .pipe(
        catchError((error: ErrorDetail) => {
          return this.eh.handleError(error);
        }),
        map((_: T) => _)
      );
  }

  public delete = (url, headers?: HttpHeaders): Observable<any> => {
    return this.httpClient.delete(url, { headers })
      .pipe(
        catchError((error: ErrorDetail) => {
          return this.eh.handleError(error);
        }),
        map((_: any) => _)
      );
  }

  public postExport = <T>(url: string, body: any, fileName?: string, headers?: HttpHeaders): any => {
    return this.httpClient.post(url, body, { headers, responseType: 'text' })
      .pipe(
        catchError((error: ErrorDetail) => {
          return this.eh.handleError(error);
        }),
        map((_) => this.downloadFile(_, fileName))
      );
  }

  downloadFile(data, fileName: string) {
    const blob = new Blob([data], { type: 'text/csv' });
    const a = document.createElement('a');
    a.href = URL.createObjectURL(blob);
    a.download = `${fileName}.csv`;
    a.click();
  }
}
