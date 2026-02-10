
package com.rewardSystem.service;

import com.rewardSystem.entity.RewardResponse;
import com.rewardSystem.entity.Transactions;
import com.rewardSystem.repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardServiceImpl implements RewardService {

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Override
    public List<RewardResponse> findAllRewards() {
        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(2).withDayOfMonth(1);

        return transactionsRepository.findAll().stream()
                .filter(t -> !t.getDate().isBefore(threeMonthsAgo))
                .collect(Collectors.groupingBy(Transactions::getCustomerId))
                .entrySet().stream()
                .map(entry -> buildRewardResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private RewardResponse buildRewardResponse(int customerId, List<Transactions> transactions) {
        Map<String, Integer> monthlyPoints = new HashMap<>();
        int totalPoints = 0;

        for (Transactions trans : transactions) {
            int points = calculatePoints(trans.getAmount());
            String month = trans.getDate().getMonth().toString();
            monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
            totalPoints += points;
        }

        RewardResponse response = new RewardResponse();
        response.setCustomerId(customerId);
        response.setMonthlyRewards(monthlyPoints);
        response.setTotalRewardPoints(totalPoints);
        return response;
    }

    private int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (amount - 100) * 2;
        }
        if (amount > 50) {
            points += Math.min(amount, 100) - 50;
        }
        return points;
    }
}

