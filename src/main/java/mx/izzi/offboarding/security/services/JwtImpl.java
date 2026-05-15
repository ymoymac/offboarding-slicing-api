package mx.izzi.offboarding.security.services;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtImpl implements JwtService {

    private static final Logger LOG = LoggerFactory.getLogger(JwtImpl.class);

    private final Resource privateKeyResource = new ClassPathResource("keys/private.pem");

    private final Resource publicKeyResource = new ClassPathResource("keys/public.pem");

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;

    @PostConstruct
    public void init() throws Exception {
        this.privateKey = loadPrivateKey();
        this.publicKey = loadPublicKey();
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        try {
            LOG.info("[INFO]: Generating token for user {}", userDetails.getUsername());
            Instant now = Instant.now();

            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(userDetails.getUsername())
                    .issuer("offboarding-api")
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(now.plusSeconds(jwtExpiration)))
                    .claim("roles", userDetails.getAuthorities())
                    .jwtID(UUID.randomUUID().toString())
                    .build();

            SignedJWT jwt = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256)
                            .keyID("key-2026-01")
                            .type(JOSEObjectType.JWT)
                            .build(),
                    claims
            );
            jwt.sign(new RSASSASigner(privateKey));

            return jwt.serialize();
        } catch (JOSEException e) {
            LOG.error("[ERROR]: Failed to generate token for user {}", userDetails.getUsername(), e);
            LOG.error("[ERROR]: The token could not be signed");
            return null;
        }
    }

    @Override
    public JWTClaimsSet validateToken(String token) throws Exception {

        SignedJWT jwt = SignedJWT.parse(token);

        if (!jwt.verify(new RSASSAVerifier(publicKey))) {
            throw new SecurityException("Invalid signature");
        }

        JWTClaimsSet claims = jwt.getJWTClaimsSet();

        if (claims.getExpirationTime().before(new Date())) {
            throw new SecurityException("Expired token");
        }

        return claims;
    }

    private RSAPrivateKey loadPrivateKey() throws Exception {
        String key = new String(privateKeyResource.getInputStream().readAllBytes(), StandardCharsets.UTF_8)
                .replace("\uFEFF", "")
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");

        System.out.println("Private key: " + key);

        byte[] decoded = Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);

        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private RSAPublicKey loadPublicKey() throws Exception {
        String key = new String(publicKeyResource.getInputStream().readAllBytes())
                .replace("\uFEFF", "")
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
    }

}

