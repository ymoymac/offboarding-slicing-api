package mx.izzi.offboarding.security.filters;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import mx.izzi.offboarding.security.services.JwtService;
import mx.izzi.offboarding.shared.models.OBErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().startsWith("/api/v1/auth/login")) {
            LOG.info("[INFO]: JwtAuthenticationFilter Is login path '{}'", "/api/v1/auth/login");
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            LOG.info("[INFO]: JwtAuthenticationFilter is public endpoint '{}'", authHeader == null);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            LOG.info("[INFO]: Authentication Validation");

            final String token = authHeader.substring(7);

            LOG.info("[INFO]: JWT Token: {}", token);

            JWTClaimsSet claims = jwtService.validateToken(token);

            String email = claims.getSubject();

            LOG.info("[INFO]: Claim email '{}'", email);

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (Exception e) {
            LOG.error("[ERROR]: Failed to authenticate user {}", e.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            OBErrorResponse error = OBErrorResponse.builder()
                    .httpStatus(HttpServletResponse.SC_UNAUTHORIZED)
                    .httpCode("Unauthorized")
                    .message("Invalid JWT Token. Malformed or expired token")
                    .build();

            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(error);

            response.getWriter().write(jsonBody);
            response.getWriter().flush();
            return;
        }

        filterChain.doFilter(request, response);
    }
}
