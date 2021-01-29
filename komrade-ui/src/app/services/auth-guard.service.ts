import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { navigateToLogin } from '../routing';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService {

  constructor(public auth: AuthService, public router: Router) { }
  canActivate(): boolean {
    if (!this.auth.isAuthenticated()) {
      navigateToLogin(this.router);
      return false;
    }
    return true;
  }
}
