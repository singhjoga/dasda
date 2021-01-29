import { Injectable } from '@angular/core';
import { Group } from '../models/group.model';
import { User } from '../models/user.model';
import { SERVER_URL } from '../shared/consts';
import { HttpClientService } from './http-client.service';
import { SubResourceService } from './sub-resource.service';


@Injectable({
  providedIn: 'root'
})
export class UsersService extends SubResourceService<User> {
  constructor(
    httpBase: HttpClientService
  ) {
    super(
      httpBase
    );
    this.url = `${SERVER_URL}api/v1/users`;
  }

  getGroups = (userId: number) =>
    this.list<Group>(`${this.url}/${userId}/groups`)

  attachGroup = (userId: number, groupIds: number[]) =>
    this.updateWithUrl(groupIds, `${this.url}/${userId}/attachtogroups`)

  detachGroup = (userId: number, groupIds: number[]) =>
    this.updateWithUrl(groupIds, `${this.url}/${userId}/detachfromgroups`)

  updatePassword = (userId: string, password: string) =>
    this.updateWithUrl({ password }, `${this.url}/${userId}/updatepassword`)
}
