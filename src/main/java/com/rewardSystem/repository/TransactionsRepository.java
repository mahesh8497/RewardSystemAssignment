package com.rewardSystem.repository;

import com.rewardSystem.entity.CustomerTranscation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepository extends JpaRepository<CustomerTranscation,Long> {
}
