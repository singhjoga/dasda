import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginService } from './login.service';

@Injectable()
export class Interceptor implements HttpInterceptor {
  constructor(private loginService: LoginService) { }
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (
      request.url.indexOf('api/login') === -1 &&
      this.loginService.isAuthenticated()
    ) {
      request = request.clone({
        setHeaders: {
          Authorization: 'Bearer ' + this.loginService.currentUserValue.access_token
        }
      });
    } else {
      this.loginService.logout();
    }

    return next.handle(request);
  }
}
