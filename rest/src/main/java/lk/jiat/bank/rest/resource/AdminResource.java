package lk.jiat.bank.rest.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.jiat.bank.ejb.service.AdminServiceBean;
import lk.jiat.bank.jpa.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Path("/admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class AdminResource {

    @EJB
    private AdminServiceBean adminService;

    @POST
    @Path("/create-user")
    public Response createUser(Map<String, Object> body) {
        try {
            String username = (String) body.get("username");
            String email = (String) body.get("email");
            String password = (String) body.get("password");
            List<String> rolesList = (List<String>) body.get("roles");

            if (username == null || email == null || password == null || rolesList == null)
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "Missing fields")).build();

            adminService.createUser(username, email, password, Set.copyOf(rolesList));

            return Response.ok(Map.of("message", "User created and verification email sent")).build();

        } catch (Exception e) {
            return Response.serverError().entity(Map.of("error", e.getMessage())).build();
        }
    }

    @GET
    @Path("/users")
    public Response getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return Response.ok(users).build();
    }

    @PUT
    @Path("/users/{id}/block")
    public Response blockUser(@PathParam("id") Long id) {
        try {
            adminService.blockUser(id);
            return Response.ok(Map.of("message", "User blocked")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(Map.of("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("/run-interest")
    public Response runInterest() {
        adminService.triggerInterestManually();
        return Response.ok(Map.of("message", "Interest calculation triggered")).build();
    }
}
