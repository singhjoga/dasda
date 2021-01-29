import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { BehaviorSubject } from 'rxjs';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material';
import { User } from 'src/app/models/user.model';
import { UsersService, UserDataModel } from 'src/app/services/users.service';
import { SnackbarService } from 'src/app/services/snackbar.service';
import { TranslateService } from '@ngx-translate/core';
import { ConfirmationDialogComponent } from 'src/app/shared/components/confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-user-form-dialog',
  templateUrl: './user-form-dialog.component.html',
  styleUrls: ['./user-form-dialog.component.sass']
})
export class UserFormDialogComponent implements OnInit {
  public user$ = new BehaviorSubject({} as User);

  readonly EMAIL = 'email';
  readonly FIRST_NAME = 'firstname';
  readonly LAST_NAME = 'lastname';
  readonly USERNAME = 'username';
  readonly PASSWORD = 'password';

  public loginFormGroup: FormGroup;
  public isLoading$ = new BehaviorSubject<boolean>(false);

  public fg: FormGroup;

  constructor(
    private fb: FormBuilder,
    private service: UsersService,
    private sb: SnackbarService,
    private translation: TranslateService,
    private dialog: MatDialog,
    public dialogRef: MatDialogRef<UserFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: { user: User }
  ) {
    this.user$.next(data.user);
  }

  ngOnInit() {
    const user: User = this.data.user;

    this.fg = this.fb.group({
      [this.EMAIL]: [user && user.email ? user.email : '', [Validators.required]],
      [this.FIRST_NAME]: [user && user.firstName ? user.firstName : '', [Validators.required]],
      [this.LAST_NAME]: [user && user.lastName ? user.lastName : '', [Validators.required]],
      [this.USERNAME]: [user && user.name ? user.name : '', Validators.required],
      [this.PASSWORD]: [user && user.password ? user.password : '', Validators.required],
    });
  }

  async save() {
    this.isLoading$.next(true);

    const ok = !!(await this.dialog
      .open(ConfirmationDialogComponent,
        {
          data: this.data.user ? 'confirm-message-update' : 'confirm-message-create'
        })
      .afterClosed()
      .toPromise());

    const user = {
      email: this.fg.controls[this.EMAIL].value,
      firstName: this.fg.controls[this.FIRST_NAME].value,
      lastName: this.fg.controls[this.LAST_NAME].value,
      name: this.fg.controls[this.USERNAME].value,
    } as User;

    if (this.data.user) {
      this.service.updateUser$(user)
        .subscribe((_: UserDataModel) => {
          this.isLoading$.next(false);
          this.sb.openSnackBar(this.translation.instant('users.user-form-success'), '', 'success-snackbar');
          this.dialogRef.close();
        },
          (err) => { this.isLoading$.next(false); });
    } else {
      user.password = this.fg.controls[this.PASSWORD].value;

      this.service.createUser$(user)
        .subscribe((_: UserDataModel) => {
          this.isLoading$.next(false);
          this.sb.openSnackBar(this.translation.instant('users.user-form-success'), '', 'success-snackbar');
          this.dialogRef.close();
        },
          (err) => { this.isLoading$.next(false); });
    }
  }

}
