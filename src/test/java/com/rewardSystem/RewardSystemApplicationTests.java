package com.rewardSystem;

import com.rewardSystem.controller.RewardsController;
import com.rewardSystem.service.RewardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("RewardSystemApplication Test Suite")
class RewardSystemApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired(required = false)
	private RewardsController rewardsController;

	@Autowired(required = false)
	private RewardService rewardService;

	@Test
	@DisplayName("Should load application context successfully")
	void contextLoads() {
		assertNotNull(applicationContext, "Application context should not be null");
	}

	@Test
	@DisplayName("Should create RewardsController bean")
	void shouldCreateRewardsControllerBean() {
		assertNotNull(rewardsController, "RewardsController bean should be created");
	}

	@Test
	@DisplayName("Should create RewardService bean")
	void shouldCreateRewardServiceBean() {
		assertNotNull(rewardService, "RewardService bean should be created");
	}

	@Test
	@DisplayName("Should have RewardsController in application context")
	void shouldHaveRewardsControllerInContext() {
		assertTrue(applicationContext.containsBean("rewardsController"),
			"Application context should contain rewardsController bean");
	}

	@Test
	@DisplayName("Should have RewardService in application context")
	void shouldHaveRewardServiceInContext() {
		assertTrue(applicationContext.containsBean("rewardServiceImpl"),
			"Application context should contain rewardServiceImpl bean");
	}

	@Test
	@DisplayName("Should have correct application name")
	void shouldHaveCorrectApplicationName() {
		assertNotNull(applicationContext.getId(), "Application context should have an ID");
	}

	@Test
	@DisplayName("Should verify beans are autowired correctly")
	void shouldVerifyBeansAreAutowired() {
		assertNotNull(applicationContext);
		Object controller = applicationContext.getBean("rewardsController");
		Object service = applicationContext.getBean("rewardServiceImpl");
		assertNotNull(controller);
		assertNotNull(service);
	}
}
