package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRecordRepository;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive; // <-- NEW IMPORT
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final UserRecordRepository userRepository;
    private final TransactionRecordRepository transactionRepository;
    private final IncentiveService incentiveService; // <-- NEW FIELD

    @Autowired
    public TransactionService(UserRecordRepository userRepository,
                              TransactionRecordRepository transactionRepository,
                              IncentiveService incentiveService) { // <-- INJECT NEW SERVICE
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.incentiveService = incentiveService;
    }

    @Transactional
    public boolean processTransaction(Transaction transaction) {
        // 1. Find Sender and Recipient
        Optional<UserRecord> senderOpt = userRepository.findById(transaction.getSenderId());
        Optional<UserRecord> recipientOpt = userRepository.findById(transaction.getRecipientId());

        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            logger.warn("Validation failed: Sender or Recipient not found.");
            return false;
        }

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();
        float amount = transaction.getAmount();

        // 2. Validate Balance
        if (sender.getBalance() < amount) {
            logger.warn("Validation failed: Sender {} balance ({}) is insufficient for amount ({}).",
                    sender.getName(), sender.getBalance(), amount);
            return false;
        }

        // --- Task 4 Core Logic ---

        // 3. Get Incentive from API
        Incentive incentive = incentiveService.calculateIncentive(transaction);
        float incentiveAmount = incentive.getAmount();

        // 4. Adjust Balances

        // Sender deduction
        sender.setBalance(sender.getBalance() - amount);
        // Recipient gain (transaction amount + incentive amount)
        recipient.setBalance(recipient.getBalance() + amount + incentiveAmount);

        // 5. Save and Record
        userRepository.save(sender);
        userRepository.save(recipient);

        // Record the transaction history, including incentive
        TransactionRecord record = new TransactionRecord(sender, recipient, amount, incentiveAmount);
        transactionRepository.save(record);

        logger.info("Transaction processed: {} paid {} to {} (Incentive: {})",
                sender.getName(), amount, recipient.getName(), incentiveAmount);
        return true;
    }
}