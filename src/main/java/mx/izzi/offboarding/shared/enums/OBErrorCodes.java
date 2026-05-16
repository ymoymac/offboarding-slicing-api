package mx.izzi.offboarding.shared.enums;

import lombok.Getter;

@Getter
public enum OBErrorCodes {

    // ============================================
    // USER ERROR CODES
    // ============================================
    INTERNAL_SERVER_ERROR(500, "Internal server error", "Oops... Something went wrong"),
    USER_NOT_FOUND(404, "Not Found", "User not found"),
    USER_ERROR_TO_CREATE(500, "Internal server error", "User could not be created"),
    USER_ERROR_PARAMS(400, "Bad request", "User could not be created. Some values are not valid"),
    USER_UNIQUENESS_RULE(400, "Bad Request", "Insertion error. This element already exists"),
    DATA_INTEGRITY_ERROR(409, "Bad Request", "Data integrity error"),
    LOGIN_ERROR_CREDENTIALS(401, "Unauthorized", "Invalid credentials"),
    ACCESS_DENIED(403, "Forbidden", "Access denied"),
    ;

    private final Integer httpStatus;
    private final String httpCode;
    private final String message;

    OBErrorCodes(Integer httpStatus, String httpCode, String message) {
        this.httpStatus = httpStatus;
        this.httpCode = httpCode;
        this.message = message;
    }
}
