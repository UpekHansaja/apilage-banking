package lk.jiat.bank.rest.resource;

import jakarta.ejb.EJB;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lk.jiat.bank.ejb.service.AccountServiceBean;
import lk.jiat.bank.ejb.service.TransactionServiceBean;
import lk.jiat.bank.jpa.entity.Account;
import lk.jiat.bank.jpa.entity.Transaction;

import java.math.BigDecimal;
import java.util.Map;

@Path("/transfer")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransferResource {

    @EJB
    private AccountServiceBean accountService;

    @EJB
    private TransactionServiceBean txnService;

    @POST
    @RolesAllowed("CUSTOMER")
    public Response transferFunds(Map<String, String> request) {
        String fromAcc = request.get("from");
        String toAcc = request.get("to");
        BigDecimal amount = new BigDecimal(request.get("amount"));

        Account source = accountService.getAccountByNumber(fromAcc);
        Account dest = accountService.getAccountByNumber(toAcc);

        Transaction txn = txnService.transfer(source, dest, amount);
        return Response.ok(txn).build();
    }
}
