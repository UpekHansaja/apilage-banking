package lk.jiat.bank.rest.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.jiat.bank.ejb.service.UserServiceBean;

import java.util.HashMap;
import java.util.Map;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @EJB
    private UserServiceBean userService;

    /**
     * Email verification endpoint. Called by clicking on the link in the email.
     * Example: GET /api/user/verify?token=abc123
     */
    @GET
    @Path("/verify")
    @PermitAll
    public Response verifyEmail(@QueryParam("token") String token) {
        if (token == null || token.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Missing verification token"))
                    .build();
        }

        boolean verified = userService.verifyToken(token);
        if (verified) {
            return Response.ok(Map.of("message", "✅ Account verified successfully!")).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "❌ Invalid or expired token"))
                    .build();
        }
    }

    /**
     * Future extension (not implemented now):
     * - Profile info
     * - Update account
     * - Change password
     */
}
