package mx.izzi.offboarding.shared.enums;

import lombok.Getter;

@Getter
public enum OBResponseCodes {
    // ============================================
    // RESPONSE CODES
    // ============================================
    CREATED(201, "Created", "Resource created successfully"),
    LOGIN(201, "Success", "Successful login"),
    GET_RESOURCE(200, "Get resource", "Resource retrieved successfully"),
    LIST_ACTIVE_RESOURCES(200, "Get all active resources", "All active resources were obtained"),
    ROLE_ASSIGNED(201, "Created", "Role assigned successfully"),
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
