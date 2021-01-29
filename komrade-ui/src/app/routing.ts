import { Router } from '@angular/router';

export const PAGE_LOGIN = 'login';
export const PAGE_HOME = 'home';
export const PAGE_ROLES = 'roles';
export const PAGE_USERS = 'users';
export const PAGE_CLIENTS = 'clients';

export const navigateToHome = (router: Router) => router.navigate([PAGE_HOME]);
export const navigateToRoles = (router: Router) => router.navigate([PAGE_ROLES]);
export const navigateToUsers = (router: Router) => router.navigate([PAGE_USERS]);
export const navigateToLogin = (router: Router) => router.navigate([PAGE_LOGIN]);
export const navigateToClients = (router: Router) => router.navigate([PAGE_CLIENTS]);
