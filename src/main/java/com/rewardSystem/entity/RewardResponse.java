package com.rewardSystem.entity;

import java.util.Map;

public class RewardResponse {

    private int customerId;

    private Map<String, Integer> monthlyRewards;

    private int totalRewardPoints;

    public RewardResponse() {

    }

    public RewardResponse(int customerId, Map<String, Integer> monthlyRewards, int totalRewardPoints) {
        this.customerId = customerId;
        this.monthlyRewards = monthlyRewards;
        this.totalRewardPoints = totalRewardPoints;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public Map<String, Integer> getMonthlyRewards() {
        return monthlyRewards;
    }

    public void setMonthlyRewards(Map<String, Integer> monthlyRewards) {
        this.monthlyRewards = monthlyRewards;
    }

    public int getTotalRewardPoints() {
        return totalRewardPoints;
    }

    public void setTotalRewardPoints(int totalRewardPoints) {
        this.totalRewardPoints = totalRewardPoints;
    }

    @Override
    public String toString() {
        return "RewardResponse [customerId=" + customerId + ", monthlyRewards=" + monthlyRewards
                + ", totalRewardPoints=" + totalRewardPoints + "]";
    }

}
