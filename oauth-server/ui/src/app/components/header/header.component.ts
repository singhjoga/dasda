import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/components/common/menuitem';
import { LoginService } from 'src/app/services/login.service';
import { routeToHome, routeToClients, routeToRoles, routeToGroups, routeToUsers, routeToAuthSettings } from 'src/app/routing';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.sass']
})
export class HeaderComponent implements OnInit {
  constructor(
    public loginService: LoginService,
    private router: Router
  ) { }

  items: MenuItem[];

  ngOnInit() {
    this.items = [
      { label: 'Dashboard', icon: 'pi pi-chart-bar', command: () => routeToHome(this.router) },
      { label: 'Clients', icon: 'pi pi-id-card', command: () => routeToClients(this.router) },
      { label: 'Groups', icon: 'pi pi-users', command: () => routeToGroups(this.router) },
      { label: 'Roles', icon: 'pi pi-lock', command: () => routeToRoles(this.router) },
      { label: 'Users', icon: 'pi pi-user', command: () => routeToUsers(this.router) },
      { label: 'Auth Settings', icon: 'pi pi-cog', command: () => routeToAuthSettings(this.router) },
    ];
  }

  callLogout() {
    this.loginService.logout();
  }

  goToHome() {
    routeToHome(this.router);
  }
}
