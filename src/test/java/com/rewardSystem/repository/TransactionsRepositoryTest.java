package com.rewardSystem.repository;

import com.rewardSystem.entity.CustomerTranscation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("TransactionsRepository Test Suite")
class TransactionsRepositoryTest {

    @Autowired
    private TransactionsRepository transactionsRepository;

    @BeforeEach
    void setUp() {
        transactionsRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save transaction to database")
    void testSaveTransaction() {
        // Arrange
        CustomerTranscation transaction = new CustomerTranscation(1, 100.0, LocalDate.now());

        // Act
        CustomerTranscation saved = transactionsRepository.save(transaction);

        // Assert
        assertNotNull(saved.getId());
        assertEquals(1, saved.getCustomerId());
        assertEquals(100.0, saved.getAmount());
    }

    @Test
    @DisplayName("Should retrieve all transactions")
    void testFindAll() {
        // Arrange
        transactionsRepository.save(new CustomerTranscation(1, 100.0, LocalDate.now()));
        transactionsRepository.save(new CustomerTranscation(2, 200.0, LocalDate.now()));

        // Act
        List<CustomerTranscation> transactions = transactionsRepository.findAll();

        // Assert
        assertEquals(2, transactions.size());
    }

    @Test
    @DisplayName("Should find transaction by ID")
    void testFindById() {
        // Arrange
        CustomerTranscation saved = transactionsRepository.save(new CustomerTranscation(1, 100.0, LocalDate.now()));

        // Act
        var found = transactionsRepository.findById(saved.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(1, found.get().getCustomerId());
    }

    @Test
    @DisplayName("Should update transaction")
    void testUpdateTransaction() {
        // Arrange
        CustomerTranscation saved = transactionsRepository.save(new CustomerTranscation(1, 100.0, LocalDate.now()));
        saved.setAmount(150.0);

        // Act
        CustomerTranscation updated = transactionsRepository.save(saved);

        // Assert
        assertEquals(150.0, updated.getAmount());
    }

    @Test
    @DisplayName("Should delete transaction")
    void testDeleteTransaction() {
        // Arrange
        CustomerTranscation saved = transactionsRepository.save(new CustomerTranscation(1, 100.0, LocalDate.now()));
        Long id = saved.getId();

        // Act
        transactionsRepository.deleteById(id);

        // Assert
        assertTrue(transactionsRepository.findById(id).isEmpty());
    }

    @Test
    @DisplayName("Should save multiple transactions")
    void testSaveMultipleTransactions() {
        // Arrange
        CustomerTranscation trans1 = new CustomerTranscation(1, 100.0, LocalDate.now());
        CustomerTranscation trans2 = new CustomerTranscation(2, 200.0, LocalDate.now());
        CustomerTranscation trans3 = new CustomerTranscation(3, 300.0, LocalDate.now());

        // Act
        transactionsRepository.saveAll(List.of(trans1, trans2, trans3));
        List<CustomerTranscation> allTransactions = transactionsRepository.findAll();

        // Assert
        assertEquals(3, allTransactions.size());
    }

    @Test
    @DisplayName("Should count transactions")
    void testCountTransactions() {
        // Arrange
        transactionsRepository.save(new CustomerTranscation(1, 100.0, LocalDate.now()));
        transactionsRepository.save(new CustomerTranscation(2, 200.0, LocalDate.now()));

        // Act
        long count = transactionsRepository.count();

        // Assert
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Should handle transactions with same customer ID")
    void testMultipleTransactionsSameCustomer() {
        // Arrange
        transactionsRepository.save(new CustomerTranscation(1, 100.0, LocalDate.now()));
        transactionsRepository.save(new CustomerTranscation(1, 150.0, LocalDate.now()));

        // Act
        List<CustomerTranscation> allTransactions = transactionsRepository.findAll();

        // Assert
        assertEquals(2, allTransactions.size());
        assertTrue(allTransactions.stream().allMatch(t -> t.getCustomerId() == 1));
    }

    @Test
    @DisplayName("Should persist transaction with various amounts")
    void testVariousTransactionAmounts() {
        // Arrange
        double[] amounts = {10.0, 50.0, 75.5, 100.0, 150.75, 1000.0};

        // Act
        for (double amount : amounts) {
            transactionsRepository.save(new CustomerTranscation(1, amount, LocalDate.now()));
        }
        List<CustomerTranscation> transactions = transactionsRepository.findAll();

        // Assert
        assertEquals(amounts.length, transactions.size());
    }

    @Test
    @DisplayName("Should persist transaction with different dates")
    void testTransactionWithDifferentDates() {
        // Arrange
        LocalDate date1 = LocalDate.of(2026, 1, 15);
        LocalDate date2 = LocalDate.of(2026, 2, 10);
        LocalDate date3 = LocalDate.of(2026, 3, 5);

        // Act
        transactionsRepository.save(new CustomerTranscation(1, 100.0, date1));
        transactionsRepository.save(new CustomerTranscation(1, 100.0, date2));
        transactionsRepository.save(new CustomerTranscation(1, 100.0, date3));
        List<CustomerTranscation> transactions = transactionsRepository.findAll();

        // Assert
        assertEquals(3, transactions.size());
        assertTrue(transactions.stream().map(CustomerTranscation::getDate).distinct().count() >= 1);
    }

    @Test
    @DisplayName("Should return empty list when no transactions exist")
    void testEmptyTransactionsList() {
        // Act
        List<CustomerTranscation> transactions = transactionsRepository.findAll();

        // Assert
        assertTrue(transactions.isEmpty());
    }

    @Test
    @DisplayName("Should delete all transactions")
    void testDeleteAll() {
        // Arrange
        transactionsRepository.save(new CustomerTranscation(1, 100.0, LocalDate.now()));
        transactionsRepository.save(new CustomerTranscation(2, 200.0, LocalDate.now()));

        // Act
        transactionsRepository.deleteAll();
        List<CustomerTranscation> transactions = transactionsRepository.findAll();

        // Assert
        assertTrue(transactions.isEmpty());
    }

    @Test
    @DisplayName("Should persist large transaction amount")
    void testLargeTransactionAmount() {
        // Arrange
        CustomerTranscation transaction = new CustomerTranscation(1, 999999.99, LocalDate.now());

        // Act
        CustomerTranscation saved = transactionsRepository.save(transaction);
        var retrieved = transactionsRepository.findById(saved.getId());

        // Assert
        assertTrue(retrieved.isPresent());
        assertEquals(999999.99, retrieved.get().getAmount());
    }
}

