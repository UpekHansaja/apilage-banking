package lk.jiat.bank.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {

//    @Override
//    public Set<Class<?>> getClasses() {
//        return Set.of(
//                lk.jiat.bank.rest.resource.AuthResource.class,
//                lk.jiat.bank.rest.resource.UserResource.class,
//                lk.jiat.bank.rest.resource.AdminResource.class,
//                lk.jiat.bank.rest.resource.AccountResource.class,
//                lk.jiat.bank.rest.resource.TransferResource.class,
//                lk.jiat.bank.rest.resource.ReportResource.class,
//                lk.jiat.bank.rest.filter.CORSFilter.class,
//                lk.jiat.bank.rest.filter.GlobalExceptionMapper.class
//                // Note: Don't register JwtFilter here if using @PermitAll for auth endpoint
//        );
//    }
}