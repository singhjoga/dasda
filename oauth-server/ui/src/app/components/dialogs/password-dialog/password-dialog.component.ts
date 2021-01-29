import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { DynamicDialogRef, DynamicDialogConfig, ConfirmationService } from 'primeng/api';

@Component({
  selector: 'app-password-dialog',
  templateUrl: './password-dialog.component.html',
  styleUrls: ['./password-dialog.component.sass']
})
export class PasswordDialogComponent implements OnInit {

  constructor(
    private formBuilder: FormBuilder,
    public ref: DynamicDialogRef,
    public config: DynamicDialogConfig,
    private confirmationService: ConfirmationService,
  ) { }

  readonly FORM_PASSWORD = 'password';

  readonly passwordForm = this.formBuilder.group({
    [this.FORM_PASSWORD]: [this.config.data.password ? this.config.data.password : '', [Validators.required]],
  });

  ngOnInit() {
    /*  this.passwordForm.controls[this.FORM_PASSWORD].setValue() */
  }

  save() {
    this.confirmationService.confirm({
      message: 'Are you sure to update the password?',
      accept: () => {
        this.ref.close(this.passwordForm.controls[this.FORM_PASSWORD].value);
      }
    });
  }

}
