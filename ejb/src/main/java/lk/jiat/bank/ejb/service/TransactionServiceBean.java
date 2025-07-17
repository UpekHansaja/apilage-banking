package lk.jiat.bank.ejb.service;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.jiat.bank.jpa.entity.Account;
import lk.jiat.bank.jpa.entity.Transaction;
import lk.jiat.bank.jpa.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class TransactionServiceBean {

    @PersistenceContext(unitName = "ApilageBankingPU")
    private EntityManager em;

    public Transaction deposit(Account acc, BigDecimal amount, String desc) {
        acc.setBalance(acc.getBalance().add(amount));
        em.merge(acc);

        Transaction txn = new Transaction();
        txn.setAccount(acc);
        txn.setAmount(amount);
        txn.setDescription(desc);
        txn.setType(TransactionType.DEPOSIT);
        txn.setCreatedAt(LocalDateTime.now());
        em.persist(txn);
        return txn;
    }

    public Transaction withdraw(Account acc, BigDecimal amount, String desc) {
        if (acc.getBalance().compareTo(amount) < 0)
            throw new RuntimeException("Insufficient funds");

        acc.setBalance(acc.getBalance().subtract(amount));
        em.merge(acc);

        Transaction txn = new Transaction();
        txn.setAccount(acc);
        txn.setAmount(amount);
        txn.setDescription(desc);
        txn.setType(TransactionType.WITHDRAWAL);
        txn.setCreatedAt(LocalDateTime.now());
        em.persist(txn);
        return txn;
    }

    public Transaction transfer(Account from, Account to, BigDecimal amount) {
        withdraw(from, amount, "Transfer to " + to.getAccountNumber());
        return deposit(to, amount, "Transfer from " + from.getAccountNumber());
    }
}
