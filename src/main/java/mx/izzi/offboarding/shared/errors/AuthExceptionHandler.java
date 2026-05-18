package mx.izzi.offboarding.shared.errors;

import mx.izzi.offboarding.shared.enums.OBErrorCodes;
import mx.izzi.offboarding.shared.exceptions.ResourceAccessDeniedException;
import mx.izzi.offboarding.shared.models.OBErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<OBErrorResponse> handleInternalAuthenticationService(InternalAuthenticationServiceException e) {

        return getObErrorResponseResponseEntity(e.getMessage(), e);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<OBErrorResponse> handleBadCredentials(BadCredentialsException e) {

        return getObErrorResponseResponseEntity(e.getMessage(), e);
    }

    @NonNull
    private ResponseEntity<OBErrorResponse> getObErrorResponseResponseEntity(String message, Exception e) {
        List<Map<String, Object>> errors = new ArrayList<>();

        Map<String, Object> err = new HashMap<>();
        err.put("code", OBErrorCodes.LOGIN_ERROR_CREDENTIALS.getCode());
        err.put("display", OBErrorCodes.LOGIN_ERROR_CREDENTIALS.getDisplay());
        errors.add(err);

        return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.LOGIN_ERROR_CREDENTIALS, message, errors),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler({ResourceAccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<OBErrorResponse> handleAccessDenied(ResourceAccessDeniedException e) {

        List<Map<String, Object>> errors = new ArrayList<>();

        Map<String, Object> err = new HashMap<>();
        err.put("code", OBErrorCodes.RESOURCE_ACCESS_DENIED.getCode());
        err.put("display", OBErrorCodes.RESOURCE_ACCESS_DENIED.getDisplay());
        errors.add(err);

        return new ResponseEntity<>(
                new OBErrorResponse(OBErrorCodes.RESOURCE_ACCESS_DENIED, e.getMessage(), errors),
                HttpStatus.FORBIDDEN
        );
    }

}
