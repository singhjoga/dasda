import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ROUTE_LOGIN, ROUTE_HOME, ROUTE_CLIENTS, ROUTE_ROLES, ROUTE_GROUPS, ROUTE_USERS, ROUTE_AUTH_SETTINGS } from './routing';
import { LoginComponent } from './components/login/login.component';
import { AuthGuard } from './services/auth-guard.service';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ClientsComponent } from './components/clients/clients.component';
import { RolesComponent } from './components/roles/roles.component';
import { GroupsComponent } from './components/groups/groups.component';
import { UsersComponent } from './components/users/users.component';
import { AppAuthSettingsComponent } from './components/app-auth-settings/app-auth-settings.component';

const routes: Routes = [
  { path: ROUTE_HOME, component: DashboardComponent, canActivate: [AuthGuard] },
  { path: ROUTE_LOGIN, component: LoginComponent },
  { path: ROUTE_CLIENTS, component: ClientsComponent, canActivate: [AuthGuard] },
  { path: ROUTE_ROLES, component: RolesComponent, canActivate: [AuthGuard] },
  { path: ROUTE_GROUPS, component: GroupsComponent, canActivate: [AuthGuard] },
  { path: ROUTE_USERS, component: UsersComponent, canActivate: [AuthGuard] },
  { path: ROUTE_AUTH_SETTINGS, component: AppAuthSettingsComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/' + ROUTE_HOME, pathMatch: 'full' },
  { path: '**', redirectTo: '/' + ROUTE_HOME, pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
