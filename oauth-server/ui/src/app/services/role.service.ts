import { Injectable } from '@angular/core';
import { Group } from '../models/group.model';
import { Role } from '../models/role.model';
import { SERVER_URL } from '../shared/consts';
import { HttpClientService } from './http-client.service';
import { SubResourceService } from './sub-resource.service';

@Injectable({
  providedIn: 'root'
})
export class RoleService extends SubResourceService<Role> {

  constructor(
    httpBase: HttpClientService
  ) {
    super(
      httpBase
    );
    this.url = `${SERVER_URL}api/v1/roles`;
  }

  getGroups = (roleId: number) =>
    this.list<Group>(`${this.url}/${roleId}/groups`)

  attachGroup = (roleId: number, groupIds: number[]) =>
    this.updateWithUrl(groupIds, `${this.url}/${roleId}/attachtogroups`)

  detachGroup = (roleId: number, groupIds: [number]) =>
    this.updateWithUrl(groupIds, `${this.url}/${roleId}/detachfromgroups`)
}
