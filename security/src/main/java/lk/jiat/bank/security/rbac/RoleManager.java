package lk.jiat.bank.security.rbac;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.jiat.bank.jpa.entity.User;

import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class RoleManager {

    @PersistenceContext(unitName = "ApilageBankingPU")
    private EntityManager em;

    public Set<String> getUserRoles(String username) {
        User user = em.createQuery(
                        "SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();

        return user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
    }

    public boolean hasRole(String username, String requiredRole) {
        return getUserRoles(username).contains(requiredRole);
    }
}
