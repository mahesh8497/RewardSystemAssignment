package com.rewardSystem.controller;

import com.rewardSystem.entity.RewardPoints;
import com.rewardSystem.service.RewardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("v1/api")
public class RewardsController {

    private static final Logger logger = LoggerFactory.getLogger(RewardsController.class);

    @Autowired
    private RewardService rewardService;

    @GetMapping("/rewards")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<?> getAllRewards() {
        logger.info("Fetching all rewards for customers - accessible only to ADMIN and MANAGER");
        try {
            List<RewardPoints> rewards = rewardService.findAllRewards();
            logger.info("Successfully retrieved rewards. Total records: {}", rewards.size());
            return ResponseEntity.ok(rewards);
        } catch (Exception e) {
            logger.error("Error occurred while fetching all rewards", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching rewards: " + e.getMessage());
        }
    }
}
