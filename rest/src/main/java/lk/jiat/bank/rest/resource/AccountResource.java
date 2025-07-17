package lk.jiat.bank.rest.resource;

import jakarta.ejb.EJB;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.jiat.bank.ejb.service.AccountServiceBean;
import lk.jiat.bank.jpa.entity.Account;
import lk.jiat.bank.jpa.entity.User;

import java.util.List;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

    @EJB
    private AccountServiceBean accountService;

    // ⚠️ Replace with proper user resolution via token
    @GET
    @Path("/{username}")
    @RolesAllowed({"CUSTOMER", "ADMIN"})
    public Response getUserAccounts(@PathParam("username") String username) {
        // Mock user to simulate test case
        User user = new User();
        user.setUsername(username);

        List<Account> accounts = accountService.getAccountsByUser(user);
        return Response.ok(accounts).build();
    }
}
