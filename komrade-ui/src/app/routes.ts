import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { PAGE_HOME, PAGE_LOGIN, PAGE_ROLES, PAGE_USERS } from './routing';
import { AuthGuardService } from './services/auth-guard.service';
import { RolesComponent } from './components/roles/roles.component';
import { UsersComponent } from './components/users/users.component';

export const appRoutes: Routes = [
    { path: PAGE_LOGIN, component: LoginComponent, data: { breadcrumb: 'Login' } },
    { path: PAGE_HOME, component: HomeComponent, data: { breadcrumb: 'Home' }, canActivate: [AuthGuardService] },
    { path: PAGE_ROLES, component: RolesComponent, data: { breadcrumb: 'Roles' }, canActivate: [AuthGuardService] },
    { path: PAGE_USERS, component: UsersComponent, data: { breadcrumb: 'Users' }, canActivate: [AuthGuardService] },

    { path: '', redirectTo: '/' + PAGE_HOME, pathMatch: 'full', data: { breadcrumb: '' } },
    { path: '**', redirectTo: '/' + PAGE_HOME, pathMatch: 'full', data: { breadcrumb: '' } },
];
