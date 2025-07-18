package lk.jiat.bank.jpa.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private boolean verified;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "verification_token_created_at")
    private LocalDateTime verificationTokenCreatedAt;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return passwordHash;
    }

    public void setPassword(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setEnabled(boolean b) {
        this.verified = b;
    }

    public boolean isEnabled() {
        return verified;
    }

    public String getVerificationCode() {
        return verificationToken;
    }

    public void setVerificationCode(String string) {
        this.verificationToken = string;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVerificationToken(Object o) {
        if (o instanceof String) {
            this.verificationToken = (String) o;
        } else {
            throw new IllegalArgumentException("Verification token must be a String");
        }
    }
}
