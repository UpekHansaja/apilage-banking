package lk.jiat.bank.ejb.service;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.jiat.bank.jpa.entity.Role;
import lk.jiat.bank.jpa.entity.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Stateless
@RolesAllowed("ADMIN")
public class AdminServiceBean {

    @PersistenceContext(unitName = "ApilageBankingPU")
    private EntityManager em;

    @EJB
    private TransactionServiceBean transactionService;

    public void createUser(String username, String email, String rawPassword, Set<String> roles) {
        // Check if user already exists
        if (!em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList().isEmpty()) {
            throw new RuntimeException("Username already exists.");
        }

        // Encrypt password with BCrypt
        String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashed);
        user.setEnabled(false);
        user.setVerificationCode(UUID.randomUUID().toString());

        Set<Role> roleEntities = roles.stream()
                .map(roleName -> {
                    Role role = em.find(Role.class, roleName);
                    if (role == null) throw new RuntimeException("Role not found: " + roleName);
                    return role;
                })
                .collect(Collectors.toSet());

        user.setRoles(roleEntities);
        em.persist(user);

        sendVerificationEmail(user.getEmail(), user.getVerificationCode());
    }

//    send verification code as a mail with smtp
    private void sendVerificationEmail(String email, String verificationCode) {
        String from = "upek.wemixt@gmail.com";
        String password = "wnsfwukdoslgejlx";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            message.setSubject("ApilageBanking: Verify your Email");
            message.setText("Please click the link to verify your account:\n" +
                    "http://localhost:8080/apilagebanking/api/auth/verify?code=" + verificationCode);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u LEFT JOIN FETCH u.roles", User.class)
                .getResultList();
    }

    public void blockUser(Long userId) {
        User user = em.find(User.class, userId);
        if (user == null) throw new RuntimeException("User not found.");
        user.setEnabled(false);
        em.merge(user);
    }

    public void triggerInterestManually() {
        transactionService.calculateDailyInterest();
    }
}
