package lk.jiat.bank.rest.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import lk.jiat.bank.ejb.service.AccountServiceBean;
import lk.jiat.bank.ejb.service.UserServiceBean;
import lk.jiat.bank.jpa.entity.Account;
import lk.jiat.bank.jpa.entity.User;

import java.security.Principal;
import java.util.List;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

    @EJB
    private AccountServiceBean accountService;

    @EJB
    private UserServiceBean userService;

    @GET
    @RolesAllowed({"CUSTOMER", "ADMIN"})
    public Response getMyAccounts(@Context SecurityContext ctx) {
        Principal principal = ctx.getUserPrincipal();

        if (principal == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        String username = principal.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        List<Account> accounts = accountService.getAccountsByUser(user);
        return Response.ok(accounts).build();
    }
}
