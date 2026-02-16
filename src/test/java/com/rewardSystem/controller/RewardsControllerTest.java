package com.rewardSystem.controller;

import com.rewardSystem.entity.RewardPoints;
import com.rewardSystem.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardsController.class)
@DisplayName("RewardsController Test Suite")
class RewardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    private List<RewardPoints> mockRewards;
    private RewardPoints mockReward1;
    private RewardPoints mockReward2;

    @BeforeEach
    void setUp() {
        mockRewards = new ArrayList<>();

        // Setup mock reward 1
        mockReward1 = new RewardPoints();
        mockReward1.setCustomerId(1);
        Map<String, Integer> monthlyRewards1 = new HashMap<>();
        monthlyRewards1.put("JANUARY", 100);
        monthlyRewards1.put("FEBRUARY", 50);
        mockReward1.setMonthlyRewards(monthlyRewards1);
        mockReward1.setTotalRewardPoints(150);

        // Setup mock reward 2
        mockReward2 = new RewardPoints();
        mockReward2.setCustomerId(2);
        Map<String, Integer> monthlyRewards2 = new HashMap<>();
        monthlyRewards2.put("JANUARY", 75);
        mockReward2.setMonthlyRewards(monthlyRewards2);
        mockReward2.setTotalRewardPoints(75);
    }

    @Test
    @DisplayName("Should return HTTP 200 OK when requesting all rewards")
    void testGetAllRewardsReturnsOk() throws Exception {
        // Arrange
        mockRewards.add(mockReward1);
        mockRewards.add(mockReward2);
        when(rewardService.findAllRewards()).thenReturn(mockRewards);

        // Act & Assert
        mockMvc.perform(get("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return correct content type (application/json)")
    void testGetAllRewardsReturnsJsonContentType() throws Exception {
        // Arrange
        mockRewards.add(mockReward1);
        when(rewardService.findAllRewards()).thenReturn(mockRewards);

        // Act & Assert
        mockMvc.perform(get("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Should return empty list when no rewards exist")
    void testGetAllRewardsEmptyList() throws Exception {
        // Arrange
        when(rewardService.findAllRewards()).thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(get("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should return correct number of rewards")
    void testGetAllRewardsReturnCorrectCount() throws Exception {
        // Arrange
        mockRewards.add(mockReward1);
        mockRewards.add(mockReward2);
        when(rewardService.findAllRewards()).thenReturn(mockRewards);

        // Act & Assert
        mockMvc.perform(get("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should return rewards with correct customer IDs")
    void testGetAllRewardsReturnCorrectCustomerIds() throws Exception {
        // Arrange
        mockRewards.add(mockReward1);
        mockRewards.add(mockReward2);
        when(rewardService.findAllRewards()).thenReturn(mockRewards);

        // Act & Assert
        mockMvc.perform(get("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId", is(1)))
                .andExpect(jsonPath("$[1].customerId", is(2)));
    }

    @Test
    @DisplayName("Should return correct total reward points for each customer")
    void testGetAllRewardsReturnCorrectTotalPoints() throws Exception {
        // Arrange
        mockRewards.add(mockReward1);
        mockRewards.add(mockReward2);
        when(rewardService.findAllRewards()).thenReturn(mockRewards);

        // Act & Assert
        mockMvc.perform(get("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalRewardPoints", is(150)))
                .andExpect(jsonPath("$[1].totalRewardPoints", is(75)));
    }

    @Test
    @DisplayName("Should return correct monthly rewards breakdown")
    void testGetAllRewardsReturnCorrectMonthlyBreakdown() throws Exception {
        // Arrange
        mockRewards.add(mockReward1);
        when(rewardService.findAllRewards()).thenReturn(mockRewards);

        // Act & Assert
        mockMvc.perform(get("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].monthlyRewards.JANUARY", is(100)))
                .andExpect(jsonPath("$[0].monthlyRewards.FEBRUARY", is(50)));
    }

    @Test
    @DisplayName("Should return complete JSON structure with all fields")
    void testGetAllRewardsReturnCompleteStructure() throws Exception {
        // Arrange
        mockRewards.add(mockReward1);
        when(rewardService.findAllRewards()).thenReturn(mockRewards);

        // Act
        MvcResult result = mockMvc.perform(get("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Assert all required fields are present
                .andExpect(jsonPath("$[0].customerId").exists())
                .andExpect(jsonPath("$[0].monthlyRewards").exists())
                .andExpect(jsonPath("$[0].totalRewardPoints").exists())
                .andReturn();

        // Verify the response is valid JSON
        assertNotNull(result.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("Should handle multiple customers in response")
    void testGetAllRewardsMultipleCustomers() throws Exception {
        // Arrange
        mockRewards.add(mockReward1);
        mockRewards.add(mockReward2);
        when(rewardService.findAllRewards()).thenReturn(mockRewards);

        // Act & Assert
        mockMvc.perform(get("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].customerId", hasItems(1, 2)))
                .andExpect(jsonPath("$[*].totalRewardPoints", hasItems(150, 75)));
    }

//    @Test
//    @DisplayName("Should return rewards in array format")
//    void testGetAllRewardsReturnArrayFormat() throws Exception {
//        // Arrange
//        mockRewards.add(mockReward1);
//        when(rewardService.findAllRewards()).thenReturn(mockRewards);
//
//        // Act & Assert
//        mockMvc.perform(get("/v1/api/rewards")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect((ResultMatcher) jsonPath("$", isArray()));
//    }

    @Test
    @DisplayName("Should return rewards in array format")
    void testGetAllRewardsReturnArrayFormat() throws Exception {

        mockRewards.add(mockReward1);
        when(rewardService.findAllRewards()).thenReturn(mockRewards);

        mockMvc.perform(get("/v1/api/rewards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());  // ✅ Correct
    }

    @Test
    @DisplayName("Should accept GET request on /v1/api/rewards endpoint")
    void testGetAllRewardsEndpointMapping() throws Exception {
        // Arrange
        when(rewardService.findAllRewards()).thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(get("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 405 Method Not Allowed for POST request")
    void testPostRequestNotAllowed() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Should return 405 Method Not Allowed for PUT request")
    void testPutRequestNotAllowed() throws Exception {
        // Act & Assert
        mockMvc.perform(put("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Should return 405 Method Not Allowed for DELETE request")
    void testDeleteRequestNotAllowed() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("Should return single reward response with all fields populated")
    void testGetAllRewardsSingleRewardCompleteData() throws Exception {
        // Arrange
        mockRewards.add(mockReward1);
        when(rewardService.findAllRewards()).thenReturn(mockRewards);

        // Act & Assert
        mockMvc.perform(get("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId", equalTo(1)))
                .andExpect(jsonPath("$[0].totalRewardPoints", equalTo(150)))
                .andExpect(jsonPath("$[0].monthlyRewards", notNullValue()))
                .andExpect(jsonPath("$[0].monthlyRewards.JANUARY", equalTo(100)))
                .andExpect(jsonPath("$[0].monthlyRewards.FEBRUARY", equalTo(50)));
    }

    @Test
    @DisplayName("Should handle large response with many customers")
    void testGetAllRewardsLargeDataSet() throws Exception {
        // Arrange
        List<RewardPoints> largeDataSet = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            RewardPoints reward = new RewardPoints();
            reward.setCustomerId(i);
            Map<String, Integer> monthlyRewards = new HashMap<>();
            monthlyRewards.put("JANUARY", i * 10);
            reward.setMonthlyRewards(monthlyRewards);
            reward.setTotalRewardPoints(i * 10);
            largeDataSet.add(reward);
        }
        when(rewardService.findAllRewards()).thenReturn(largeDataSet);

        // Act & Assert
        mockMvc.perform(get("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(100)))
                .andExpect(jsonPath("$[0].customerId", is(1)))
                .andExpect(jsonPath("$[99].customerId", is(100)));
    }

    @Test
    @DisplayName("Should verify service method is called")
    void testVerifyServiceMethodCalled() throws Exception {
        // Arrange
        mockRewards.add(mockReward1);
        when(rewardService.findAllRewards()).thenReturn(mockRewards);

        // Act
        mockMvc.perform(get("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Assert - Verify the service method was called (implicit through mock)
        verify(rewardService, times(1)).findAllRewards();
    }

    @Test
    @DisplayName("Should return zero points correctly in response")
    void testGetAllRewardsWithZeroPoints() throws Exception {
        // Arrange
        RewardPoints zeroReward = new RewardPoints();
        zeroReward.setCustomerId(3);
        zeroReward.setMonthlyRewards(new HashMap<>());
        zeroReward.setTotalRewardPoints(0);
        mockRewards.add(zeroReward);
        when(rewardService.findAllRewards()).thenReturn(mockRewards);

        // Act & Assert
        mockMvc.perform(get("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalRewardPoints", is(0)));
    }

    @Test
    @DisplayName("Should return response as JSON array not object")
    void testGetAllRewardsResponseIsArray() throws Exception {
        // Arrange
        mockRewards.add(mockReward1);
        mockRewards.add(mockReward2);
        when(rewardService.findAllRewards()).thenReturn(mockRewards);

        // Act
        MvcResult result = mockMvc.perform(get("/v1/api/rewards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        String content = result.getResponse().getContentAsString();
        assertTrue(content.startsWith("["), "Response should start with array bracket [");
        assertTrue(content.endsWith("]"), "Response should end with array bracket ]");
    }
}
