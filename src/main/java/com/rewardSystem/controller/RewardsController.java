package com.rewardSystem.controller;

import com.rewardSystem.entity.RewardResponse;
import com.rewardSystem.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("v1/api")
public class RewardsController {
    @Autowired
    private RewardService rewardService;

    @GetMapping("/rewards")
    public List<RewardResponse> getAllRewards(){
        return rewardService.findAllRewards();
    }
}
