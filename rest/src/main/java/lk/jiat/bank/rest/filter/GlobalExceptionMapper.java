package lk.jiat.bank.rest.filter;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable ex) {
        ex.printStackTrace(); // Log to console/server log
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Something went wrong: " + ex.getMessage())
                .build();
    }
}
