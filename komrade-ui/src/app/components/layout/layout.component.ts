import { Component, OnInit, ViewChild } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';
import { CommonFunctionsService } from 'src/app/services/common-functions.service';
import { navigateToUsers, navigateToRoles, navigateToClients } from 'src/app/routing';
import { Router } from '@angular/router';
import { MatSidenav } from '@angular/material';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit {
  @ViewChild('snav') leftSideBar: MatSidenav;

  public locale: string;

  constructor(
    private service: AuthService,
    private common: CommonFunctionsService,
    private router: Router,
    private translation: TranslateService
  ) { }

  ngOnInit() {
    this.locale = localStorage.getItem('locale');
  }

  toggleLeftSideNav() {
    this.leftSideBar.toggle();
  }

  isAuthenticated() {
    return this.service.isAuthenticated();
  }

  isUserPermitted() {
    return this.common.isPermitted('user', 'view', '');
  }

  isRolePermitted() {
    return this.common.isPermitted('role', 'view', '');
  }

  isClientPermitted() {
    return this.common.isPermitted('client', 'view', '');
  }

  goToUsers() {
    navigateToUsers(this.router);
    this.toggleLeftSideNav();
  }

  goToRoles() {
    navigateToRoles(this.router);
    this.toggleLeftSideNav();
  }

  goToClients() {
    navigateToClients(this.router);
    this.toggleLeftSideNav();
  }

  changeLanguge(event) {
    const value = event.value;
    localStorage.setItem('locale', value)
    this.translation.setDefaultLang(value);
  }
}
