package lk.jiat.bank.security.auth;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DbLoginModule implements LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private String username;
    private Set<String> roles = new HashSet<>();

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
    }

    @Override
    public boolean login() throws LoginException {
        NameCallback nameCb = new NameCallback("Username:");
        PasswordCallback passwordCb = new PasswordCallback("Password:", false);
        Callback[] callbacks = new Callback[]{nameCb, passwordCb};

        try {
            callbackHandler.handle(callbacks);
            username = nameCb.getName();
            String password = new String(passwordCb.getPassword());

            // Dummy check (you should inject a UserService and verify credentials)
            if ("admin".equals(username) && "admin123".equals(password)) {
                roles.add("ADMIN");
                return true;
            }

            throw new LoginException("Invalid credentials");

        } catch (Exception e) {
            throw new LoginException("Login failed: " + e.getMessage());
        }
    }

    @Override
    public boolean commit() {
        subject.getPrincipals().add(new Principal() {
            @Override
            public String getName() {
                return username;
            }
        });

        for (String role : roles) {
            subject.getPrincipals().add(new Principal() {
                @Override
                public String getName() {
                    return role;
                }
            });
        }

        return true;
    }


    @Override
    public boolean abort() {
        return false;
    }

    @Override
    public boolean logout() {
        return true;
    }
}
