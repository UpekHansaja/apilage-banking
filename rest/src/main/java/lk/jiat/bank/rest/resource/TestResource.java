package lk.jiat.bank.rest.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/test")
public class TestResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "REST API is working!";
    }

    @GET
    @Path("/json")
    @Produces(MediaType.APPLICATION_JSON)
    public String testJson() {
        return "{\"message\": \"REST API is working with JSON!\"}";
    }
}