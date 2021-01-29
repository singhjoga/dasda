import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MessageService, ConfirmationService, DialogService, MenuItem } from 'primeng/api';
import { RoleService } from 'src/app/services/role.service';
import { DIALOIG_WIDTH, TABLE_ITEMS_PER_PAGE, DIALOIG_HEIGHT, DIALOG_TABLE_ITEMS_PER_PAGE } from '../../shared/consts';
import { BehaviorSubject, Subject } from 'rxjs';
import { Role } from 'src/app/models/role.model';
import { tap } from 'rxjs/operators';
import { Group } from 'src/app/models/group.model';
import { GroupService } from 'src/app/services/group.service';
import { Dialog } from 'primeng/dialog';
import { SelectGroupsDialogComponent } from '../dialogs/select-groups-dialog/select-groups-dialog.component';

@Component({
  selector: 'app-roles',
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.sass'],
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
export class RolesComponent implements OnInit {

  constructor(
    private formBuilder: FormBuilder,
    private messageService: MessageService,
    private roleService: RoleService,
    private groupService: GroupService,
    private confirmationService: ConfirmationService,
    private dialogService: DialogService,
  ) { }

  @ViewChild(Dialog) dialog;

  WIDTH = DIALOIG_WIDTH;
  HEIGHT = DIALOIG_HEIGHT;
  ITEMS_PER_PAGE = TABLE_ITEMS_PER_PAGE;
  DIALOG_ITEMS_PER_PAGE = DIALOG_TABLE_ITEMS_PER_PAGE;

  isNewRole = true;

  items: MenuItem[];

  readonly columns = [
    { field: 'name', header: 'Name' },
    { field: 'description', header: 'Description' },
    { field: 'isSystem', header: 'System?' },
    { field: 'isDisabled', header: 'Disabled?' },
  ];

  readonly FORM_ID = 'id';
  readonly FORM_NAME = 'name';
  readonly FORM_DESCRIPTION = 'description';
  readonly FORM_DISABLED = 'isDisabled';
  readonly FORM_IS_SYSTEM = 'isSystem';

  readonly roleForm = this.formBuilder.group({
    [this.FORM_ID]: [''],
    [this.FORM_NAME]: ['', [Validators.required]],
    [this.FORM_DESCRIPTION]: ['', [Validators.required]],
    [this.FORM_DISABLED]: [false],
    [this.FORM_IS_SYSTEM]: [false],
  });

  busy$ = new BehaviorSubject(false);
  rolesData$ = new BehaviorSubject([]);

  groupsData$ = new BehaviorSubject([]);
  groups$ = new BehaviorSubject([]);

  showDialog = false;
  private readonly done$ = new Subject();

  ngOnInit() {
    this.busy$.next(true);
    this.loadData();
  }

  loadData() {
    this.roleService.getAll().subscribe((_: Role[]) => {
      this.rolesData$.next(_);
      this.busy$.next(false);
    });

    this.groupService.getAll().subscribe((_: Group[]) => this.groups$.next(_));
  }

  loadGroups(roleId: number) {
    this.roleService.getGroups(roleId).subscribe((_: Group[]) => {
      this.groupsData$.next(_);
      this.busy$.next(false);
    });
  }

  onRowEdit(role: Role) {
    this.loadGroups(role.id);
    this.isNewRole = false;
    this.roleForm.setValue({
      id: role.id,
      name: role.name,
      description: role.description,
      isDisabled: role.isDisabled,
      isSystem: role.isSystem
    });
    this.showDialog = true;
    window.setTimeout(() => {
      this.dialog.center();
    });
  }

  save() {
    const disableControl = this.roleForm.controls[this.FORM_DISABLED];
    disableControl.setValue(disableControl.value ? disableControl.value : false);

    this.roleForm.controls[this.FORM_IS_SYSTEM].setValue(false);

    this.confirmationService.confirm({
      message: 'Are you sure to save this role?',
      accept: () => {
        if (this.isNewRole) {
          this.roleService.create(this.roleForm.value)
            .pipe(
              (tap((ii) => {
                this.showDialog = false;
                this.busy$.next(true);
                this.loadData();
                this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Role added successfully' });
              }
              ))
            )
            .toPromise();
        } else {
          this.roleService.update({
            id: this.roleForm.controls[this.FORM_ID].value,
            name: this.roleForm.controls[this.FORM_NAME].value,
            description: this.roleForm.controls[this.FORM_DESCRIPTION].value,
            isDisabled: this.roleForm.controls[this.FORM_DISABLED].value,
            isSystem: this.roleForm.controls[this.FORM_IS_SYSTEM].value,
          })
            .pipe(
              (tap((ii) => {
                this.showDialog = false;
                this.busy$.next(true);
                this.loadData();
                this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Role updated successfully' });
              }
              ))
            )
            .toPromise();
        }
      }
    });
  }

  onRowDelete(role: Role) {
    this.confirmationService.confirm({
      message: 'Are you sure to remove this role?',
      accept: () => {
        this.roleService.delete(role.id.toString())
          .pipe(
            (tap((ii) => {
              this.showDialog = false;
              this.busy$.next(true);
              this.loadData();
              this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Role removed successfully' });
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
    this.roleForm.reset();
  }

  onGroupSelect(group: Group) {
    this.confirmationService.confirm({
      message: 'Are you sure to add this group?',
      accept: () => {
        this.roleService.attachGroup(this.roleForm.controls[this.FORM_ID].value, [group.id])
          .pipe(
            (tap((ii) => {
              this.loadGroups(this.roleForm.controls[this.FORM_ID].value);
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
        this.roleService.detachGroup(this.roleForm.controls[this.FORM_ID].value, [group.id])
          .pipe(
            (tap((ii) => {
              this.loadGroups(this.roleForm.controls[this.FORM_ID].value);
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
    this.isNewRole = true;
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

        this.roleService.attachGroup(this.roleForm.controls[this.FORM_ID].value, groupIds)
          .pipe(
            (tap(() => {
              this.loadGroups(this.roleForm.controls[this.FORM_ID].value);
              this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Groups added successfully' });
            }
            ))
          )
          .toPromise();
      }
    });
  }

  onRolesActionsMenu(role: Role) {
    this.items = [
      {
        label: 'Modify',
        icon: 'pi pi-fw pi-pencil',
        styleClass: 'ui-button-info',
        disabled: role.isSystem,
        command: () => this.onRowEdit(role)
      },
      {
        label: 'Delete',
        icon: 'pi pi-fw pi-trash',
        styleClass: 'ui-button-danger',
        disabled: role.isSystem,
        command: () => this.onRowDelete(role)
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
