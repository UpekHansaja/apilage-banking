package lk.jiat.bank.rest.filter;

import jakarta.ws.rs.container.*;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CORSFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) {
        res.getHeaders().add("Access-Control-Allow-Origin", "*");
        res.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        res.getHeaders().add("Access-Control-Allow-Credentials", "true");
        res.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    }
}
