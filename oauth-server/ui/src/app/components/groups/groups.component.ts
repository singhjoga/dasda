import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MessageService, ConfirmationService, DialogService, MenuItem } from 'primeng/api';
import { GroupService } from 'src/app/services/group.service';
import { DIALOIG_WIDTH, DIALOIG_HEIGHT, TABLE_ITEMS_PER_PAGE, DIALOG_TABLE_ITEMS_PER_PAGE } from '../../shared/consts';
import { BehaviorSubject, Subject } from 'rxjs';
import { Group } from 'src/app/models/group.model';
import { tap } from 'rxjs/operators';
import { Role } from 'src/app/models/role.model';
import { RoleService } from 'src/app/services/role.service';
import { Dialog } from 'primeng/dialog';
import { UsersService } from 'src/app/services/users.service';
import { User } from 'src/app/models/user.model';
import { SelectRolesDialogComponent } from '../dialogs/select-roles-dialog/select-roles-dialog.component';
import { SelectUsersDialogComponent } from '../dialogs/select-users-dialog/select-users-dialog.component';


@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.sass'],
  providers: [DialogService],
  styles: [`
        :host ::ng-deep .ui-table .ui-table-thead > tr > th {
            position: -webkit-sticky;
            position: sticky;
            top: 70px;
        }

        @media screen and (max-width: 64em) {
            :host ::ng-deep .ui-table .ui-table-thead > tr > th {
                top: 100px;
            }
        }
`]
})

export class GroupsComponent implements OnInit {

  constructor(
    private formBuilder: FormBuilder,
    private messageService: MessageService,
    private groupService: GroupService,
    private roleService: RoleService,
    private usersService: UsersService,
    private confirmationService: ConfirmationService,
    public dialogService: DialogService
  ) { }

  @ViewChild(Dialog) dialog;

  WIDTH = DIALOIG_WIDTH;
  HEIGHT = DIALOIG_HEIGHT;
  ITEMS_PER_PAGE = TABLE_ITEMS_PER_PAGE;
  DIALOG_ITEMS_PER_PAGE = DIALOG_TABLE_ITEMS_PER_PAGE;

  isNewGroup = true;
  isNewRole = true;
  showDialog = false;

  items: MenuItem[];

  readonly columns = [
    { field: 'name', header: 'Name' },
    { field: 'description', header: 'Description' },
    { field: 'isSystem', header: 'System?' },
    { field: 'isDisabled', header: 'Disabled?' },
  ];

  private readonly done$ = new Subject();
  readonly FORM_ID = 'id';
  readonly FORM_NAME = 'name';
  readonly FORM_DESCRIPTION = 'description';
  readonly FORM_DISABLED = 'isDisabled';
  readonly FORM_IS_SYSTEM = 'isSystem';

  readonly groupForm = this.formBuilder.group({
    [this.FORM_ID]: [''],
    [this.FORM_NAME]: ['', [Validators.required]],
    [this.FORM_DESCRIPTION]: ['', [Validators.required]],
    [this.FORM_DISABLED]: [false],
    [this.FORM_IS_SYSTEM]: [false],
  });

  busy$ = new BehaviorSubject(false);
  groupsData$ = new BehaviorSubject([]);

  rolesData$ = new BehaviorSubject([]);
  roles$ = new BehaviorSubject([]);

  usersData$ = new BehaviorSubject([]);
  users$ = new BehaviorSubject([]);

  ngOnInit() {
    this.busy$.next(true);
    this.loadData();
  }

  loadData() {
    this.groupService.getAll().subscribe((_: Group[]) => {
      this.groupsData$.next(_);
      this.busy$.next(false);
    });

    this.roleService.getAll().subscribe((_: Role[]) => this.roles$.next(_));
    this.usersService.getAll().subscribe((_: User[]) => this.users$.next(_));
  }

  loadRoles(groupId: number) {
    this.groupService.getRoles(groupId).subscribe((_: Role[]) => {
      this.rolesData$.next(_);
      this.busy$.next(false);
    });
  }

  loadUsers(groupId: number) {
    this.groupService.getUsers(groupId).subscribe((_: User[]) => {
      this.usersData$.next(_);
      this.busy$.next(false);
    });
  }

  onRowEdit(group: Group) {
    this.loadRoles(group.id);
    this.loadUsers(group.id);
    this.isNewGroup = false;
    this.groupForm.setValue({
      id: group.id,
      name: group.name,
      description: group.description,
      isDisabled: group.isDisabled,
      isSystem: group.isSystem
    });
    this.showDialog = true;
    window.setTimeout(() => {
      this.dialog.center();
    });

  }

  save() {
    const disableControl = this.groupForm.controls[this.FORM_DISABLED];
    disableControl.setValue(disableControl.value ? disableControl.value : false);

    this.groupForm.controls[this.FORM_IS_SYSTEM].setValue(false);

    this.confirmationService.confirm({
      message: 'Are you sure to save this group?',
      accept: () => {
        if (this.isNewGroup) {
          this.groupService.create(this.groupForm.value)
            .pipe(
              (tap((ii) => {
                this.showDialog = false;
                this.busy$.next(true);
                this.loadData();
                this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Group added successfully' });
              }
              ))
            )
            .toPromise();
        } else {
          this.groupService.update({
            groupId: this.groupForm.controls[this.FORM_ID].value,
            name: this.groupForm.controls[this.FORM_NAME].value,
            description: this.groupForm.controls[this.FORM_DESCRIPTION].value,
            isDisabled: this.groupForm.controls[this.FORM_DISABLED].value,
            isSystem: this.groupForm.controls[this.FORM_IS_SYSTEM].value
          })
            .pipe(
              (tap((ii) => {
                this.showDialog = false;
                this.busy$.next(true);
                this.loadData();
                this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Group updated successfully' });
              }
              ))
            )
            .toPromise();
        }
      }
    });
  }

  onRowDelete(group: Group) {
    this.confirmationService.confirm({
      message: 'Are you sure to remove this group?',
      accept: () => {
        this.groupService.delete(group.id.toString())
          .pipe(
            (tap((ii) => {
              this.showDialog = false;
              this.busy$.next(true);
              this.loadData();
              this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Group removed successfully' });
            }
            ))
          )
          .toPromise();
      }
    });
  }

  showDialogToAdd() {
    this.isNewRole = true;
    this.showDialog = true;
    window.setTimeout(() => { this.dialog.center(); });
    this.groupForm.reset();
  }

  onRoleSelect(role: Role) {
    this.confirmationService.confirm({
      message: 'Are you sure to add this role?',
      accept: () => {
        this.groupService.attachRole(this.groupForm.controls[this.FORM_ID].value, [role.id])
          .pipe(
            (tap((ii) => {
              this.loadRoles(this.groupForm.controls[this.FORM_ID].value);
            }
            ))
          )
          .toPromise();
      }
    });
  }

  onDetachRole(role: Role) {
    this.confirmationService.confirm({
      message: 'Are you sure to remove this role?',
      accept: () => {
        this.groupService.detachRole(this.groupForm.controls[this.FORM_ID].value, [role.id])
          .pipe(
            (tap((ii) => {
              this.loadRoles(this.groupForm.controls[this.FORM_ID].value);
            }
            ))
          )
          .toPromise();
      }
    });
  }

  filterRoles(event) {
    const query = event.query;
    this.roleService.getAll().subscribe((roles: Role[]) => {
      this.roles$.next(roles.filter(_ => _.name.toLowerCase().includes(query.toLowerCase())));
    });
  }

  onUserSelect(user: User) {
    this.confirmationService.confirm({
      message: 'Are you sure to add this user?',
      accept: () => {
        this.groupService.attachUser(this.groupForm.controls[this.FORM_ID].value, [user.id])
          .pipe(
            (tap((ii) => {
              this.loadUsers(this.groupForm.controls[this.FORM_ID].value);
            }
            ))
          )
          .toPromise();
      }
    });
  }

  onDetachUser(user: User) {
    this.confirmationService.confirm({
      message: 'Are you sure to remove this user?',
      accept: () => {
        this.groupService.detachUser(this.groupForm.controls[this.FORM_ID].value, [user.id])
          .pipe(
            (tap((ii) => {
              this.loadUsers(this.groupForm.controls[this.FORM_ID].value);
            }
            ))
          )
          .toPromise();
      }
    });
  }

  filterUsers(event) {
    const query = event.query;
    this.usersService.getAll().subscribe((users: User[]) => {
      this.users$.next(users.filter(_ => _.name.toLowerCase().includes(query.toLowerCase())));
    });
  }

  onDialogHide() {
    this.isNewGroup = true;
  }

  showAddRoleDialog() {
    const ref = this.dialogService.open(SelectRolesDialogComponent, {
      data: {
        selectedRoles: this.rolesData$.value
      },
      header: 'Select Roles',
      width: DIALOIG_WIDTH
    });

    ref.onClose.subscribe((roles: Role[]) => {
      if (roles) {
        const roleIds = [];
        roles.forEach(element => {
          roleIds.push(element.id);
        });

        this.groupService.attachRole(this.groupForm.controls[this.FORM_ID].value, roleIds)
          .pipe(
            (tap(() => {
              this.loadRoles(this.groupForm.controls[this.FORM_ID].value);
              this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Roles added successfully' });
            }
            ))
          )
          .toPromise();
      }
    });
  }

  showAddUserDialog() {
    const ref = this.dialogService.open(SelectUsersDialogComponent, {
      data: {
        selectedUsers: this.usersData$.value
      },
      header: 'Select Users',
      width: DIALOIG_WIDTH
    });

    ref.onClose.subscribe((users: User[]) => {
      if (users) {
        const userIds = [];
        users.forEach(element => {
          userIds.push(element.id);
        });

        this.groupService.attachUser(this.groupForm.controls[this.FORM_ID].value, userIds)
          .pipe(
            (tap(() => {
              this.loadUsers(this.groupForm.controls[this.FORM_ID].value);
              this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Users added successfully' });
            }
            ))
          )
          .toPromise();
      }
    });
  }

  onGroupsActionsMenu(group: Group) {
    this.items = [
      {
        label: 'Modify',
        icon: 'pi pi-fw pi-pencil',
        styleClass: 'ui-button-info',
        disabled: group.isSystem,
        command: () => this.onRowEdit(group)
      },
      {
        label: 'Delete',
        icon: 'pi pi-fw pi-trash',
        styleClass: 'ui-button-danger',
        disabled: group.isSystem,
        command: () => this.onRowDelete(group)
      }
    ];
  }

  onRolesActionsMenu(role: Role) {
    this.items = [
      {
        label: 'Delete',
        icon: 'pi pi-fw pi-trash',
        styleClass: 'ui-button-danger',
        command: () => this.onDetachRole(role)
      }
    ];
  }

  onUsersActionsMenu(user: User) {
    this.items = [
      {
        label: 'Delete',
        icon: 'pi pi-fw pi-trash',
        styleClass: 'ui-button-danger',
        command: () => this.onDetachUser(user)
      }
    ];
  }

}
