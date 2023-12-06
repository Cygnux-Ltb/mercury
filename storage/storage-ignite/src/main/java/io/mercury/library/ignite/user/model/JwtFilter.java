package io.mercury.library.ignite.user.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * Check the jwt token from front end if is invalid
 **/
public class JwtFilter extends GenericFilterBean {

    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {

        // Change the req and res to HttpServletRequest and HttpServletResponse
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        // Get authorization from Http request
        final String authHeader = request.getHeader("authorization");

        // If the Http request is OPTIONS then just return the status code 200
        // which is HttpServletResponse.SC_OK in this code
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);

            chain.doFilter(req, res);
        }
        // Except OPTIONS, other request should be checked by JWT
        else {

            // Check the authorization, check if the token is started by "Bearer "
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ServletException("Missing or invalid Authorization header");
            }

            // Then get the JWT token from authorization
            final String token = authHeader.substring(7);

            try {
                // Use JWT parser to check if the signature is valid with the Key "secretkey"
                final Claims claims = Jwts.parser().setSigningKey("secretkey").build().parseClaimsJws(token)
                        .getBody();

                // Add the claim to request header
                request.setAttribute("claims", claims);
            } catch (final SecurityException e) {
                throw new ServletException("Invalid token");
            }

            chain.doFilter(req, res);
        }
    }
}