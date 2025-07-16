package lk.jiat.bank.security.auth;

import jakarta.security.auth.message.callback.PasswordValidationCallback;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.inject.Inject;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.util.*;

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
        subject.getPrincipals().add(() -> username);
        subject.getPrincipals().addAll(() -> roles);
        return true;
    }

    @Override public boolean abort() { return false; }
    @Override public boolean logout() { return true; }
}
