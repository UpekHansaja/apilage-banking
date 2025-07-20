package lk.jiat.bank.rest.filter;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import lk.jiat.bank.security.jwt.JwtUtil;

import java.io.IOException;
import java.util.logging.Logger;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtFilter implements ContainerRequestFilter {

    private static final Logger LOG = Logger.getLogger(JwtFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String path = requestContext.getUriInfo().getPath();
        LOG.info("Processing request to: " + path);

        // Skip JWT check for test and auth/login endpoints
        if (path.contains("test")) {

            LOG.info("Skipping JWT check for test endpoint");

        } else if (path.contains("auth/login")) {

            LOG.info("Skipping JWT check for auth/login endpoint");

        } else if (path.contains("auth/register")) {

            LOG.info("Skipping JWT check for auth/register endpoint");

        } else {

            String authHeader = requestContext.getHeaderString("Authorization");
            LOG.info("Auth header: " + authHeader);

            if (authHeader == null) {
                LOG.warning("Authorization header is missing");
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Authorization header is missing").build());
                return;
            }

            String token = authHeader.substring("Bearer ".length());
            if (!JwtUtil.isTokenValid(token)) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
        }

    }
}
