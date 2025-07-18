package lk.jiat.bank.ejb.mdb;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;
import jakarta.jms.*;
import java.util.logging.Logger;

import lk.jiat.bank.ejb.service.TransactionServiceBean;

@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/TransactionQueue"),
                @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue")
        }
)
public class TransactionNotificationMDB implements MessageListener {

    private static final Logger logger = Logger.getLogger(TransactionNotificationMDB.class.getName());

    @Inject
    private TransactionServiceBean transactionService;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage textMessage) {
                String command = textMessage.getText();
                logger.info("📥 Received message from TransactionQueue: " + command);

                if ("CALCULATE_INTEREST".equals(command)) {
                    transactionService.calculateDailyInterest();
                }
            }
        } catch (JMSException e) {
            logger.severe("❌ Failed to process JMS message: " + e.getMessage());
        }
    }
}
