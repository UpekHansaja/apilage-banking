package lk.jiat.bank.rest.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.jiat.bank.security.jwt.JwtUtil;
import lk.jiat.bank.security.rbac.RoleManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private RoleManager roleManager;

    @POST
    @Path("/login")
    public Response login(Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // ⚠️ Replace this dummy check with DB login service / JAAS if wired
        if (!"admin".equals(username) || !"admin123".equals(password)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        Set<String> roles = roleManager.getUserRoles(username);
        String token = JwtUtil.generateToken(username, roles);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return Response.ok(response).build();
    }
}
