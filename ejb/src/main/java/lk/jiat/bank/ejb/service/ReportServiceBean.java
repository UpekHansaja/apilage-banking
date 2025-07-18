package lk.jiat.bank.ejb.service;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.jiat.bank.jpa.entity.Transaction;
import lk.jiat.bank.jpa.entity.User;

import java.util.List;

@Stateless
@RolesAllowed("ADMIN")
public class ReportServiceBean {

    @PersistenceContext(unitName = "ApilageBankingPU")
    private EntityManager em;

    public List<Transaction> getRecentTransactions(int limit) {
        return em.createQuery("SELECT t FROM Transaction t ORDER BY t.createdAt DESC", Transaction.class)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u ORDER BY u.createdAt DESC", User.class)
                .getResultList();
    }

    public long countTotalUsers() {
        return em.createQuery("SELECT COUNT(u) FROM User u", Long.class)
                .getSingleResult();
    }

    public long countTotalTransactions() {
        return em.createQuery("SELECT COUNT(t) FROM Transaction t", Long.class)
                .getSingleResult();
    }
}
