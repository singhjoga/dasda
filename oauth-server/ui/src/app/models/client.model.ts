export interface Client {
    clientId?: string;
    secret?: string;
    scopes?: string;
    grantTypes?: string;
    roles?: string;
    accessTokenValidityMs?: number;
    refreshTokenValidityMs?: number;
    id?: number;
    addDate?: string;
    addUser?: string;
    updateDate?: string;
    updateUser?: string;
}
