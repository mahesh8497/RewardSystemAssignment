# Reward System

A Spring Boot application that calculates and manages customer reward points based on their transaction history. The system analyzes purchases over a 3-month period and awards points according to a tiered reward structure.

## Implemented Features

- **Reward Points Calculation**: Automatically calculates reward points for customer transactions with a tiered reward structure:
  - For amounts > $100: 2 points for every dollar spent above $100
  - For amounts > $50 and up to $100: 1 point for every dollar spent between $50 and $100
  
- **Transaction Management**: Store and manage customer transactions with amount and date information

- **Reward Reports**: Generate comprehensive reward reports showing:
  - Monthly reward points breakdown
  - Total accumulated reward points
  - Customer-wise reward summary

- **REST API**: Exposed endpoints to retrieve reward information via HTTP

- **Database Persistence**: MySQL database integration for storing transactions and customer data

## Technology Stack

### Java & Spring Boot
- **Java Version**: 21
- **Spring Boot Version**: 4.0.2
- **Build Tool**: Maven

                                   # This file
```

## Prerequisites

Before running the application, ensure you have the following installed:

- **Java 21** or higher
- **Maven 3.6+**
- **MySQL 8.0+**

## Installation & Setup

### Step 1: Clone or Download the Project
```bash
cd rewardSystem
```

### Step 2: Configure MySQL Database

1. Start your MySQL server
2. Create a new database:
```sql
CREATE DATABASE rewardsystem;
```

3. Create the transactions table (JPA will auto-generate with ddl-auto=update):
```sql
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    amount DOUBLE NOT NULL,
    date DATE NOT NULL
);
```




### Step 3: Build the Project

Run the following Maven command to build the project:

```bash
mvn clean build
```

Or on Windows:

```cmd
mvn clean build
```

## Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

Or on Windows:

```cmd
mvn spring-boot:run
```

### Using Java

```bash
java -jar target/rewardSystem-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### Get All Rewards

**Endpoint**: `GET /v1/api/rewards`

**Description**: Retrieves reward points for all customers based on their transactions from the last 3 months.

**Response Example**:
```json
[
  {
    "customerId": 1,
    "monthlyRewards": {
      "JANUARY": 150,
      "FEBRUARY": 100
    },
    "totalRewardPoints": 250
  },
  {
    "customerId": 2,
    "monthlyRewards": {
      "JANUARY": 75
    },
    "totalRewardPoints": 75
  }
]
```

## Database Configuration Details

The application uses the following database configuration (in `application.properties`):

- **URL**: `jdbc:mysql://localhost:3306/rewardsystem`
- **Username**: `root`
- **Password**: `root`
- **JPA DDL Strategy**: `update` (automatically creates/updates tables)
- **SQL Logging**: Enabled (`show-sql=true`)

## Example Reward Calculation

The reward points are calculated based on the transaction amount:

| Transaction Amount | Calculation | Points |
|------------------|------------|--------|
| $50 | No points | 0 |
| $75 | ($75 - $50) × 1 | 25 |
| $120 | ($100 - $50) × 1 + ($120 - $100) × 2 = 50 + 40 | 90 |
| $150 | ($100 - $50) × 1 + ($150 - $100) × 2 = 50 + 100 | 150 |

## Testing

The application includes comprehensive unit, integration, and entity tests covering all layers of the application.

### Test Suite Overview

**Total Test Cases:** 120+

The test suite includes:
- **Service Layer Tests (15 tests)** - Reward calculation logic, data aggregation, and business rules
- **Controller Layer Tests (20 tests)** - HTTP endpoint validation, response format, and error handling
- **Entity Tests (25 tests)** - Object constructors, getters/setters, and data type handling
- **Repository Tests (14 tests)** - CRUD operations, database persistence, and query operations
- **Integration Tests (10 tests)** - End-to-end workflows and multi-layer interaction
- **Application Tests (7 tests)** - Spring Boot configuration and bean management

### Running Tests

**Run all tests:**
```bash
./mvnw clean test
```

**Run specific test class:**
```bash
./mvnw test -Dtest=RewardServiceImplTest
```

**Run specific test method:**
```bash
./mvnw test -Dtest=RewardServiceImplTest#testMultipleCustomersSeparateRewards
```

**Generate test coverage report:**
```bash
./mvnw clean test jacoco:report
```

### Test Coverage Details

For detailed information about all test cases, see [TEST_DOCUMENTATION.md](./TEST_DOCUMENTATION.md)

### Test Framework & Tools
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework
- **Spring Test** - Spring Boot integration testing
- **H2 Database** - In-memory database for tests

### Key Testing Patterns

1. **AAA Pattern (Arrange-Act-Assert)** - Clear test structure
2. **Mocking** - Isolated unit tests
3. **Boundary Testing** - Edge case validation
4. **Integration Testing** - Full workflow validation
5. **Data-Driven Testing** - Multiple scenarios covered

---

## Troubleshooting

### Database Connection Issues
- Ensure MySQL is running
- Verify database credentials in `application.properties`
- Check that the database `rewardsystem` exists

### Port Already in Use
- Change the port in `application.properties`:
```properties
server.port=8080
```

### Build Failures
- Clear Maven cache: `mvn clean`
- Ensure Java 21 is installed: `java -version`

## Development Notes

- The application uses **Spring Data JPA** for database operations
- Transactions are filtered to show only the last 3 months of data
- Reward points are aggregated by customer and month
- The API follows REST conventions with proper HTTP methods



**Last Updated**: February 2026
**Java Version**: 21
**Spring Boot Version**: 4.0.2
