package com.rewardSystem.controller;

import com.rewardSystem.entity.RewardPoints;
import com.rewardSystem.service.RewardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for managing rewards-related endpoints.
 * Provides API endpoints to retrieve customer reward information.
 */
@RestController
@RequestMapping("v1/api")
public class RewardsController {

    private static final Logger logger = LoggerFactory.getLogger(RewardsController.class);

    @Autowired
    private RewardService rewardService;

    /**
     * Retrieves all rewards for all customers.
     *
     * @return List of RewardPoints containing customer reward data
     */
    @GetMapping("/rewards")
    public List<RewardPoints> getAllRewards() {
        logger.info("Fetching all rewards for customers");
        try {
            List<RewardPoints> rewards = rewardService.findAllRewards();
            logger.info("Successfully retrieved rewards. Total records: {}", rewards.size());
            return rewards;
        } catch (Exception e) {
            logger.error("Error occurred while fetching all rewards", e);
            throw e;
        }
    }
}
