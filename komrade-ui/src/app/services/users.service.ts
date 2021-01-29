import { Injectable } from '@angular/core';
import { HttpClientService } from './http-client.service';
import { ErrorDetail, WarningDetail } from '../models/app-errors-warnings.model';
import { User } from '../models/user.model';
import { API_URL } from '../constants';
import { HttpHeaders } from '@angular/common/http';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  readonly URL = API_URL + '/api/v1/users';
  readonly OAUTH_HEADERS: HttpHeaders = new HttpHeaders({
    authorization: `Bearer  ${this.authService.getUserAuth().accessToken}`,
    'content-type': 'application/json'
  });

  constructor(
    private api: HttpClientService,
    private authService: AuthService,
  ) { }

  public getUsers$ = () => {
    return this.api.get<UserDataModel>(`${this.URL}`, this.OAUTH_HEADERS);
  }

  public createUser$ = (user: User) => {
    return this.api.post<UserDataModel>(`${this.URL}`, user, this.OAUTH_HEADERS);
  }

  public updateUser$ = (user: User) => {
    return this.api.patch<UserDataModel>(`${this.URL}/${user.name}`, user, this.OAUTH_HEADERS);
  }

  public deleteUser$ = (id: string) => {
    return this.api.delete(`${this.URL}`, this.OAUTH_HEADERS);
  }
}

export class UserDataModel {
  errorDetail: ErrorDetail;
  result: User[];
  warningDetail: WarningDetail;
}
