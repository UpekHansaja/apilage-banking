package lk.jiat.bank.ejb.interceptor;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Interceptor
@Audit
public class AuditInterceptor {

    private static final String AUDIT_FILE = "audit.log";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @AroundInvoke
    public Object auditMethod(InvocationContext ctx) throws Exception {
        // Create audit log entry
        String timestamp = LocalDateTime.now().format(formatter);
        String methodName = ctx.getMethod().getName();
        String className = ctx.getTarget().getClass().getSimpleName();
        String auditMessage = String.format("[%s] Method '%s' called on %s%n",
                timestamp, methodName, className);

        System.out.print(auditMessage);

        try {
            writeToAuditLog(auditMessage);
        } catch (IOException e) {
            System.err.println("Failed to write to audit log: " + e.getMessage());
        }

        return ctx.proceed();
    }

    private void writeToAuditLog(String message) throws IOException {
        File logFile = new File(AUDIT_FILE);

        if (!logFile.exists()) {
            logFile.createNewFile();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))) {
            writer.print(message);
        }
    }

}
