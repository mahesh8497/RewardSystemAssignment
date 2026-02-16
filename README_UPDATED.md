# Reward System - Spring Boot Application

## Overview
The Reward System is a Spring Boot REST API application that calculates and manages customer reward points based on their transactions. Customers earn reward points based on their purchase amounts, with higher rewards for larger purchases.

## Features Implemented

### Core Features
- ✅ **Calculate Customer Rewards**: Computes reward points based on transaction amounts over the last 3 months
- ✅ **Retrieve All Rewards**: Provides API endpoint to fetch reward information for all customers
- ✅ **Monthly Breakdown**: Shows reward points broken down by month for each customer
- ✅ **Flexible Point Calculation**: 
  - 2 points for every dollar spent over $100
  - 1 point for every dollar spent between $50 and $100

### Exception Handling & Logging
- ✅ **Centralized Exception Handling**: Using `@ControllerAdvice` for global exception handling
- ✅ **Custom Exception Classes**:
  - `ResourceNotFoundException` (404 Not Found)
  - `DataProcessingException` (400 Bad Request)
  - `InternalServerException` (500 Internal Server Error)
- ✅ **SLF4J Logging**: Comprehensive logging using SLF4J with Logback
- ✅ **Meaningful Error Responses**: Structured error response DTOs with timestamp, status, error message, and details
- ✅ **Logging Levels**: DEBUG, INFO, WARN, and ERROR logs at appropriate levels
- ✅ **File & Console Logging**: Logs written to both console and rotating log files

### Testing
- ✅ **Comprehensive Unit Tests**: 21+ test cases covering all scenarios
- ✅ **MockMvc Testing**: HTTP endpoint testing with MockMvc
- ✅ **Mock Services**: Using Mockito for service mocking
- ✅ **Test Profiles**: Separate application-test.properties for test environment

### Code Quality
- ✅ **JavaDoc Comments**: Detailed documentation for all public methods
- ✅ **RESTful API Design**: Following REST conventions with proper HTTP methods
- ✅ **Version Tracking**: API versioning with `/v1/api` prefix

## Technology Stack

### Core Technologies
- **Java Version**: 21
- **Spring Boot Version**: 3.2.2
- **Build Tool**: Maven
- **Database**: MySQL (Production), H2 (Testing)

### Key Dependencies
```xml
<!-- Spring Boot Web -->
spring-boot-starter-web (3.2.2)

<!-- Spring Data JPA -->
spring-boot-starter-data-jpa (3.2.2)

<!-- Database -->
mysql-connector-j (Runtime)
h2 (Test scope)

<!-- Testing -->
spring-boot-starter-test (JUnit 5, Mockito, AssertJ)
junit-jupiter (5.10.0)

<!-- Logging -->
Logback (Included with Spring Boot)
SLF4J (Included with Spring Boot)
```

### Maven Compiler Configuration
- **Source**: Java 21
- **Target**: Java 21
- **Encoding**: UTF-8

## Project Structure

```
rewardSystem/
├── src/
│   ├── main/
│   │   ├── java/com/rewardSystem/
│   │   │   ├── RewardSystemApplication.java       (Main Application)
│   │   │   ├── controller/
│   │   │   │   └── RewardsController.java         (REST Controller)
│   │   │   ├── service/
│   │   │   │   ├── RewardService.java             (Service Interface)
│   │   │   │   └── RewardServiceImpl.java          (Service Implementation)
│   │   │   ├── repository/
│   │   │   │   └── TransactionsRepository.java    (Data Access Layer)
│   │   │   ├── entity/
│   │   │   │   ├── Customer.java                  (Entity)
│   │   │   │   ├── CustomerTranscation.java       (Entity)
│   │   │   │   └── RewardPoints.java              (DTO)
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java    (Exception Handler)
│   │   │   │   ├── ResourceNotFoundException.java (Custom Exception)
│   │   │   │   ├── DataProcessingException.java   (Custom Exception)
│   │   │   │   └── InternalServerException.java   (Custom Exception)
│   │   │   └── dto/
│   │   │       └── ErrorResponse.java             (Error Response DTO)
│   │   └── resources/
│   │       ├── application.properties             (Main Configuration)
│   │       └── logback-spring.xml                 (Logging Configuration)
│   └── test/
│       ├── java/com/rewardSystem/
│       │   ├── controller/
│       │   │   └── RewardsControllerTest.java     (Controller Tests)
│       │   ├── service/
│       │   │   └── RewardServiceImplTest.java     (Service Tests)
│       │   └── repository/
│       │       └── TransactionsRepositoryTest.java (Repository Tests)
│       └── resources/
│           └── application-test.properties        (Test Configuration)
├── pom.xml                                        (Maven Configuration)
└── README.md                                      (This File)
```

## Build Instructions

### Prerequisites
- Java 21 or higher
- Maven 3.6.0 or higher
- MySQL Server (for production) or H2 (for testing)

### Build Steps

1. **Clone the Repository**
```bash
cd C:\Users\Mahesh\Downloads\rewardSystem\rewardSystem
```

2. **Build the Application**
```bash
mvn clean install
```

3. **Build Without Running Tests**
```bash
mvn clean install -DskipTests
```

4. **Build Specific Module**
```bash
mvn clean package
```

## Run Instructions

### Running the Application

1. **From Command Line (Maven)**
```bash
# Development Environment
mvn spring-boot:run

# With Specific Profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

2. **From Command Line (Java)**
```bash
java -jar target/rewardSystem-0.0.1-SNAPSHOT.jar
```

3. **From IDE**
- Right-click on `RewardSystemApplication.java` → Run

### Default Configuration
- **Server Port**: 8080 (configurable in application.properties)
- **Context Path**: /
- **Database**: MySQL (configure in application.properties)

## API Endpoints

### Get All Rewards
**Endpoint**: `GET /v1/api/rewards`

**Description**: Retrieves reward information for all customers based on transactions from the last 3 months

**Request**:
```bash
curl -X GET http://localhost:8080/v1/api/rewards
```

**Response** (200 OK):
```json
[
  {
    "customerId": 1,
    "monthlyRewards": {
      "JANUARY": 100,
      "FEBRUARY": 50,
      "MARCH": 75
    },
    "totalRewardPoints": 225
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

**Error Response** (500 Internal Server Error):
```json
{
  "timestamp": "2024-02-16T10:30:45.123456",
  "status": 500,
  "error": "INTERNAL_SERVER_ERROR",
  "message": "An unexpected error occurred while processing your request",
  "path": "/v1/api/rewards",
  "details": "Connection timeout"
}
```

## Configuration

### Application Properties (application.properties)

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/reward_system
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# Logging Configuration
logging.level.root=INFO
logging.level.com.rewardSystem=DEBUG
logging.file.name=logs/reward-system.log
logging.file.max-size=10MB
logging.file.max-history=30
```

### Test Configuration (application-test.properties)

```properties
# H2 In-Memory Database for Testing
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop

# Logging for Tests
logging.level.com.rewardSystem=DEBUG
```

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=RewardsControllerTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=RewardsControllerTest#testGetAllRewardsReturnsOk
```

### Generate Test Report
```bash
mvn surefire-report:report
```

## Logging

### Log Files
- **Main Log**: `logs/reward-system.log`
- **Error Log**: `logs/reward-system-error.log`
- **Archived Logs**: `logs/reward-system-{date}.{index}.log`

### Log Levels
- **TRACE**: Most detailed level, rarely used in production
- **DEBUG**: Detailed debugging information (used in development)
- **INFO**: General informational messages
- **WARN**: Warning messages for potentially harmful situations
- **ERROR**: Error messages with full exception stack trace

### Log Configuration Profiles
- **dev**: DEBUG level logging for development
- **prod**: INFO level logging for production
- **test**: DEBUG level with suppressed framework logs for testing

### Viewing Logs in Real-Time
```bash
# On Unix/Linux/Mac
tail -f logs/reward-system.log

# On Windows PowerShell
Get-Content logs/reward-system.log -Wait
```

## Exception Handling

### Exception Flow
1. **Request** → Controller → Service
2. **Exception Thrown** → GlobalExceptionHandler catches it
3. **Error Response** → Formatted JSON response with details
4. **Response** → HTTP Status + Error Details

### Handled Exceptions

| Exception | HTTP Status | Description |
|-----------|-------------|-------------|
| ResourceNotFoundException | 404 | Resource not found |
| DataProcessingException | 400 | Bad request / Data processing error |
| InternalServerException | 500 | Unexpected server error |
| IllegalArgumentException | 400 | Invalid argument |
| NullPointerException | 500 | Null pointer error |
| Generic Exception | 500 | Unhandled exceptions |

## Performance Considerations

1. **Transaction Filtering**: Only retrieves last 3 months of transactions
2. **Streaming API**: Uses Java Streams for memory-efficient processing
3. **Pagination**: Can be added for large datasets
4. **Caching**: Can be implemented for frequently accessed rewards
5. **Connection Pooling**: Configured through Spring Data JPA

## Security Considerations

1. **Input Validation**: Amount validation (non-negative)
2. **Exception Safety**: No sensitive data in error messages
3. **Logging**: Sensitive data should not be logged
4. **CORS**: Can be configured in SecurityConfig
5. **Authentication**: Can be added using Spring Security

## Troubleshooting

### Issue: Database Connection Failed
**Solution**: 
- Check MySQL service is running
- Verify database URL, username, password in application.properties
- Ensure database exists

### Issue: Port Already in Use
**Solution**:
```bash
# Change port in application.properties
server.port=8081
```

### Issue: Tests Failing
**Solution**:
- Clear Maven cache: `mvn clean`
- Rebuild project: `mvn clean install`
- Check H2 database configuration in application-test.properties

### Issue: No Logs Appearing
**Solution**:
- Check logback-spring.xml configuration
- Verify logging.level properties in application.properties
- Check logs/ directory for log files

## Additional Resources

### Documentation
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Logback Documentation](http://logback.qos.ch/documentation.html)
- [SLF4J Documentation](http://www.slf4j.org/manual.html)

### Maven Commands
- `mvn clean`: Remove build directory
- `mvn install`: Build and install package
- `mvn package`: Create JAR file
- `mvn test`: Run unit tests
- `mvn compile`: Compile source code

## Future Enhancements

1. **API Enhancements**
   - Add filtering by customer ID
   - Add date range filtering
   - Implement pagination

2. **Performance**
   - Implement caching (Redis)
   - Add database indexing
   - Query optimization

3. **Security**
   - Add Spring Security
   - Implement JWT authentication
   - Add role-based access control

4. **Monitoring**
   - Add Spring Boot Actuator
   - Implement health checks
   - Add metrics collection

5. **Documentation**
   - Add Swagger/OpenAPI documentation
   - Generate API documentation

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Commit your changes
6. Push to the branch
7. Create a Pull Request

## License

This project is licensed under the MIT License.

## Author

Mahesh - Reward System Project

## Support

For issues, questions, or suggestions, please create an issue in the repository.

---

**Last Updated**: February 2024
**Application Version**: 0.0.1-SNAPSHOT
**Spring Boot Version**: 3.2.2
**Java Version**: 21

