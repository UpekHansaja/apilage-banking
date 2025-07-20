package lk.jiat.bank.ejb.service;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lk.jiat.bank.ejb.exception.InsufficientFundsException;
import lk.jiat.bank.jpa.entity.Account;
import lk.jiat.bank.jpa.entity.Transaction;
import lk.jiat.bank.jpa.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@RolesAllowed({"USER", "ADMIN"})
public class TransactionServiceBean {

    @PersistenceContext(unitName = "ApilageBankingPU")
    private EntityManager em;

    @RolesAllowed({"USER", "ADMIN"})
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

    @RolesAllowed("USER")
    public Transaction withdraw(Account acc, BigDecimal amount, String desc) throws InsufficientFundsException {
        if (acc.getBalance().compareTo(amount) < 0)
            throw new InsufficientFundsException("Insufficient balance for transfer");

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

    @RolesAllowed({"USER", "ADMIN"})
    public Transaction transfer(Account from, Account to, BigDecimal amount) throws InsufficientFundsException {
        withdraw(from, amount, "Transfer to " + to.getAccountNumber());
        return deposit(to, amount, "Transfer from " + from.getAccountNumber());
    }

    @RolesAllowed({"ADMIN"})
    public void calculateDailyInterest() {
        BigDecimal annualInterestRate = new BigDecimal("0.01"); // 1% annual
        BigDecimal dailyInterestRate = annualInterestRate.divide(new BigDecimal("365"), 10, BigDecimal.ROUND_HALF_UP);

        var activeAccounts = em.createQuery(
                        "SELECT a FROM Account a WHERE a.status = :status", Account.class)
                .setParameter("status", lk.jiat.bank.jpa.entity.AccountStatus.ACTIVE)
                .getResultList();

        for (Account account : activeAccounts) {
            BigDecimal balance = account.getBalance();
            if (balance == null || balance.compareTo(BigDecimal.ZERO) <= 0) continue;

            BigDecimal interest = balance.multiply(dailyInterestRate);
            if (interest.compareTo(BigDecimal.ZERO) > 0) {
                // Use the existing deposit()
                deposit(account, interest, "Daily Interest");
            }
        }

        System.out.println("✅ Daily interest applied to " + activeAccounts.size() + " active account(s).");
    }

}
