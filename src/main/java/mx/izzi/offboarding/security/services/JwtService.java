package mx.izzi.offboarding.security.services;

import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    JWTClaimsSet validateToken(String token) throws Exception;
}
