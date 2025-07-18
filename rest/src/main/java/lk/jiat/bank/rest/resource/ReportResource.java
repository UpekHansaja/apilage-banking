package lk.jiat.bank.rest.resource;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.jiat.bank.ejb.service.ReportServiceBean;
import lk.jiat.bank.jpa.entity.Transaction;
import lk.jiat.bank.jpa.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/admin/reports")
@RolesAllowed("ADMIN")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReportResource {

    @EJB
    private ReportServiceBean reportService;

    @GET
    @Path("/summary")
    public Response getSystemSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalUsers", reportService.countTotalUsers());
        summary.put("totalTransactions", reportService.countTotalTransactions());
        return Response.ok(summary).build();
    }

    @GET
    @Path("/transactions")
    public Response getRecentTransactions(@QueryParam("limit") @DefaultValue("10") int limit) {
        List<Transaction> txns = reportService.getRecentTransactions(limit);
        return Response.ok(txns).build();
    }

    @GET
    @Path("/users")
    public Response getAllUsers() {
        List<User> users = reportService.getAllUsers();
        return Response.ok(users).build();
    }
}
