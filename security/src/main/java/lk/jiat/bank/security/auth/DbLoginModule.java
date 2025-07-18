package lk.jiat.bank.security.auth;

import lk.jiat.bank.ejb.service.UserServiceBean;
import lk.jiat.bank.jpa.entity.User;

import javax.naming.InitialContext;
import javax.naming.NamingException;
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

    private UserServiceBean userService;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler,
                           Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;

        try {
            InitialContext ctx = new InitialContext();
            userService = (UserServiceBean) ctx.lookup("java:global/apilagebanking/ejb/UserServiceBean");
        } catch (NamingException e) {
            throw new RuntimeException("❌ Failed to lookup UserServiceBean in LoginModule", e);
        }
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

            // 🔐 Real check via UserService
            User user = userService.findByUsername(username);

            if (user == null || !userService.verifyPassword(user, password)) {
                throw new LoginException("❌ Invalid credentials");
            }

            if (!user.isEnabled()) {
                throw new LoginException("📧 Email not verified. Please check your inbox.");
            }

            user.getRoles().forEach(role -> roles.add(role.getName()));
            return true;

        } catch (Exception e) {
            throw new LoginException("❌ Login failed: " + e.getMessage());
        }
    }

    @Override
    public boolean commit() {
        subject.getPrincipals().add((Principal) () -> username);
        roles.forEach(role -> subject.getPrincipals().add((Principal) () -> role));
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
