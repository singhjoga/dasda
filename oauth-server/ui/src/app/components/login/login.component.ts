import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { LoginService } from 'src/app/services/login.service';
import { routeToHome } from 'src/app/routing';
import { MessageService } from 'primeng/components/common/messageservice';
import { TOAST_LIFE, getHTTPErrorMessage } from 'src/app/shared/consts';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.sass']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  submitted = false;
  returnUrl: string;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private messageService: MessageService,
    private loginService: LoginService,
  ) {
    // redirect to home if already logged in
    if (this.loginService.currentUserValue) {
      routeToHome(this.router);
    }
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  // convenience getter for easy access to form fields
  get f() { return this.loginForm.controls; }

  onSubmit() {
    this.submitted = true;

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.loginService.login(this.f.username.value, this.f.password.value)
      .pipe(first())
      .subscribe(
        data => {
          this.messageService.add({ severity: 'success', summary: 'Success Message', detail: 'Login Successful' });
          routeToHome(this.router);
          this.loading = false;
        },
        error => {
          this.loading = false;
          console.log(error);
          /* this.messageService.add({
            severity: 'error', summary: 'Error Message',
            detail: getHTTPErrorMessage(error), life: TOAST_LIFE
          }); */
          // console.log(error);

        });
  }

}
