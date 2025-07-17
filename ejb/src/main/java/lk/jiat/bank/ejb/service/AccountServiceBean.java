package lk.jiat.bank.ejb.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.jiat.bank.jpa.entity.Account;
import lk.jiat.bank.jpa.entity.User;

import java.math.BigDecimal;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AccountServiceBean {

    @PersistenceContext(unitName = "ApilageBankingPU")
    private EntityManager em;

    public Account createAccount(User user, String accountNumber) {
        Account acc = new Account();
        acc.setUser(user);
        acc.setAccountNumber(accountNumber);
        acc.setBalance(BigDecimal.ZERO);
        em.persist(acc);
        return acc;
    }

    public Account getAccountByNumber(String number) {
        return em.createQuery("SELECT a FROM Account a WHERE a.accountNumber = :num", Account.class)
                .setParameter("num", number)
                .getSingleResult();
    }

    public List<Account> getAccountsByUser(User user) {
        return em.createQuery("SELECT a FROM Account a WHERE a.user = :user", Account.class)
                .setParameter("user", user)
                .getResultList();
    }

    public void freezeAccount(Account acc) {
        acc.setStatus(lk.jiat.bank.jpa.entity.AccountStatus.FROZEN);
        em.merge(acc);
    }
}
