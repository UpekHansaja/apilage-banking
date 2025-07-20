package lk.jiat.bank.ejb.service;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.jiat.bank.jpa.entity.Account;
import lk.jiat.bank.jpa.entity.User;

import java.math.BigDecimal;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@RolesAllowed({"USER", "ADMIN"})
public class AccountServiceBean {

    @PersistenceContext(unitName = "ApilageBankingPU")
    private EntityManager em;

    @RolesAllowed({"ADMIN"})
    public Account createAccount(User user, String accountNumber) {
        Account acc = new Account();
        acc.setUser(user);
        acc.setAccountNumber(accountNumber);
        acc.setBalance(BigDecimal.ZERO);
        em.persist(acc);
        return acc;
    }

    @RolesAllowed({"USER", "ADMIN"})
    public Account getAccountByNumber(String number) {
        return em.createQuery("SELECT a FROM Account a WHERE a.accountNumber = :num", Account.class)
                .setParameter("num", number)
                .getSingleResult();
    }

    @RolesAllowed({"USER", "ADMIN"})
    public List<Account> getAccountsByUser(User user) {
        return em.createQuery("SELECT a FROM Account a WHERE a.user = :user", Account.class)
                .setParameter("user", user)
                .getResultList();
    }

    @RolesAllowed({"ADMIN"})
    public void closeAccount(Account acc) {
        acc.setStatus(lk.jiat.bank.jpa.entity.AccountStatus.CLOSED);
        em.merge(acc);
    }

    @RolesAllowed({"ADMIN"})
    public void freezeAccount(Account acc) {
        acc.setStatus(lk.jiat.bank.jpa.entity.AccountStatus.FROZEN);
        em.merge(acc);
    }

    @RolesAllowed({"ADMIN"})
    public void unfreezeAccount(Account acc) {
        acc.setStatus(lk.jiat.bank.jpa.entity.AccountStatus.ACTIVE);
        em.merge(acc);
    }
}
