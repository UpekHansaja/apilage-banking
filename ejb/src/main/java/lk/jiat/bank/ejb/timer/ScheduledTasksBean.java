package lk.jiat.bank.ejb.timer;

import jakarta.annotation.Resource;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.jms.*;

import java.util.logging.Logger;

@Singleton
@Startup
public class ScheduledTasksBean {

    private static final Logger logger = Logger.getLogger(ScheduledTasksBean.class.getName());

    @Resource(lookup = "jms/ApilageBankingFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "jms/TransactionQueue")
    private Queue transactionQueue;

    @Schedule(hour = "0", minute = "0", second = "0", persistent = false)
    public void scheduleDailyInterestCalculation() {
        logger.info("🔔 Triggering daily interest calculation...");

        try (JMSContext context = connectionFactory.createContext()) {
            TextMessage message = context.createTextMessage("CALCULATE_INTEREST");
            context.createProducer().send(transactionQueue, message);
            logger.info("📤 Message sent to TransactionQueue");
        } catch (Exception e) {
            logger.severe("❌ Error sending scheduled message: " + e.getMessage());
        }
    }
}
