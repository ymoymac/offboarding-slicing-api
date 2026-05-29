package mx.izzi.offboarding.modules.auth.domain.models;

import mx.izzi.offboarding.modules.users.domain.models.User;
import mx.izzi.offboarding.shared.exceptions.ResourceAccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AuthUtils.class.getName());

    private AuthUtils() {}

    private static UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResourceAccessDeniedException("");
        }
        return (UserDetails) authentication.getPrincipal();
    }

    public static boolean isAdmin(User user) {

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user);
        return authenticatedUser.isAdmin();
    }

    public static boolean isSameUser(User user) {
        LOG.info("[INFO]: current user is {}", getCurrentUser());
        return getCurrentUser().getUsername().equals(user.getEmail());
    }
}
