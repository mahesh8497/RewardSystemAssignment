package com.rewardSystem.service;

import com.rewardSystem.entity.RewardPoints;

import java.util.List;

public interface RewardService {
    List<RewardPoints> findAllRewards();

}
