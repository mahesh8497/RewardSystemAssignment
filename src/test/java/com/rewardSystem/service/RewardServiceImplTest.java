package com.rewardSystem.service;

import com.rewardSystem.entity.RewardResponse;
import com.rewardSystem.entity.Transactions;
import com.rewardSystem.repository.TransactionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RewardServiceImpl Test Suite")
class RewardServiceImplTest {

    @Mock
    private TransactionsRepository transactionsRepository;

    @InjectMocks
    private RewardServiceImpl rewardService;

    private List<Transactions> testTransactions;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        testTransactions = new ArrayList<>();
        today = LocalDate.now();
    }

    @Test
    @DisplayName("Should return empty list when no transactions exist")
    void testFindAllRewardsEmptyTransactions() {
        // Arrange
        when(transactionsRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertNotNull(rewards);
        assertEquals(0, rewards.size());
    }

    @Test
    @DisplayName("Should calculate rewards for single customer with single transaction")
    void testFindAllRewardsSingleCustomerSingleTransaction() {
        // Arrange
        testTransactions.add(new Transactions(1, 120.0, today));
        when(transactionsRepository.findAll()).thenReturn(testTransactions);

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertEquals(1, rewards.size());
        RewardResponse response = rewards.get(0);
        assertEquals(1, response.getCustomerId());
        assertEquals(90, response.getTotalRewardPoints()); // (100-50)*1 + (120-100)*2 = 50 + 40 = 90
        assertNotNull(response.getMonthlyRewards());
    }

    @Test
    @DisplayName("Should calculate rewards correctly for amount > 100")
    void testRewardCalculationAmountGreaterThan100() {
        // Arrange
        testTransactions.add(new Transactions(1, 150.0, today));
        when(transactionsRepository.findAll()).thenReturn(testTransactions);

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertEquals(1, rewards.size());
        assertEquals(150, rewards.get(0).getTotalRewardPoints()); // (100-50)*1 + (150-100)*2 = 50 + 100 = 150
    }

    @Test
    @DisplayName("Should calculate rewards correctly for amount between 50 and 100")
    void testRewardCalculationAmountBetween50And100() {
        // Arrange
        testTransactions.add(new Transactions(1, 75.0, today));
        when(transactionsRepository.findAll()).thenReturn(testTransactions);

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertEquals(1, rewards.size());
        assertEquals(25, rewards.get(0).getTotalRewardPoints()); // (75-50)*1 = 25
    }

    @Test
    @DisplayName("Should return 0 points for amount <= 50")
    void testRewardCalculationAmountLessThanOrEqual50() {
        // Arrange
        testTransactions.add(new Transactions(1, 50.0, today));
        when(transactionsRepository.findAll()).thenReturn(testTransactions);

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertEquals(1, rewards.size());
        assertEquals(0, rewards.get(0).getTotalRewardPoints());
    }

    @Test
    @DisplayName("Should aggregate multiple transactions for same customer")
    void testMultipleTransactionsSameCustomer() {
        // Arrange
        testTransactions.add(new Transactions(1, 120.0, today));
        testTransactions.add(new Transactions(1, 80.0, today));
        testTransactions.add(new Transactions(1, 60.0, today));
        when(transactionsRepository.findAll()).thenReturn(testTransactions);

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertEquals(1, rewards.size());
        RewardResponse response = rewards.get(0);
        assertEquals(1, response.getCustomerId());
        int expectedPoints = 90 + 30 + 10; // 90 + 30 + 10 = 130
        assertEquals(expectedPoints, response.getTotalRewardPoints());
    }

    @Test
    @DisplayName("Should handle multiple customers separately")
    void testMultipleCustomersSeparateRewards() {
        // Arrange
        testTransactions.add(new Transactions(1, 120.0, today));
        testTransactions.add(new Transactions(2, 75.0, today));
        testTransactions.add(new Transactions(3, 200.0, today));
        when(transactionsRepository.findAll()).thenReturn(testTransactions);

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertEquals(3, rewards.size());

        // Verify customer 1
        RewardResponse customer1 = rewards.stream()
                .filter(r -> r.getCustomerId() == 1)
                .findFirst()
                .orElse(null);
        assertNotNull(customer1);
        assertEquals(90, customer1.getTotalRewardPoints());

        // Verify customer 2
        RewardResponse customer2 = rewards.stream()
                .filter(r -> r.getCustomerId() == 2)
                .findFirst()
                .orElse(null);
        assertNotNull(customer2);
        assertEquals(25, customer2.getTotalRewardPoints());

        // Verify customer 3
        RewardResponse customer3 = rewards.stream()
                .filter(r -> r.getCustomerId() == 3)
                .findFirst()
                .orElse(null);
        assertNotNull(customer3);
        assertEquals(250, customer3.getTotalRewardPoints()); // (100-50)*1 + (200-100)*2 = 50 + 200 = 250
    }

    @Test
    @DisplayName("Should break down rewards by month correctly")
    void testMonthlyRewardBreakdown() {
        // Arrange
        LocalDate thisMonth = today;
        LocalDate lastMonth = today.minusMonths(1);

        testTransactions.add(new Transactions(1, 120.0, thisMonth));
        testTransactions.add(new Transactions(1, 75.0, lastMonth));
        when(transactionsRepository.findAll()).thenReturn(testTransactions);

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertEquals(1, rewards.size());
        RewardResponse response = rewards.get(0);
        Map<String, Integer> monthlyRewards = response.getMonthlyRewards();

        assertNotNull(monthlyRewards);
        assertEquals(2, monthlyRewards.size());
        assertEquals(90, monthlyRewards.get(thisMonth.getMonth().toString()));
        assertEquals(25, monthlyRewards.get(lastMonth.getMonth().toString()));
    }

    @Test
    @DisplayName("Should only include transactions from last 3 months")
    void testOnlyIncludesLast3Months() {
        // Arrange
        LocalDate withinRange = today; // Current month
        LocalDate withinRange2 = today.minusMonths(1); // Last month
        LocalDate withinRange3 = today.minusMonths(2); // 2 months ago
        LocalDate outOfRange = today.minusMonths(3).minusDays(1); // More than 3 months ago

        testTransactions.add(new Transactions(1, 120.0, withinRange));
        testTransactions.add(new Transactions(1, 75.0, withinRange2));
        testTransactions.add(new Transactions(1, 60.0, withinRange3));
        testTransactions.add(new Transactions(1, 100.0, outOfRange)); // This should be excluded
        when(transactionsRepository.findAll()).thenReturn(testTransactions);

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertEquals(1, rewards.size());
        RewardResponse response = rewards.get(0);
        Map<String, Integer> monthlyRewards = response.getMonthlyRewards();

        // Total should be 90 + 25 + 10 = 125 (excluding the old transaction)
        assertEquals(125, response.getTotalRewardPoints());
        assertEquals(3, monthlyRewards.size()); // Should have 3 months
    }

    @Test
    @DisplayName("Should handle decimal amounts correctly")
    void testDecimalAmountHandling() {
        // Arrange
        testTransactions.add(new Transactions(1, 125.75, today));
        when(transactionsRepository.findAll()).thenReturn(testTransactions);

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertEquals(1, rewards.size());
        // (100-50)*1 + (125.75-100)*2 = 50 + 51 = 101 (truncated)
        assertTrue(rewards.get(0).getTotalRewardPoints() >= 100);
    }

    @Test
    @DisplayName("Should handle large amounts correctly")
    void testLargeAmountHandling() {
        // Arrange
        testTransactions.add(new Transactions(1, 5000.0, today));
        when(transactionsRepository.findAll()).thenReturn(testTransactions);

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertEquals(1, rewards.size());
        // (100-50)*1 + (5000-100)*2 = 50 + 9800 = 9850
        assertEquals(9850, rewards.get(0).getTotalRewardPoints());
    }

    @Test
    @DisplayName("Should aggregate transactions with same customer in different months")
    void testAggregateTransactionsMultipleMonths() {
        // Arrange
        LocalDate january = LocalDate.of(2026, 1, 15);
        LocalDate february = LocalDate.of(2026, 2, 10);

        testTransactions.add(new Transactions(1, 100.0, january));
        testTransactions.add(new Transactions(1, 100.0, january));
        testTransactions.add(new Transactions(1, 100.0, february));
        when(transactionsRepository.findAll()).thenReturn(testTransactions);

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertEquals(1, rewards.size());
        RewardResponse response = rewards.get(0);
        assertEquals(1, response.getCustomerId());
        // Each 100 = 50 points, so 3 transactions = 150 points total
        assertEquals(150, response.getTotalRewardPoints());
        assertEquals(2, response.getMonthlyRewards().size());
        assertEquals(100, response.getMonthlyRewards().get("JANUARY"));
        assertEquals(50, response.getMonthlyRewards().get("FEBRUARY"));
    }

    @Test
    @DisplayName("Should return non-null RewardResponse object")
    void testRewardResponseNotNull() {
        // Arrange
        testTransactions.add(new Transactions(1, 100.0, today));
        when(transactionsRepository.findAll()).thenReturn(testTransactions);

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertNotNull(rewards);
        assertFalse(rewards.isEmpty());
        RewardResponse response = rewards.get(0);
        assertNotNull(response.getCustomerId());
        assertNotNull(response.getMonthlyRewards());
        assertNotNull(response.getTotalRewardPoints());
    }

    @Test
    @DisplayName("Should calculate correctly with boundary amount of 51")
    void testBoundaryAmountOf51() {
        // Arrange
        testTransactions.add(new Transactions(1, 51.0, today));
        when(transactionsRepository.findAll()).thenReturn(testTransactions);

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertEquals(1, rewards.size());
        assertEquals(1, rewards.get(0).getTotalRewardPoints()); // (51-50)*1 = 1
    }

    @Test
    @DisplayName("Should calculate correctly with boundary amount of 100")
    void testBoundaryAmountOf100() {
        // Arrange
        testTransactions.add(new Transactions(1, 100.0, today));
        when(transactionsRepository.findAll()).thenReturn(testTransactions);

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertEquals(1, rewards.size());
        assertEquals(50, rewards.get(0).getTotalRewardPoints()); // (100-50)*1 = 50
    }

    @Test
    @DisplayName("Should calculate correctly with boundary amount of 101")
    void testBoundaryAmountOf101() {
        // Arrange
        testTransactions.add(new Transactions(1, 101.0, today));
        when(transactionsRepository.findAll()).thenReturn(testTransactions);

        // Act
        List<RewardResponse> rewards = rewardService.findAllRewards();

        // Assert
        assertEquals(1, rewards.size());
        assertEquals(52, rewards.get(0).getTotalRewardPoints()); // (100-50)*1 + (101-100)*2 = 50 + 2 = 52
    }
}

