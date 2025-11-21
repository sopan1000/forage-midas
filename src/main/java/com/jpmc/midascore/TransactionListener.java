package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TransactionListener {

    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);
    // Note: This list is primarily for Task 2 verification, kept for code structure
    private final CopyOnWriteArrayList<BigDecimal> receivedAmounts = new CopyOnWriteArrayList<>();

    private final TransactionService transactionService;

    @Autowired
    public TransactionListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    public void listen(Transaction transaction) {
        // Task 3 Logic: Process the transaction via the service
        boolean success = transactionService.processTransaction(transaction);

        // Task 2 compatibility (conversion from float to BigDecimal, only used by TaskTwoTests)
        BigDecimal amount = BigDecimal.valueOf(transaction.getAmount());
        receivedAmounts.add(amount);

        if (success) {
            logger.info("Transaction processed successfully and recorded.");
        } else {
            logger.warn("Transaction failed validation and was discarded.");
        }
    }

    public CopyOnWriteArrayList<BigDecimal> getReceivedAmounts() {
        return receivedAmounts;
    }
}
