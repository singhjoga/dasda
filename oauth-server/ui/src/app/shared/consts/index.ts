export * from './configurations';


export function getHTTPErrorMessage(error) {
    if (error.error.message) {
        return error.error.message;
    } else if (error.error.errorDetail) {
        return error.error.errorDetail.message;
    } else {
        return error.error.error;
    }
}

export const PROVIDER_ACTIVE_DIRECTORY = 'ActiveDirectory';
export const PROVIDER_EXERNAL_DATABASE = 'ExternalDatabase';
export const PROVIDER_INTERNAL_DATABASE = 'InternalDatabase';
export const PROVIDER_LDAP = 'LDAP';

export const PROVIDER_TYPES = [
    PROVIDER_ACTIVE_DIRECTORY,
    PROVIDER_EXERNAL_DATABASE,
    PROVIDER_INTERNAL_DATABASE,
    PROVIDER_LDAP
];

export const PASSWORD_ENCODER_NOOP = 'NoOp';
export const PASSWORD_ENCODER_BCRYPT = 'BCrypt';
export const PASSWORD_ENCODER_BPKDF = 'Bpkdf2';
export const PASSWORD_ENCODER_SCRYPT = 'SCrypt';
export const PASSWORD_ENCODER_MD4 = 'MD4';
export const PASSWORD_ENCODER_SHA256 = 'SHA256';

export const PASSWORD_ENCODER = [
    PASSWORD_ENCODER_NOOP,
    PASSWORD_ENCODER_BCRYPT,
    PASSWORD_ENCODER_BPKDF,
    PASSWORD_ENCODER_SCRYPT,
    PASSWORD_ENCODER_MD4,
    PASSWORD_ENCODER_SHA256
];

