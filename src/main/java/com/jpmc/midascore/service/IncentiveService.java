package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveService {

    private final RestTemplate restTemplate;

    // Read the API URL from application.properties
    @Value("${incentive.api.url}")
    private String apiUrl;

    public IncentiveService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Incentive calculateIncentive(Transaction transaction) {
        // Send the Transaction object as JSON in a POST request
        return restTemplate.postForObject(
                apiUrl + "/incentive",
                transaction,
                Incentive.class
        );
    }
}