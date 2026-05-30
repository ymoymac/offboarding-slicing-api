package mx.izzi.offboarding.shared.exceptions;

public class ResourceRoleAlreadyAssignedException extends RuntimeException {
    public ResourceRoleAlreadyAssignedException(String message) {
        super(message);
    }
}
