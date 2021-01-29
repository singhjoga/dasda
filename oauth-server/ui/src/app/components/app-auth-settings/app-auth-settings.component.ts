import { Component, OnInit } from '@angular/core';
import { DialogService, MenuItem, ConfirmationService, MessageService } from 'primeng/api';
import { BehaviorSubject } from 'rxjs';
import { AppAuthSetting } from 'src/app/models/app-auth-setting.model';
import { AppAuthSettingService } from 'src/app/services/app-auth-setting.service';
import { DIALOIG_WIDTH, TABLE_ITEMS_PER_PAGE } from '../../shared/consts';
import { AppAuthSettingsDialogComponent } from '../dialogs/app-auth-settings-dialog/app-auth-settings-dialog.component';
import { tap } from 'rxjs/operators';

@Component({
  selector: 'app-app-auth-settings',
  templateUrl: './app-auth-settings.component.html',
  styleUrls: ['./app-auth-settings.component.sass'],
  providers: [DialogService]
})
export class AppAuthSettingsComponent implements OnInit {

  constructor(
    private appAuthService: AppAuthSettingService,
    private dialogService: DialogService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
  ) { }

  WIDTH = DIALOIG_WIDTH;
  ITEMS_PER_PAGE = TABLE_ITEMS_PER_PAGE;

  items: MenuItem[];

  readonly columns = [
    { field: 'displayOrder', header: 'Display Order' },
    { field: 'name', header: 'Name' },
    { field: 'description', header: 'Description' },
    { field: 'providerType', header: 'Provider Type' },
    { field: 'isDisabled', header: 'Disabled?' },
    { field: 'clientId', header: 'Client Id' },
  ];

  busy$ = new BehaviorSubject(false);
  aapAuthSettingData$ = new BehaviorSubject([]);

  ngOnInit() {
    this.busy$.next(true);
    this.loadData();
  }

  loadData() {
    this.appAuthService.getAll().subscribe((_: AppAuthSetting[]) => {
      this.aapAuthSettingData$.next(_);
      this.busy$.next(false);
    });
  }

  openAddDialog() {
    const ref = this.dialogService.open(AppAuthSettingsDialogComponent, {
      data: { mode: 'create' },
      header: 'Create setting',
      width: DIALOIG_WIDTH
    });

    ref.onClose.subscribe((result) => {
      if (result) {
        this.loadData();
      }
    });
  }

  openUpdateDialog(setting: AppAuthSetting) {
    const ref = this.dialogService.open(AppAuthSettingsDialogComponent, {
      data: { mode: 'update', setting },
      header: 'Update setting',
      width: DIALOIG_WIDTH
    });

    ref.onClose.subscribe((result) => {
      if (result) {
        this.loadData();
      }
    });
  }

  onSettingsActionsMenu(setting: AppAuthSetting) {
    this.items = [
      {
        label: 'Modify',
        icon: 'pi pi-fw pi-pencil',
        styleClass: 'ui-button-info',
        command: () => this.openUpdateDialog(setting)
      },
      {
        label: 'Delete',
        icon: 'pi pi-fw pi-trash',
        styleClass: 'ui-button-danger',
        command: () => this.onRowDelete(setting)
      }
    ];
  }

  onRowDelete(setting: AppAuthSetting) {
    this.confirmationService.confirm({
      message: 'Are you sure to remove this setting?',
      accept: () => {
        this.appAuthService.delete(setting.id.toString())
          .pipe(
            (tap((ii) => {
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
}
