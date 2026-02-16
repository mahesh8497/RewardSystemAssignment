
package com.rewardSystem.service;

import com.rewardSystem.entity.RewardPoints;
import com.rewardSystem.entity.CustomerTranscation;
import com.rewardSystem.exception.DataProcessingException;
import com.rewardSystem.exception.InternalServerException;
import com.rewardSystem.repository.TransactionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardServiceImpl implements RewardService {

    private static final Logger logger = LoggerFactory.getLogger(RewardServiceImpl.class);

    @Autowired
    private TransactionsRepository transactionsRepository;

    /**
     * Finds all rewards for customers based on transactions from the last 3 months.
     *
     * @return List of RewardPoints for all customers
     * @throws InternalServerException if database access fails
     * @throws DataProcessingException if data processing fails
     */
    @Override
    public List<RewardPoints> findAllRewards() {
        logger.debug("Starting findAllRewards operation");

        try {
            LocalDate threeMonthsAgo = LocalDate.now().minusMonths(2).withDayOfMonth(1);
            logger.debug("Filtering transactions from date: {}", threeMonthsAgo);

            // Fetch all transactions from database
            List<CustomerTranscation> allTransactions = transactionsRepository.findAll();
            logger.info("Retrieved {} total transactions from database", allTransactions.size());

            if (allTransactions == null || allTransactions.isEmpty()) {
                logger.warn("No transactions found in database");
                return List.of();
            }

            // Filter, group, and process transactions
            List<RewardPoints> rewards = allTransactions.stream()
                    .filter(t -> !t.getDate().isBefore(threeMonthsAgo))
                    .collect(Collectors.groupingBy(CustomerTranscation::getCustomerId))
                    .entrySet().stream()
                    .map(entry -> {
                        logger.debug("Processing rewards for customer ID: {}", entry.getKey());
                        return buildRewardResponse(entry.getKey(), entry.getValue());
                    })
                    .collect(Collectors.toList());

            logger.info("Successfully calculated rewards for {} customers", rewards.size());
            return rewards;

        } catch (NullPointerException e) {
            logger.error("Null pointer exception while processing rewards", e);
            throw new InternalServerException("An error occurred while processing rewards", e);
        } catch (DataProcessingException e) {
            logger.error("Data processing error occurred: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching all rewards", e);
            throw new InternalServerException("An unexpected error occurred while processing your request", e);
        }
    }

    /**
     * Builds a reward response object for a specific customer based on their transactions.
     *
     * @param customerId the ID of the customer
     * @param transactions list of transactions for the customer
     * @return RewardPoints object containing calculated reward data
     * @throws DataProcessingException if calculation fails
     */
    private RewardPoints buildRewardResponse(int customerId, List<CustomerTranscation> transactions) {
        logger.debug("Building reward response for customer ID: {}", customerId);

        try {
            Map<String, Integer> monthlyPoints = new HashMap<>();
            int totalPoints = 0;

            for (CustomerTranscation trans : transactions) {
                try {
                    int points = calculatePoints(trans.getAmount());
                    String month = trans.getDate().getMonth().toString();
                    monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
                    totalPoints += points;
                    logger.trace("Calculated points for customer {}: {} points for {}",
                            customerId, points, month);
                } catch (NullPointerException e) {
                    logger.error("Error processing transaction for customer {}: {}", customerId, e.getMessage(), e);
                    throw new DataProcessingException("Error processing transaction for customer " + customerId, e);
                }
            }

            RewardPoints response = new RewardPoints();
            response.setCustomerId(customerId);
            response.setMonthlyRewards(monthlyPoints);
            response.setTotalRewardPoints(totalPoints);

            logger.debug("Successfully built reward response for customer {}: {} total points",
                    customerId, totalPoints);
            return response;

        } catch (DataProcessingException e) {
            logger.error("Data processing error for customer {}: {}", customerId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while building reward response for customer {}", customerId, e);
            throw new DataProcessingException("Error building reward response for customer " + customerId, e);
        }
    }

    /**
     * Calculates reward points based on transaction amount.
     * Rules:
     * - 2 points for every dollar spent over $100
     * - 1 point for every dollar spent between $50 and $100
     *
     * @param amount the transaction amount
     * @return calculated reward points
     * @throws IllegalArgumentException if amount is invalid
     */
    private int calculatePoints(double amount) {
        logger.trace("Calculating points for amount: {}", amount);

        try {
            if (amount < 0) {
                throw new IllegalArgumentException("Transaction amount cannot be negative");
            }

            int points = 0;
            if (amount > 100) {
                points += (amount - 100) * 2;
            }
            if (amount > 50) {
                points += Math.min(amount, 100) - 50;
            }

            logger.trace("Calculated {} points for amount {}", points, amount);
            return points;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid amount for point calculation: {}", amount, e);
            throw e;
        } catch (Exception e) {
            logger.error("Error calculating points for amount {}", amount, e);
            throw new DataProcessingException("Error calculating reward points", e);
        }
    }
}

