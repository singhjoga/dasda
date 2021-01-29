import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MessageService } from 'primeng/api';
import { map, catchError } from 'rxjs/operators';
import { getHTTPErrorMessage, TOAST_LIFE } from '../shared/consts';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HttpClientService {

  constructor(
    private http: HttpClient,
    private messageService: MessageService
  ) { }

  public post<T>(item: T, url: string) {
    return this.http
      .post<T>(`${url}`, item)
      .pipe(map((data: any) => data as T),
        catchError((error) => {
          console.log(error);
          this.messageService.add({
            severity: 'error', summary: 'Error Message',
            detail: getHTTPErrorMessage(error), life: TOAST_LIFE
          });
          return [];
        })
      );
  }

  public patch<T>(item: T, url: string): Observable<T> {
    return this.http
      .patch<T>(`${url}`, item)
      .pipe(map((data: any) => data as T),
        catchError((error) => {
          console.log(error);
          this.messageService.add({
            severity: 'error', summary: 'Error Message',
            detail: getHTTPErrorMessage(error), life: TOAST_LIFE
          });
          return [];
        })
      );
  }

  get<T>(url: string): Observable<T[]> {
    return this.http
      .get(`${url}`)
      .pipe(map((data: any) => data.result as T[]),
        catchError((error) => {
          console.log(error);
          this.messageService.add({
            severity: 'error', summary: 'Error Message',
            detail: getHTTPErrorMessage(error), life: TOAST_LIFE
          });
          return [];
        })
      );
  }

  getById<T>(id: number, url: string): Observable<T> {
    return this.http
      .get(`${url}`)
      .pipe(map((data: any) => data as T),
        catchError((error) => {
          console.log(error);
          this.messageService.add({
            severity: 'error', summary: 'Error Message',
            detail: getHTTPErrorMessage(error), life: TOAST_LIFE
          });
          return [];
        })
      );
  }

  detele(url: string) {
    return this.http
      .delete(`${url}`)
      .pipe(
        map(ii => (ii || null) as string),
        catchError((error) => {
          console.log(error);
          this.messageService.add({
            severity: 'error', summary: 'Error Message',
            detail: getHTTPErrorMessage(error), life: TOAST_LIFE
          });
          return [];
        })
      );
  }
}
