# Exception Handling Documentation

## Overview

This document provides comprehensive information about the exception handling and logging implementation in the Reward System application. The application uses centralized exception handling with `@ControllerAdvice`, SLF4J logging with Logback, and provides meaningful error responses.

## Architecture

### Exception Handling Flow

```
Request
    ↓
Controller / Service
    ↓
Exception Thrown
    ↓
GlobalExceptionHandler (@ControllerAdvice)
    ↓
ErrorResponse DTO
    ↓
HTTP Response (with status code + JSON error details)
```

## Custom Exceptions

### 1. ResourceNotFoundException

**Purpose**: Thrown when a requested resource is not found.

**HTTP Status**: 404 Not Found

**Usage Example**:
```java
public RewardPoints getRewardById(int customerId) {
    RewardPoints reward = rewardRepository.findById(customerId);
    if (reward == null) {
        throw new ResourceNotFoundException("Reward for customer " + customerId + " not found");
    }
    return reward;
}
```

**Error Response**:
```json
{
  "timestamp": "2024-02-16T10:30:45.123456",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Reward for customer 123 not found",
  "path": "/v1/api/rewards/123",
  "details": null
}
```

### 2. DataProcessingException

**Purpose**: Thrown when data processing fails (validation errors, calculation errors, etc.).

**HTTP Status**: 400 Bad Request

**Usage Example**:
```java
private int calculatePoints(double amount) {
    if (amount < 0) {
        throw new IllegalArgumentException("Transaction amount cannot be negative");
    }
    // ... calculation logic
}
```

**Error Response**:
```json
{
  "timestamp": "2024-02-16T10:30:45.123456",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Error processing transaction for customer 1",
  "path": "/v1/api/rewards",
  "details": null
}
```

### 3. InternalServerException

**Purpose**: Thrown for unexpected internal server errors (database connection failures, etc.).

**HTTP Status**: 500 Internal Server Error

**Usage Example**:
```java
public List<RewardPoints> findAllRewards() {
    try {
        return transactionsRepository.findAll();
    } catch (Exception e) {
        logger.error("Database error", e);
        throw new InternalServerException("An error occurred while processing your request", e);
    }
}
```

**Error Response**:
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

## GlobalExceptionHandler

The `GlobalExceptionHandler` class uses Spring's `@ControllerAdvice` annotation to provide centralized exception handling across the entire application.

### Handler Methods

#### 1. handleResourceNotFoundException
```java
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
        ResourceNotFoundException ex,
        WebRequest request)
```
- **HTTP Status**: 404 Not Found
- **Error Code**: NOT_FOUND
- **Logging Level**: WARN

#### 2. handleDataProcessingException
```java
@ExceptionHandler(DataProcessingException.class)
public ResponseEntity<ErrorResponse> handleDataProcessingException(
        DataProcessingException ex,
        WebRequest request)
```
- **HTTP Status**: 400 Bad Request
- **Error Code**: BAD_REQUEST
- **Logging Level**: ERROR

#### 3. handleInternalServerException
```java
@ExceptionHandler(InternalServerException.class)
public ResponseEntity<ErrorResponse> handleInternalServerException(
        InternalServerException ex,
        WebRequest request)
```
- **HTTP Status**: 500 Internal Server Error
- **Error Code**: INTERNAL_SERVER_ERROR
- **Logging Level**: ERROR

#### 4. handleIllegalArgumentException
```java
@ExceptionHandler(IllegalArgumentException.class)
public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
        IllegalArgumentException ex,
        WebRequest request)
```
- **HTTP Status**: 400 Bad Request
- **Error Code**: BAD_REQUEST
- **Logging Level**: ERROR

#### 5. handleNullPointerException
```java
@ExceptionHandler(NullPointerException.class)
public ResponseEntity<ErrorResponse> handleNullPointerException(
        NullPointerException ex,
        WebRequest request)
```
- **HTTP Status**: 500 Internal Server Error
- **Error Code**: INTERNAL_SERVER_ERROR
- **Logging Level**: ERROR

#### 6. handleGenericException (Catch-all)
```java
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorResponse> handleGenericException(
        Exception ex,
        WebRequest request)
```
- **HTTP Status**: 500 Internal Server Error
- **Error Code**: INTERNAL_SERVER_ERROR
- **Logging Level**: ERROR

## ErrorResponse DTO

The `ErrorResponse` class provides a standardized structure for all error responses.

### Fields

| Field | Type | Description |
|-------|------|-------------|
| timestamp | LocalDateTime | Time when the error occurred |
| status | int | HTTP status code (400, 404, 500, etc.) |
| error | String | Error type/code (BAD_REQUEST, NOT_FOUND, etc.) |
| message | String | Human-readable error message |
| path | String | The API endpoint that caused the error |
| details | String | Additional error details (optional) |

### Constructors

```java
// Default constructor
public ErrorResponse()

// Constructor with basic error info
public ErrorResponse(int status, String error, String message, String path)

// Constructor with details
public ErrorResponse(int status, String error, String message, String path, String details)
```

### Example JSON Structure

```json
{
  "timestamp": "2024-02-16T10:30:45.123456789",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Invalid transaction amount",
  "path": "/v1/api/rewards",
  "details": "Transaction amount cannot be negative"
}
```

## Logging Configuration

### Logback Configuration File

The `logback-spring.xml` file provides comprehensive logging configuration:

```xml
<!-- Console Output -->
- Pattern: [timestamp] [thread] [level] [logger] - message

<!-- File Output -->
- Main log: logs/reward-system.log
- Error log: logs/reward-system-error.log
- Rolling policy: 10MB per file, max 30 days history, 1GB total cap

<!-- Application Loggers -->
- com.rewardSystem: DEBUG level
- org.springframework: INFO level
- org.hibernate: INFO level
```

### Log Levels

| Level | Description | Used For |
|-------|-------------|----------|
| TRACE | Most detailed | Very detailed debugging (rarely used) |
| DEBUG | Detailed info | Development debugging, flow tracing |
| INFO | General info | Important business events, successes |
| WARN | Warnings | Potentially problematic situations |
| ERROR | Errors | Error conditions, stack traces |

### Logging in Controllers

```java
private static final Logger logger = LoggerFactory.getLogger(RewardsController.class);

@GetMapping("/rewards")
public List<RewardPoints> getAllRewards() {
    logger.info("Fetching all rewards for customers");
    try {
        List<RewardPoints> rewards = rewardService.findAllRewards();
        logger.info("Successfully retrieved rewards. Total records: {}", rewards.size());
        return rewards;
    } catch (Exception e) {
        logger.error("Error occurred while fetching all rewards", e);
        throw e;
    }
}
```

### Logging in Services

```java
@Override
public List<RewardPoints> findAllRewards() {
    logger.debug("Starting findAllRewards operation");
    try {
        // ... business logic
        logger.info("Successfully calculated rewards for {} customers", rewards.size());
        return rewards;
    } catch (Exception e) {
        logger.error("Unexpected error occurred while fetching all rewards", e);
        throw new InternalServerException("An unexpected error occurred", e);
    }
}
```

## Error Handling Best Practices

### 1. Always Log Exceptions

```java
// Good
catch (Exception e) {
    logger.error("Error processing transaction", e);
    throw new DataProcessingException("Failed to process transaction", e);
}

// Bad - Silent catch
catch (Exception e) {
    // Do nothing
}
```

### 2. Provide Context in Error Messages

```java
// Good - Specific message with context
throw new ResourceNotFoundException(
    "Customer with ID " + customerId + " not found"
);

// Bad - Generic message
throw new ResourceNotFoundException("Not found");
```

### 3. Use Appropriate Exception Types

```java
// Good - Specific exception
if (customerId < 0) {
    throw new DataProcessingException("Invalid customer ID");
}

// Bad - Generic exception
throw new Exception("Error");
```

### 4. Include Meaningful Details

```java
// Good - Full context
throw new InternalServerException(
    "Database connection failed while retrieving customer rewards",
    databaseException
);

// Bad - No context
throw new InternalServerException("Error");
```

## Testing Exception Handling

### Unit Tests for Exceptions

```java
@Test
@DisplayName("Should handle ResourceNotFoundException with 404 status")
void testHandleResourceNotFoundException() {
    // Arrange
    ResourceNotFoundException exception = new ResourceNotFoundException("Not found");
    
    // Act
    ResponseEntity<ErrorResponse> response = 
        exceptionHandler.handleResourceNotFoundException(exception, webRequest);
    
    // Assert
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals(404, response.getBody().getStatus());
    assertEquals("NOT_FOUND", response.getBody().getError());
}
```

### Integration Tests for API Endpoints

```java
@Test
@DisplayName("Should return 404 when customer not found")
void testGetRewardsNotFound() throws Exception {
    // Arrange
    when(rewardService.findAllRewards()).thenThrow(
        new ResourceNotFoundException("No rewards found")
    );
    
    // Act & Assert
    mockMvc.perform(get("/v1/api/rewards"))
           .andExpect(status().isNotFound())
           .andExpect(jsonPath("$.status").value(404))
           .andExpect(jsonPath("$.error").value("NOT_FOUND"));
}
```

## Log File Locations

### Development Environment
- **Main Log**: `logs/reward-system.log`
- **Error Log**: `logs/reward-system-error.log`

### Log Rotation
- **Max File Size**: 10MB
- **Max History**: 30 days
- **Total Cap**: 1GB

### Viewing Logs

**Windows PowerShell**:
```powershell
# Real-time log viewing
Get-Content logs/reward-system.log -Wait

# Last 50 lines
Get-Content logs/reward-system.log -Tail 50
```

**Linux/Mac**:
```bash
# Real-time log viewing
tail -f logs/reward-system.log

# Last 50 lines
tail -50 logs/reward-system.log

# Search for errors
grep "ERROR" logs/reward-system-error.log
```

## Common Scenarios

### Scenario 1: Database Connection Failure

**Flow**:
1. Service attempts to access database
2. Database connection exception occurs
3. Service catches exception and throws `InternalServerException`
4. GlobalExceptionHandler catches it
5. Returns 500 with error details

**Example Response**:
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

**Log Entry**:
```
2024-02-16 10:30:45.123 [http-nio-8080-exec-1] ERROR c.r.service.RewardServiceImpl - Unexpected error occurred while fetching all rewards
java.sql.SQLException: Connection timeout
```

### Scenario 2: Invalid Input Data

**Flow**:
1. User provides negative transaction amount
2. Service validates amount
3. Throws `DataProcessingException`
4. GlobalExceptionHandler catches it
5. Returns 400 with error details

**Example Response**:
```json
{
  "timestamp": "2024-02-16T10:30:45.123456",
  "status": 400,
  "error": "BAD_REQUEST",
  "message": "Error calculating reward points",
  "path": "/v1/api/rewards",
  "details": null
}
```

**Log Entry**:
```
2024-02-16 10:30:45.456 [http-nio-8080-exec-1] ERROR c.r.service.RewardServiceImpl - Invalid amount for point calculation: -50
```

### Scenario 3: Resource Not Found

**Flow**:
1. User requests specific customer rewards
2. Customer not found in database
3. Service throws `ResourceNotFoundException`
4. GlobalExceptionHandler catches it
5. Returns 404 with error details

**Example Response**:
```json
{
  "timestamp": "2024-02-16T10:30:45.123456",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Customer with ID 999 not found",
  "path": "/v1/api/rewards/999",
  "details": null
}
```

**Log Entry**:
```
2024-02-16 10:30:45.789 [http-nio-8080-exec-1] WARN c.r.exception.GlobalExceptionHandler - Resource not found: Customer with ID 999 not found
```

## Performance Considerations

1. **Logging Performance**: Use appropriate log levels to avoid excessive logging in production
2. **Exception Creation**: Custom exceptions are lightweight and efficient
3. **Error Response Generation**: ErrorResponse objects are quickly serialized to JSON
4. **Thread Safety**: All exception handlers are thread-safe

## Security Considerations

1. **No Sensitive Data in Errors**: Error messages don't expose internal implementation details
2. **Stack Traces**: Only logged internally, not returned to clients
3. **SQL Injection Protection**: Input validation before processing
4. **XSS Protection**: Error messages are properly encoded before serialization

## Troubleshooting

### Issue: Logs not appearing in console

**Solution**:
1. Check `logback-spring.xml` has CONSOLE appender defined
2. Verify logging level allows the message to be logged
3. Check `application.properties` for log level configuration

### Issue: Log files growing too large

**Solution**:
1. Verify rolling policy is configured (10MB in logback-spring.xml)
2. Check log level is not set to TRACE or DEBUG in production
3. Implement log archiving strategy

### Issue: Too many log files

**Solution**:
1. Adjust `maxHistory` in rolling policy (default: 30 days)
2. Adjust `totalSizeCap` in rolling policy (default: 1GB)
3. Implement log cleanup script

## Future Enhancements

1. **Structured Logging**: Implement JSON-based logging for better log aggregation
2. **Distributed Tracing**: Add correlation IDs for request tracing
3. **Alerting**: Implement automated alerts for ERROR and CRITICAL logs
4. **Metrics**: Add metrics collection for exception rates
5. **Custom Exception Codes**: Implement detailed error code system for API clients

## References

- [Spring Boot Exception Handling](https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc)
- [SLF4J Documentation](http://www.slf4j.org/manual.html)
- [Logback Configuration](http://logback.qos.ch/configuration.html)
- [REST API Error Handling Best Practices](https://www.rfc-editor.org/rfc/rfc7231)

---

**Last Updated**: February 2024
**Version**: 1.0

