package mx.izzi.offboarding.shared.enums;

import lombok.Getter;

@Getter
public enum OBErrorCodes {

    // ============================================
    // SYSTEM ERROR CODES
    // ============================================
    INTERNAL_SERVER_ERROR("PB-SYS-001", 500, "Internal server error", "Oops... Something went wrong", "Ups... Algo salio mal. Intentalo más tarde, si el error persiste contacta con un administrador"),
    NAN("PB-SYS-002", 400, "Bad Request", "Not a number", "No es un número"),
    VALUE_NOT_VALID("PB-SYS-003", 400, "Bad Request", "Value not valid", "Valor no valido"),


    // ============================================
    // ERROR CODES
    // ============================================
    NOT_FOUND("PB-ERR001", 404, "Not Found", "Resource not found", "Recurso no encontrado"),
    ERROR_TO_CREATE("PB-ERR002", 500, "Internal server error", "Resource could not be created", "Error al crear el recurso. Intenta más tarde, si el error persiste contacta un administrador"),
    UNIQUENESS_RULE("PB-ERR003", 409, "Conflict", "Insertion error. This element already exists", "Esté recurso ya existe"),
    INVALID_BODY_PARAMS("PB-ERR004", 400, "Bad request", "Invalid params", "Parámetros inválidos"),
    NOT_AVAILABLE("PB-ERR005", 400, "Bad Request", "Resource not available", "Recurso no disponible"),
    EMPLOYEE_TERMINATED("PB-ERR006", 400, "Bad Request", "This employee has already been fired", "Esté empleado ya paso por un proceso de bloqueo. No esta activo"),

    // ============================================
    // AUTH ERROR CODES
    // ============================================

    LOGIN_ERROR_CREDENTIALS("PB-AUTH-ERR001", 401, "Unauthorized", "Invalid credentials", "Usuario o contraseña incorrectos"),
    ACCESS_DENIED("PB-AUTH-ERR002", 403, "Forbidden", "Access denied", "Acceso denegado"),
    RESOURCE_ACCESS_DENIED("PB-AUTH-ERR003", 403, "Forbidden", "Access denied", "No tienes permiso para acceder a este recurso"),
    ;

    private final String code;
    private final Integer httpStatus;
    private final String httpCode;
    private final String message;
    private final String display;

    OBErrorCodes(String code, Integer httpStatus, String httpCode, String message, String display) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.httpCode = httpCode;
        this.message = message;
        this.display = display;
    }
}
