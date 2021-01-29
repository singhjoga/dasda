import { Router } from '@angular/router';

export const ROUTE_HOME = 'home';
export const ROUTE_LOGIN = 'login';
export const ROUTE_CLIENTS = 'clients';
export const ROUTE_ROLES = 'roles';
export const ROUTE_GROUPS = 'groups';
export const ROUTE_USERS = 'users';
export const ROUTE_AUTH_SETTINGS = 'authsettings';
export const ROUTE_WILDCARD = '**';

export const routeToLogin = (router: Router) => router.navigate([ROUTE_LOGIN]);
export const routeToHome = (router: Router) => router.navigate([ROUTE_HOME]);
export const routeToClients = (router: Router) => router.navigate([ROUTE_CLIENTS]);
export const routeToRoles = (router: Router) => router.navigate([ROUTE_ROLES]);
export const routeToGroups = (router: Router) => router.navigate([ROUTE_GROUPS]);
export const routeToUsers = (router: Router) => router.navigate([ROUTE_USERS]);
export const routeToAuthSettings = (router: Router) => router.navigate([ROUTE_AUTH_SETTINGS]);
