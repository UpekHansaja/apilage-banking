package lk.jiat.bank.rest.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import lk.jiat.bank.security.jwt.JwtUtil;
import lk.jiat.bank.security.rbac.RoleManager;

import java.security.Principal;
import java.util.*;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private SecurityContext securityContext;

    @Inject
    private RoleManager roleManager;

    @Context
    private HttpServletRequest request;

    @POST
    @Path("/login")
    @PermitAll
    public Response login(@Context HttpServletRequest req) {
        try {
            // Trigger JAAS authentication
            req.login(req.getParameter("username"), req.getParameter("password"));

            Principal userPrincipal = req.getUserPrincipal();
            if (userPrincipal == null) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Login failed").build();
            }

            String username = userPrincipal.getName();
            Set<String> roles = roleManager.getUserRoles(username);
            String token = JwtUtil.generateToken(username, roles);

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return Response.ok(response).build();

        } catch (ServletException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
    }
}
