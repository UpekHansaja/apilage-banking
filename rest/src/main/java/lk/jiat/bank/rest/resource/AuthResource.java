package lk.jiat.bank.rest.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.security.enterprise.SecurityContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.jiat.bank.security.jwt.JwtUtil;
import lk.jiat.bank.security.rbac.RoleManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
            // Read raw request body
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = req.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // Convert to JSON
            String body = sb.toString();
            JsonObject jsonObject = Json.createReader(new StringReader(body)).readObject();

            String userName = jsonObject.getString("username");
            String passWord = jsonObject.getString("password");

            // Trigger JAAS authentication
            req.login(userName, passWord);

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
