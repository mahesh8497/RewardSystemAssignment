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

### Key Dependencies

| Dependency | Purpose |
|-----------|---------|
| `spring-boot-starter-data-jpa` | JPA for database operations and ORM |
| `spring-boot-starter-webmvc` | Web framework for REST APIs |
| `mysql-connector-j` | MySQL database driver |
| `spring-boot-starter-data-jpa-test` | Testing utilities for JPA |
| `spring-boot-starter-webmvc-test` | Testing utilities for Web MVC |

## Project Structure

```
rewardSystem/
├── src/
│   ├── main/
│   │   ├── java/com/rewardSystem/
│   │   │   ├── RewardSystemApplication.java          # Main Spring Boot Application
│   │   │   ├── controller/
│   │   │   │   └── RewardsController.java           # REST API endpoints
│   │   │   ├── entity/
│   │   │   │   ├── Customer.java                    # Customer entity
│   │   │   │   ├── Transactions.java                # Transactions entity
│   │   │   │   └── RewardResponse.java              # API response model
│   │   │   ├── repository/
│   │   │   │   └── TransactionsRepository.java      # Database access layer
│   │   │   └── service/
│   │   │       ├── RewardService.java               # Service interface
│   │   │       └── RewardServiceImpl.java            # Service implementation
│   │   └── resources/
│   │       └── application.properties               # Application configuration
│   └── test/
│       └── java/com/rewardSystem/
│           └── RewardSystemApplicationTests.java    # Unit tests
├── pom.xml                                          # Maven configuration
└── README.md                                        # This file
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

### Step 3: Update Database Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rewardsystem
spring.datasource.username=root
spring.datasource.password=root
```

Update the `username` and `password` with your MySQL credentials.

### Step 4: Build the Project

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

Run the unit tests using Maven:

```bash
mvn test
```

## Troubleshooting

### Database Connection Issues
- Ensure MySQL is running
- Verify database credentials in `application.properties`
- Check that the database `rewardsystem` exists

### Port Already in Use
- Change the port in `application.properties`:
```properties
server.port=8081
```

### Build Failures
- Clear Maven cache: `mvn clean`
- Ensure Java 21 is installed: `java -version`

## Development Notes

- The application uses **Spring Data JPA** for database operations
- Transactions are filtered to show only the last 3 months of data
- Reward points are aggregated by customer and month
- The API follows REST conventions with proper HTTP methods

## Future Enhancements

- Add customer endpoints (CRUD operations)
- Implement pagination for large datasets
- Add filtering by date range
- Implement customer dashboard UI
- Add reward redemption features
- Implement audit logging

## License

This project is open source and available under the MIT License.

## Support

For issues or questions, please create an issue in the repository or contact the development team.

---

**Last Updated**: February 2026
**Java Version**: 21
**Spring Boot Version**: 4.0.2

