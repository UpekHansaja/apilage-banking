package lk.jiat.bank.ejb.interceptor;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@Audit
public class AuditInterceptor {

    @AroundInvoke
    public Object auditMethod(InvocationContext ctx) throws Exception {
        // Example: You could write to DB or system log
        System.out.println("[AUDIT] " + ctx.getMethod().getName() + " called on " + ctx.getTarget().getClass().getSimpleName());
        return ctx.proceed();
    }
}
