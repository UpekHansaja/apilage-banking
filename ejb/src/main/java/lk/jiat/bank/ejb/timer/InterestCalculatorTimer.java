package lk.jiat.bank.ejb.timer;

import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lk.jiat.bank.ejb.service.TransactionServiceBean;
import lk.jiat.bank.jpa.entity.Account;
import lk.jiat.bank.jpa.entity.AccountStatus;
import lk.jiat.bank.jpa.entity.Transaction;
import lk.jiat.bank.jpa.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Singleton
@Startup
public class InterestCalculatorTimer {

    @PersistenceContext(unitName = "apilageBankingPU")
    private EntityManager em;

    @Inject
    private TransactionServiceBean transactionService;

    private static final BigDecimal ANNUAL_INTEREST_RATE = new BigDecimal("0.01");
    private static final BigDecimal DAILY_INTEREST_RATE = ANNUAL_INTEREST_RATE.divide(new BigDecimal("365"), 10, BigDecimal.ROUND_HALF_UP);

    @Schedule(hour = "23", minute = "0", second = "0", persistent = false)
    @Transactional
    public void calculateDailyInterest() {
        List<Account> accounts = em.createQuery("SELECT a FROM Account a WHERE a.status = :status", Account.class)
                .setParameter("status", AccountStatus.ACTIVE)
                .getResultList();

        for (Account account : accounts) {
            BigDecimal balance = account.getBalance();
            BigDecimal interest = balance.multiply(DAILY_INTEREST_RATE);

            if (interest.compareTo(BigDecimal.ZERO) > 0) {
                // Update account balance
                account.setBalance(balance.add(interest));
                em.merge(account);

                // Add transaction entry
                Transaction txn = new Transaction();
                txn.setAccount(account);
                txn.setAmount(interest);
                txn.setType(TransactionType.CREDIT);
                txn.setDescription("Daily interest credited");
                txn.setCreatedAt(LocalDateTime.now());

                em.persist(txn);
            }
        }

        System.out.println("✅ Daily interest applied to " + accounts.size() + " account(s).");
    }
}
