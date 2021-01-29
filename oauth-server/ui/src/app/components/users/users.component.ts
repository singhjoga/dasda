import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MessageService, ConfirmationService, DialogService, MenuItem } from 'primeng/api';
import { UsersService } from 'src/app/services/users.service';
import { DIALOIG_WIDTH, TABLE_ITEMS_PER_PAGE, DIALOIG_HEIGHT, DIALOG_TABLE_ITEMS_PER_PAGE } from '../../shared/consts';
import { BehaviorSubject, Subject } from 'rxjs';
import { User } from 'src/app/models/user.model';
import { tap } from 'rxjs/operators';
import { Group } from 'src/app/models/group.model';
import { Dialog } from 'primeng/dialog';
import { GroupService } from 'src/app/services/group.service';
import { SelectGroupsDialogComponent } from '../dialogs/select-groups-dialog/select-groups-dialog.component';
import { PasswordDialogComponent } from '../dialogs/password-dialog/password-dialog.component';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.sass'],
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
export class UsersComponent implements OnInit {

  constructor(
    private formBuilder: FormBuilder,
    private messageService: MessageService,
    private userService: UsersService,
    private groupService: GroupService,
    private confirmationService: ConfirmationService,
    private dialogService: DialogService,
  ) { }

  @ViewChild(Dialog) dialog;

  WIDTH = DIALOIG_WIDTH;
  HEIGHT = DIALOIG_HEIGHT;
  ITEMS_PER_PAGE = TABLE_ITEMS_PER_PAGE;
  DIALOG_ITEMS_PER_PAGE = DIALOG_TABLE_ITEMS_PER_PAGE;

  isNewUser = true;

  items: MenuItem[];

  readonly columns = [
    { field: 'name', header: 'Name' },
    { field: 'isSystem', header: 'System?' },
    { field: 'isDisabled', header: 'Disabled?' },
  ];

  readonly FORM_ID = 'id';
  readonly FORM_NAME = 'name';
  readonly FORM_PASSWORD = 'password';
  readonly FORM_DISABLED = 'isDisabled';
  readonly FORM_IS_SYSTEM = 'isSystem';

  readonly userForm = this.formBuilder.group({
    [this.FORM_ID]: [''],
    [this.FORM_NAME]: ['', [Validators.required]],
    [this.FORM_PASSWORD]: ['', [Validators.required]],
    [this.FORM_DISABLED]: [false],
    [this.FORM_IS_SYSTEM]: [false],
  });

  busy$ = new BehaviorSubject(false);
  usersData$ = new BehaviorSubject([]);

  groupsData$ = new BehaviorSubject([]);
  groups$ = new BehaviorSubject([]);

  showDialog = false;
  private readonly done$ = new Subject();

  selectedGroup: Group;

  ngOnInit() {
    this.busy$.next(true);
    this.loadData();
  }

  loadData() {
    this.userService.getAll().subscribe((_: User[]) => {
      this.usersData$.next(_);
      this.busy$.next(false);
    });
    this.groupService.getAll().subscribe((_: Group[]) => this.groups$.next(_));
  }

  loadGroups(userId: number) {
    this.userService.getGroups(userId).subscribe((_: Group[]) => {
      this.groupsData$.next(_);
      console.log(this.groupsData$.value)
      this.busy$.next(false);
    });
  }

  onRowEdit(user: User) {
    this.loadGroups(user.id);
    this.isNewUser = false;
    this.userForm.setValue({
      id: user.id,
      name: user.name,
      password: user.password,
      isSystem: user.isSystem,
      isDisabled: user.isDisabled
    });
    this.showDialog = true;
    window.setTimeout(() => {
      this.dialog.center();
    });
  }

  save() {
    const disableControl = this.userForm.controls[this.FORM_DISABLED];
    disableControl.setValue(disableControl.value ? disableControl.value : false);

    this.userForm.controls[this.FORM_IS_SYSTEM].setValue(false);

    this.confirmationService.confirm({
      message: 'Are you sure to save this user?',
      accept: () => {
        if (this.isNewUser) {
          this.userService.create({
            name: this.userForm.controls[this.FORM_NAME].value,
            password: this.userForm.controls[this.FORM_PASSWORD].value,
            isDisabled: this.userForm.controls[this.FORM_DISABLED].value,
            isSystem: this.userForm.controls[this.FORM_IS_SYSTEM].value,
          })
            .pipe(
              (tap((ii) => {
                this.showDialog = false;
                this.busy$.next(true);
                this.loadData();
                this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'User added successfully' });
              }
              ))
            )
            .toPromise();
        } else {
          this.userService.update({
            id: this.userForm.controls[this.FORM_ID].value,
            name: this.userForm.controls[this.FORM_NAME].value,
            isDisabled: this.userForm.controls[this.FORM_DISABLED].value,
            isSystem: this.userForm.controls[this.FORM_IS_SYSTEM].value,
          })
            .pipe(
              (tap((ii) => {
                this.showDialog = false;
                this.busy$.next(true);
                this.loadData();
                this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'User updated successfully' });
              }
              ))
            )
            .toPromise();
        }
      }
    });
  }

  onRowDelete(user: User) {
    this.confirmationService.confirm({
      message: 'Are you sure to remove this user?',
      accept: () => {
        this.userService.delete(user.id.toString())
          .pipe(
            (tap((ii) => {
              this.showDialog = false;
              this.busy$.next(true);
              this.loadData();
              this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'User removed successfully' });
            }
            ))
          )
          .toPromise();
      }
    });
  }

  showDialogToAdd() {
    this.isNewUser = true;
    this.showDialog = true;
    this.userForm.reset();
  }

  onGroupSelect(group: Group) {
    this.confirmationService.confirm({
      message: 'Are you sure to add this group?',
      accept: () => {
        this.userService.attachGroup(this.userForm.controls[this.FORM_ID].value, [group.id])
          .pipe(
            (tap((ii) => {
              this.loadGroups(this.userForm.controls[this.FORM_ID].value);
            }
            ))
          )
          .toPromise();
      }
    });
  }

  onDetachGroup(group: Group) {
    this.confirmationService.confirm({
      message: 'Are you sure to remove this group?',
      accept: () => {
        this.userService.detachGroup(this.userForm.controls[this.FORM_ID].value, [group.id])
          .pipe(
            (tap((ii) => {
              this.loadGroups(this.userForm.controls[this.FORM_ID].value);
            }
            ))
          )
          .toPromise();
      }
    });
  }

  filterGroups(event) {
    const query = event.query;
    this.groupService.getAll().subscribe((groups: Group[]) => {
      this.groups$.next(groups.filter(_ => _.name.toLowerCase().includes(query.toLowerCase())));
    });
  }

  onDialogHide() {
    this.isNewUser = true;
  }

  showAddGroupDialog() {
    const ref = this.dialogService.open(SelectGroupsDialogComponent, {
      data: {
        selectedGroups: this.groupsData$.value
      },
      header: 'Select Groups',
      width: DIALOIG_WIDTH
    });

    ref.onClose.subscribe((groups: Group[]) => {
      if (groups) {
        const groupIds = [];
        groups.forEach(element => {
          groupIds.push(element.id);
        });

        this.userService.attachGroup(this.userForm.controls[this.FORM_ID].value, groupIds)
          .pipe(
            (tap(() => {
              this.loadGroups(this.userForm.controls[this.FORM_ID].value);
              this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Groups added successfully' });
            }
            ))
          )
          .toPromise();
      }
    });
  }

  onChangePassword(user: User) {
    const ref = this.dialogService.open(PasswordDialogComponent, {
      data: {
        password: user.password
      },
      header: 'Update password',
      width: DIALOIG_WIDTH
    });

    ref.onClose.subscribe((password: string) => {
      if (password) {
        this.userService.updatePassword(user.name, password)
          .pipe(
            (tap(() => {
              this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Password updated successfully' });
            }
            ))
          )
          .toPromise();
      }
    });
  }

  onUsersActionsMenu(user: User) {
    this.items = [
      {
        label: 'Modify',
        icon: 'pi pi-fw pi-pencil',
        styleClass: 'ui-button-info',
        disabled: user.isSystem,
        command: () => this.onRowEdit(user)
      },
      {
        label: 'Delete',
        icon: 'pi pi-fw pi-trash',
        styleClass: 'ui-button-danger',
        disabled: user.isSystem,
        command: () => this.onRowDelete(user)
      },
      {
        label: 'Password',
        icon: 'pi pi-fw pi-key',
        styleClass: 'ui-button-info',
        disabled: user.isSystem,
        command: () => this.onChangePassword(user)
      }
    ];
  }

  onGroupsActionsMenu(group: Group) {
    this.items = [
      {
        label: 'Delete',
        icon: 'pi pi-fw pi-trash',
        styleClass: 'ui-button-danger',
        command: () => this.onDetachGroup(group)
      }
    ];
  }
}
