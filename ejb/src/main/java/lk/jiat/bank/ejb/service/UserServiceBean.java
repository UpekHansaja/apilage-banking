package lk.jiat.bank.ejb.service;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.jiat.bank.jpa.entity.Role;
import lk.jiat.bank.jpa.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Stateless
public class UserServiceBean {

    @PersistenceContext(unitName = "ApilageBankingPU")
    private EntityManager em;

    @SuppressWarnings("EjbEnvironmentInspection")
    @Resource(name = "mail/ApilageBanking")
    private Session mailSession;

    public void register(String username, String email, String password, Set<String> roleNames) throws Exception {
        if (em.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult() > 0)
            throw new Exception("Username already exists");

        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashed);
        user.setEnabled(false); // require verification

        // Fetch and assign roles
        List<Role> roles = em.createQuery("SELECT r FROM Role r WHERE r.name IN :names", Role.class)
                .setParameter("names", roleNames)
                .getResultList();

        user.setRoles(Set.copyOf(roles));

        // Set verification token
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);

        em.persist(user);

        sendVerificationEmail(user.getEmail(), token);
    }

    public void sendVerificationEmail(String email, String token) throws MessagingException {
        Message message = new MimeMessage(mailSession);
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setFrom(new InternetAddress("upek.wemixt@gmail.com"));
        message.setSubject("Verify your ApilageBanking Account");
        message.setText("Click to verify your Account: http://localhost:8080/apilagebanking/api/user/verify?token=" + token);

        Transport.send(message);
    }

    public boolean verifyToken(String token) {
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.verificationToken = :token", User.class)
                .setParameter("token", token)
                .getResultList();

        if (users.isEmpty()) return false;

        User user = users.get(0);
        user.setEnabled(true);
        user.setVerificationToken(null);
        em.merge(user);

        return true;
    }

    public User findByUsername(String username) {
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        if (users.isEmpty()) {
            return null;
        } else {
            return users.get(0);
        }
    }

    public boolean verifyPassword(User user, String password) {
        if (user == null || user.getPassword() == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(password, user.getPassword());
        } catch (Exception e) {
            // Log the exception if needed
            return false;
        }
    }
}
