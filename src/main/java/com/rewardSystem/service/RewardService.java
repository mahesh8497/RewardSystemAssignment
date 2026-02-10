package com.rewardSystem.service;

import com.rewardSystem.entity.RewardResponse;

import java.util.List;

public interface RewardService {
    List<RewardResponse> findAllRewards();

}
