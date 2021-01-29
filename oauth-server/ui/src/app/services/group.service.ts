import { Injectable } from '@angular/core';
import { Group } from '../models/group.model';
import { Role } from '../models/role.model';
import { User } from '../models/user.model';
import { SERVER_URL } from '../shared/consts';
import { SubResourceService } from './sub-resource.service';
import { HttpClientService } from './http-client.service';

@Injectable({
  providedIn: 'root'
})
export class GroupService extends SubResourceService<Group> {


  constructor(
    httpBase: HttpClientService) {
    super(
      httpBase
    );
    this.url = `${SERVER_URL}api/v1/groups`;
  }

  getRoles = (groupId: number) =>
    this.list<Role>(`${this.url}/${groupId}/roles`)

  attachRole = (groupId: number, roleIds: number[]) =>
    this.updateWithUrl(roleIds, `${this.url}/${groupId}/attachroles`)

  detachRole = (groupId: number, roleIds: number[]) =>
    this.updateWithUrl(roleIds, `${this.url}/${groupId}/detachroles`)

  getUsers = (groupId: number) =>
    this.list<User>(`${this.url}/${groupId}/users`)

  attachUser = (groupId: number, userIds: number[]) =>
    this.updateWithUrl(userIds, `${this.url}/${groupId}/attachusers`)

  detachUser = (groupId: number, userIds: number[]) =>
    this.updateWithUrl(userIds, `${this.url}/${groupId}/detachusers`)
}
