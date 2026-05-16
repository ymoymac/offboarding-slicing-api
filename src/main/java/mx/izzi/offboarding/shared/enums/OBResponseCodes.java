package mx.izzi.offboarding.shared.enums;

import lombok.Getter;

@Getter
public enum OBResponseCodes {
    // ============================================
    // USER RESPONSE CODES
    // ============================================
    USER_CREATED(201, "Created", "User created successfully"),
    USER_LOGIN(201, "Success", "Successful login"),
    GET_USER(200, "Get user", "User retrieved successfully"),
    LIST_ACTIVE_USER(200, "Get all active users", "All active users were obtained"),

    // ============================================
    // TERMINATION RESPONSE CODES
    // ============================================
    TERMINATION_CREATED(200, "Created", "Block request successfully created"),

    ;

    private final Integer httpStatus;
    private final String httpCode;
    private final String message;

    OBResponseCodes(Integer httpStatus, String httpCode, String message) {
        this.httpStatus = httpStatus;
        this.httpCode = httpCode;
        this.message = message;
    }

}
