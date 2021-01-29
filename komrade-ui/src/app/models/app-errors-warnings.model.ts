export class AppErrorsWarnings {
    errorDetail?: ErrorDetail;
    warningDetail?: WarningDetail;

}

export class ErrorDetail {
    code?: number;
    message?: string;
    errors?: AppError[];
}

export class WarningDetail {
    code?: number;
    message?: string;
    warnings?: AppWarning[];
}

class AppError {
    code?: number;
    field?: string;
    message?: string;
}

class AppWarning {
    code?: number;
    message?: string;
}
