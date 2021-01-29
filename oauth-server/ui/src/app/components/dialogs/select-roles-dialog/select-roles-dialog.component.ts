import { Component, OnInit } from '@angular/core';
import { RoleService } from 'src/app/services/role.service';
import { DynamicDialogRef, DynamicDialogConfig, ConfirmationService } from 'primeng/api';
import { Role } from 'src/app/models/role.model';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-select-roles-dialog',
  templateUrl: './select-roles-dialog.component.html',
  styleUrls: ['./select-roles-dialog.component.sass']
})
export class SelectRolesDialogComponent implements OnInit {

  constructor(
    private roleService: RoleService,
    public ref: DynamicDialogRef,
    public config: DynamicDialogConfig,
    private confirmationService: ConfirmationService,
  ) { }

  oldSelectedRoles: Role[];
  newSelectedRoles: Role[] = [];
  roles$ = new BehaviorSubject([]);

  ngOnInit() {
    this.oldSelectedRoles = this.config.data.selectedRoles ? this.config.data.selectedRoles : [];

    const selectedIds = [];
    this.oldSelectedRoles.forEach(element => {
      selectedIds.push(element.id);
    });

    this.roleService.getAll()
      .subscribe((_: Role[]) => {
        this.roles$.next(_.filter(ii => !selectedIds.includes(ii.id)));
      });
  }

  selectRole(role: Role) {
    this.ref.close(role);
  }

  onChange(event, role) {
    if (event) {
      this.newSelectedRoles.push(role);
    } else {
      const index = this.newSelectedRoles.indexOf(role);
      this.newSelectedRoles.splice(index, 1);
    }
  }

  addSelected() {
    this.confirmationService.confirm({
      message: 'Are you sure to remove these roles?',
      accept: () => {
        this.ref.close(this.newSelectedRoles);
      }
    });
  }
}
