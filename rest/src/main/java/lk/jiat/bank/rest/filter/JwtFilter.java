package lk.jiat.bank.rest.filter;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lk.jiat.bank.security.jwt.JwtUtil;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        String token = authHeader.substring("Bearer ".length());
        if (!JwtUtil.isTokenValid(token)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }

        // Optionally set security context here...
    }
}
