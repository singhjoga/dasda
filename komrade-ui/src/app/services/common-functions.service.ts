import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { EntityPermission } from '../models/login-user.model';

@Injectable({
  providedIn: 'root'
})
export class CommonFunctionsService {

  constructor(private authService: AuthService) { }

  isPermitted(entityName: string, actionName: string, constraint: string) {
    if (!entityName || !actionName) {
      return false;
    }

    const permissions: EntityPermission[] = this.authService.getUserAuth().permissions;
    if (!permissions) {
      return false;
    }

    const entity = permissions.find((_: EntityPermission) =>
      _.entityName === entityName
    );

    if (!entity) {
      return false;
    }

    const action = entity.actions.find(_ => _.name === actionName);
    if (!action) {
      return false;
    }
    // TODO: Contraints later
    return true;
  }
}
