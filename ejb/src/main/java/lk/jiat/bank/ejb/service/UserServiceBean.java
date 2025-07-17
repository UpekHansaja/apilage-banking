package lk.jiat.bank.ejb.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lk.jiat.bank.jpa.entity.User;

@Stateless
public class UserServiceBean {

    @PersistenceContext(unitName = "apilageBankingPU")
    private EntityManager em;

    public User findByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Could be handled differently if preferred
        }
    }
}
