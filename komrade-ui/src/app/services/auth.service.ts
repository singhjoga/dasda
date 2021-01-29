import { Injectable } from '@angular/core';
import { HttpClientService } from './http-client.service';
import { API_URL } from '../constants';
import { LoginUser, EntityPermission } from '../models/login-user.model';
import { BehaviorSubject } from 'rxjs';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private userAuth$ = new BehaviorSubject(null as LoginUser);

  constructor(
    private api: HttpClientService
  ) {
    this.userAuth$.next(JSON.parse(localStorage.getItem('auth')));
  }

  public isAuthenticated(): boolean {
    return this.userAuth$.getValue() !== null;
  }

  public login$ = (userName: string, password: string) => {
    return this.api.post<LoginUser>(`${API_URL}/login?userName=${userName}&password=${password}`, null);
  }

  public getPermissions$ = () => {
    const OAUTH_HEADERS: HttpHeaders = new HttpHeaders({
      authorization: `Bearer  ${this.userAuth$.getValue().accessToken}`,
      'content-type': 'application/json'
    });

    return this.api.get(`${API_URL}/permissions`, OAUTH_HEADERS);
  }

  public setUserAuth(auth: LoginUser) {
    auth.accessToken = auth['access_token'];
    auth.expiresIn = auth['expires_in'];
    auth.refreshToken = auth['refresh_token'];
    this.userAuth$.next(auth);
    localStorage.setItem('auth', JSON.stringify(auth));
  }

  public getUserAuth(): LoginUser {
    return this.userAuth$.getValue();
  }

}
