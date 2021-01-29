import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Token } from '../models/token.model';
import { routeToLogin } from '../routing';
import { SERVER_URL } from '../shared/consts';
import { SubResourceService } from './sub-resource.service';
import { HttpClientService } from './http-client.service';


@Injectable({
  providedIn: 'root'
})
export class LoginService extends SubResourceService<any> {

  constructor(
    httpBase: HttpClientService,
    private router: Router
  ) {
    super(
      httpBase
    );
    this.url = `${SERVER_URL}login`;

    this.currentUserSubject = new BehaviorSubject<Token>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();

    if (this.currentUserSubject.value) {

      if (this.getExpiryTime() < 1) {
        this.logout();
        routeToLogin(this.router);
      }
    }
  }

  public currentUser: Observable<Token>;
  private currentUserSubject = new BehaviorSubject<Token>({});

  public isLoggedIn$ = new BehaviorSubject(false);

  public get currentUserValue(): Token {
    return this.currentUserSubject.value;
  }

  login(uName: string, pwd: string) {

    const body = new HttpParams()
      .set('username', uName)
      .set('password', pwd)
      .set('grant_type', 'password')
      .set('scope', 'read');

    return this.createWithUrl<any>(null, `${this.url}?password=${pwd}&userName=${uName}`)
      .pipe(map((user: Token) => {
        // login successful if there's a jwt token in the response
        if (user) {
          const d = new Date();
          d.setSeconds(d.getSeconds() + user.expires_in);
          user.expiresAt = d;
          // store user details and jwt token in local storage to keep user logged in between page refreshes
          localStorage.setItem('currentUser', JSON.stringify(user));
          this.currentUserSubject.next(user);
          this.isLoggedIn$.next(true);
        }

        return user;
      }));
  }

  isAuthenticated() {
    return (this.currentUserSubject.value ? true : false) && (this.getExpiryTime() > 0);
  }

  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    routeToLogin(this.router);
  }

  getExpiryTime() {
    const startDate = new Date();
    const endDate = new Date(this.currentUserSubject.value.expiresAt);
    const diff = Math.ceil((endDate.getTime() - startDate.getTime()) / 1000);
    return diff;
  }
}
