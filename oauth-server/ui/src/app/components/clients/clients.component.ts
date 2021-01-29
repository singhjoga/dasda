import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ConfirmationService, DialogService, MenuItem } from 'primeng/api';
import { MessageService } from 'primeng/components/common/messageservice';
import { BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Client } from 'src/app/models/client.model';
import { ClientsService } from 'src/app/services/clients.service';
import { DIALOIG_WIDTH, TABLE_ITEMS_PER_PAGE } from '../../shared/consts';
import { PasswordDialogComponent } from '../dialogs/password-dialog/password-dialog.component';

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.sass'],
  providers: [DialogService],
})
export class ClientsComponent implements OnInit {

  constructor(
    private formBuilder: FormBuilder,
    private messageService: MessageService,
    private clientService: ClientsService,
    private confirmationService: ConfirmationService,
    private dialogService: DialogService,
  ) { }

  WIDTH = DIALOIG_WIDTH;
  ITEMS_PER_PAGE = TABLE_ITEMS_PER_PAGE;

  isNewClient = true;

  items: MenuItem[];

  readonly columns = [
    { field: 'clientId', header: 'Client Id' },
    { field: 'scopes', header: 'Scope' },
    { field: 'grantTypes', header: 'Grants' },
    { field: 'roles', header: 'Roles' },
    { field: 'accessTokenValidityMs', header: 'Access Token Validity' },
    { field: 'refreshTokenValidityMs', header: 'Refresh Token Validity' },
  ];

  readonly FORM_ACCESS_VALIDITY = 'accessTokenValidityMs';
  readonly FORM_CLIENT_ID = 'clientId';
  readonly FORM_GRANT_TYPES = 'grantTypes';
  readonly FORM_ID = 'id';
  readonly FORM_REFRESH_VALIDITY = 'refreshTokenValidityMs';
  readonly FORM_ROLES = 'roles';
  readonly FORM_SCOPES = 'scopes';
  readonly FORM_SECRET = 'secret';

  readonly clientForm = this.formBuilder.group({
    [this.FORM_ACCESS_VALIDITY]: ['', [Validators.required]],
    [this.FORM_CLIENT_ID]: [{ value: '', disabled: !this.isNewClient }, [Validators.required]],
    [this.FORM_GRANT_TYPES]: ['', [Validators.required]],
    [this.FORM_ID]: [''],
    [this.FORM_REFRESH_VALIDITY]: ['', [Validators.required]],
    [this.FORM_ROLES]: ['', [Validators.required]],
    [this.FORM_SCOPES]: ['', [Validators.required]],
    [this.FORM_SECRET]: [{ value: '', disabled: !this.isNewClient }, [Validators.required]],
  });

  busy$ = new BehaviorSubject(false);
  clientsData$ = new BehaviorSubject([]);

  showDialog = false;

  ngOnInit() {
    this.busy$.next(true);
    this.loadData();
  }

  loadData() {
    this.clientService.getAll().subscribe((_: Client[]) => {
      this.clientsData$.next(_);
      this.busy$.next(false);
    });
  }

  onRowEdit(client: Client) {
    this.isNewClient = false;
    this.clientForm.setValue(client);
    this.showDialog = true;
  }

  save() {
    this.confirmationService.confirm({
      message: 'Are you sure to save this client?',
      accept: () => {
        this.clientForm.controls[this.FORM_ID].setValue(this.clientForm.controls[this.FORM_CLIENT_ID].value);
        if (this.isNewClient) {
          this.clientService.create(this.clientForm.value)
            .pipe(
              (tap((ii) => {
                this.showDialog = false;
                this.busy$.next(true);
                this.loadData();
                this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Client added successfully' });
              }
              ))
            )
            .toPromise();
        } else {
          const client: Client = this.clientForm.value;
          delete client.secret;
          this.clientService.update(client)
            .pipe(
              (tap((ii) => {
                this.showDialog = false;
                this.busy$.next(true);
                this.loadData();
                this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Client updated successfully' });
              }
              ))
            )
            .toPromise();
        }
      }
    });
  }

  onRowDelete(client: Client) {
    this.confirmationService.confirm({
      message: 'Are you sure to remove this client?',
      accept: () => {
        this.clientService.delete(client.clientId)
          .pipe(
            (tap((ii) => {
              this.showDialog = false;
              this.busy$.next(true);
              this.loadData();
              this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Client removed successfully' });
            }
            ))
          )
          .toPromise();
      }
    });
  }

  showDialogToAdd() {
    this.isNewClient = true;
    this.showDialog = true;
    this.clientForm.reset();
  }

  onDialogHide() {
    this.isNewClient = true;
  }

  onClientsActionsMenu(client: Client) {
    this.items = [
      {
        label: 'Modify',
        icon: 'pi pi-fw pi-pencil',
        styleClass: 'ui-button-info',
        command: () => this.onRowEdit(client)
      },
      {
        label: 'Delete',
        icon: 'pi pi-fw pi-trash',
        styleClass: 'ui-button-danger',
        command: () => this.onRowDelete(client)
      },
      {
        label: 'Secret',
        icon: 'pi pi-fw pi-key',
        styleClass: 'ui-button-info',
        command: () => this.onChangePassword(client)
      }
    ];
  }

  onChangePassword(client: Client) {
    const ref = this.dialogService.open(PasswordDialogComponent, {
      data: {
        password: client.secret
      },
      header: 'Update secret',
      width: DIALOIG_WIDTH
    });

    ref.onClose.subscribe((secret: string) => {
      if (secret) {
        this.clientService.updateSecret(client.id, secret)
          .pipe(
            (tap(() => {
              this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Secret updated successfully' });
            }
            ))
          )
          .toPromise();
      }
    });
  }
}
