export class LoginUser {
    accessToken?: string;
    refreshToken?: string;
    expiresIn?: number;
    permissions: EntityPermission[];
}

export class EntityPermission {
    entityName?: string;
    actions?: EntityAction[];
}

export class EntityAction {
    name?: string;
    constraints?: object;
}
