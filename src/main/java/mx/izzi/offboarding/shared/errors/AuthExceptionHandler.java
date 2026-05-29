package mx.izzi.offboarding.shared.errors;

import mx.izzi.offboarding.shared.enums.OBErrorCodes;
import mx.izzi.offboarding.shared.exceptions.ResourceAccessDeniedException;
import mx.izzi.offboarding.shared.models.OBErrorResponse;
import mx.izzi.offboarding.shared.utils.ErrorMapper;
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

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<OBErrorResponse> handleAuthorizationDeniedExceptionSpringBoot(AuthorizationDeniedException e) {

        return new ResponseEntity<>(
                new OBErrorResponse(
                        OBErrorCodes.ACCESS_DENIED,
                        e.getMessage(),
                        ErrorMapper.errors(OBErrorCodes.ACCESS_DENIED)
                ),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<OBErrorResponse> handleInternalAuthenticationService(InternalAuthenticationServiceException e) {
        return new ResponseEntity<>(
                new OBErrorResponse(
                        OBErrorCodes.LOGIN_ERROR_CREDENTIALS,
                        e.getMessage(),
                        ErrorMapper.errors(OBErrorCodes.LOGIN_ERROR_CREDENTIALS)
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<OBErrorResponse> handleBadCredentials(BadCredentialsException e) {
        return new ResponseEntity<>(
                new OBErrorResponse(
                        OBErrorCodes.LOGIN_ERROR_CREDENTIALS,
                        e.getMessage(),
                        ErrorMapper.errors(OBErrorCodes.LOGIN_ERROR_CREDENTIALS)
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(ResourceAccessDeniedException.class)
    public ResponseEntity<OBErrorResponse> handleAccessDenied(ResourceAccessDeniedException e) {
        return new ResponseEntity<>(
                new OBErrorResponse(
                        OBErrorCodes.RESOURCE_ACCESS_DENIED,
                        e.getMessage(),
                        ErrorMapper.errors(OBErrorCodes.RESOURCE_ACCESS_DENIED)
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

}
