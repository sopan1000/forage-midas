package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRecordRepository; // CRITICAL IMPORT
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    // Correct type for the injected field
    private final UserRecordRepository userRepository;

    public DatabaseConduit(UserRecordRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }
}