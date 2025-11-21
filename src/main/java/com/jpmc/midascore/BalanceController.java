package com.jpmc.midascore;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRecordRepository;
import com.jpmc.midascore.foundation.Balance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class BalanceController {

    private final UserRecordRepository userRepository;

    @Autowired
    public BalanceController(UserRecordRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Exposes a GET endpoint at /balance that accepts a required userId
    @GetMapping("/balance")
    public Balance getBalance(@RequestParam(name = "userId") Long userId) {
        // Find the user by ID
        Optional<UserRecord> userRecordOpt = userRepository.findById(userId);

        if (userRecordOpt.isPresent()) {
            // User exists: return their current balance
            UserRecord user = userRecordOpt.get();
            return new Balance(user.getBalance());
        } else {
            // User does not exist: return a balance of 0.0f, as required
            return new Balance(0.0f);
        }
    }
}