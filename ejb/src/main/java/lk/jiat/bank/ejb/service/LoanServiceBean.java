package lk.jiat.bank.ejb.service;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.jiat.bank.jpa.entity.User;

import java.math.BigDecimal;

@Stateless
public class LoanServiceBean {

    @PersistenceContext(unitName = "ApilageBankingPU")
    private EntityManager em;

    @RolesAllowed("ADMIN")
    public void issueLoan(User user, BigDecimal loanAmount) {
        BigDecimal currentBalance = em.createQuery(
                        "SELECT a.balance FROM Account a WHERE a.user = :user",
                        BigDecimal.class)
                .setParameter("user", user)
                .getSingleResult();

//        set loanAmount to user's account
        BigDecimal newBalance = currentBalance.add(loanAmount);
        em.createQuery("UPDATE Account a SET a.balance = :newBalance WHERE a.user = :user")
                .setParameter("newBalance", newBalance)
                .setParameter("user", user)
                .executeUpdate();

        System.out.println("Loan of " + loanAmount + " issued to user: " + user.getUsername());

    }
}
