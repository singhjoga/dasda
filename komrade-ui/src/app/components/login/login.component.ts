import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';
import { LoginUser, EntityPermission } from 'src/app/models/login-user.model';
import { navigateToHome } from 'src/app/routing';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  readonly USERNAME = 'username';
  readonly PASSWORD = 'password';

  public loginFormGroup: FormGroup;
  public isLoading$ = new BehaviorSubject<boolean>(false);

  constructor(
    private fb: FormBuilder,
    private service: AuthService,
    private router: Router
  ) { }

  ngOnInit() {
    this.loginFormGroup = this.fb.group({
      [this.USERNAME]: ['', [Validators.required]],
      [this.PASSWORD]: ['', [Validators.required]],
    });
  }

  onSubmit() {
    this.isLoading$.next(true);
    this.service.login$(this.loginFormGroup.controls[this.USERNAME].value, this.loginFormGroup.controls[this.PASSWORD].value)
      .subscribe((_: LoginUser) => {
        this.service.setUserAuth(_);
        this.service.getPermissions$()
          .subscribe((__: {
            result: EntityPermission[]
          }) => {
            _.permissions = __.result;
            this.service.setUserAuth(_);
            this.isLoading$.next(false);
            navigateToHome(this.router);
          });

      },
        (err) => { this.isLoading$.next(false); });
  }

}
