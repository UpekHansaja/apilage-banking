package lk.jiat.bank.ejb.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class LoanServiceBean {

    @PersistenceContext(unitName = "ApilageBankingPU")
    private EntityManager em;

    // Placeholder logic — to be extended later
    public void issueLoan(String userId, double amount) {
        // Business logic for issuing a loan
        System.out.println("Issuing loan of: " + amount + " to user: " + userId);
    }
}
