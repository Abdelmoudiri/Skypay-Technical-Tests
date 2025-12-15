# Banking Service - Technical Test 1

A robust banking system implementation in Java that provides core banking functionality including deposits, withdrawals, and transaction statements.

## Overview

This project implements a banking service with the following features:
- **Deposit money** into accounts
- **Withdraw money** from accounts (with balance validation)
- **Print transaction statements** in reverse chronological order
- **Comprehensive error handling** for invalid operations
- **Account management service** for multiple accounts and transfers
- **Thread-safe operations** using synchronized methods

## Project Structure

```
Test1/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── org/
│   │           └── skypay/
│   │               ├── Account.java          # Core account class
│   │               ├── AccountService.java   # Service for managing multiple accounts
│   │               ├── Transaction.java      # Transaction entity
│   │               └── Main.java             # Demo application
│   └── test/
│       └── java/
│           └── org/
│               └── skypay/
│                   ├── AccountTest.java          # Account unit tests (26 tests)
│                   ├── AccountServiceTest.java   # Service unit tests (16 tests)
│                   └── TransactionTest.java      # Transaction unit tests (9 tests)
└── pom.xml
```

## Features

### 1. Account Class

The `Account` class provides the public interface for banking operations:

```java
public void deposit(int amount, LocalDate date)
public void withdraw(int amount, LocalDate date)
public void printStatement()
```

#### Key Features:
- **Balance Management**: Maintains accurate account balance
- **Transaction History**: Stores all transactions using ArrayList
- **Error Handling**: 
  - Validates positive amounts
  - Prevents withdrawals exceeding balance
  - Validates non-null dates
- **Thread Safety**: All methods are synchronized
- **Statement Formatting**: Displays transactions in reverse chronological order

### 2. Transaction Class

Represents individual banking transactions with:
- Date of transaction
- Amount (positive integer)
- Transaction type (DEPOSIT/WITHDRAWAL)
- Balance after transaction
- Formatted statement output

### 3. AccountService Class

Provides higher-level banking operations:
- Create and manage multiple accounts
- Transfer money between accounts
- Account lookup and management
- Atomic transfer operations

## Acceptance Test

The implementation satisfies the following acceptance criteria:

**Given:**
- A client makes a deposit of 1000 on 10-01-2012
- And a deposit of 2000 on 13-01-2012
- And a withdrawal of 500 on 14-01-2012

**When:**
- They print their bank statement

**Then:**
- They see:
```
date || amount || balance
14-01-2012 || -500.00 || 2500.00
13-01-2012 || 2000.00 || 3000.00
10-01-2012 || 1000.00 || 1000.00
```

## Technical Requirements Compliance

✅ **Exception Handling**: Comprehensive validation for invalid inputs
- Negative/zero amounts
- Insufficient funds
- Null dates
- Duplicate account numbers
- Invalid transfers

✅ **Performance**: Efficient operations with O(1) deposits/withdrawals
- ArrayList for transaction storage
- Test verified: 1000 transactions complete in < 1 second

✅ **Testing**: 51 comprehensive unit tests covering:
- Normal operations
- Edge cases
- Error conditions
- Performance benchmarks

✅ **Data Structure**: Uses ArrayList as specified (not persistent storage)

✅ **Money Representation**: Uses int type for amounts as instructed

✅ **Simplicity**: Clean, maintainable code following SOLID principles

## Building and Running

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Compile the Project
```bash
cd Test1
mvn clean compile
```

### Run the Demo
```bash
mvn exec:java -Dexec.mainClass="org.skypay.Main"
```

### Run Tests
```bash
mvn test
```

Expected output:
```
Tests run: 51, Failures: 0, Errors: 0, Skipped: 0
```

## Usage Examples

### Basic Account Operations

```java
// Create an account
Account account = new Account();

// Make deposits
account.deposit(1000, LocalDate.of(2012, 1, 10));
account.deposit(2000, LocalDate.of(2012, 1, 13));

// Make a withdrawal
account.withdraw(500, LocalDate.of(2012, 1, 14));

// Print statement
account.printStatement();
```

### Using AccountService

```java
// Create service
AccountService service = new AccountService();

// Create accounts
Account acc1 = service.createAccount("ACC001");
Account acc2 = service.createAccount("ACC002");

// Deposit to first account
acc1.deposit(5000, LocalDate.now());

// Transfer between accounts
service.transfer("ACC001", "ACC002", 2000, LocalDate.now());

// Check balances
System.out.println("ACC001: " + acc1.getBalance()); // 3000
System.out.println("ACC002: " + acc2.getBalance()); // 2000
```

## Error Handling Examples

```java
Account account = new Account();
account.deposit(1000, LocalDate.now());

// Negative amount
try {
    account.deposit(-100, LocalDate.now());
} catch (IllegalArgumentException e) {
    // "Amount must be positive. Received: -100"
}

// Insufficient funds
try {
    account.withdraw(2000, LocalDate.now());
} catch (IllegalArgumentException e) {
    // "Insufficient funds. Balance: 1000, Withdrawal amount: 2000"
}

// Null date
try {
    account.deposit(100, null);
} catch (IllegalArgumentException e) {
    // "Date cannot be null"
}
```

## Design Decisions

### 1. Thread Safety
- All Account methods are synchronized to prevent race conditions
- AccountService uses synchronized blocks for atomic transfers

### 2. Immutability
- Transaction objects are immutable
- Balance is only modified through public methods

### 3. Separation of Concerns
- **Account**: Core banking operations
- **Transaction**: Data representation
- **AccountService**: Multi-account management
- **Main**: Demonstration and examples

### 4. Error Handling Strategy
- Fail-fast with IllegalArgumentException
- Descriptive error messages
- No silent failures

### 5. Transaction Storage
- ArrayList for O(1) append operations
- Preserves insertion order
- Reverse iteration for statement printing

## Test Coverage

### AccountTest (26 tests)
- Balance operations
- Transaction history
- Error conditions
- Performance testing
- Acceptance test scenario

### AccountServiceTest (16 tests)
- Account creation
- Multiple account management
- Transfer operations
- Error handling

### TransactionTest (9 tests)
- Transaction creation
- Statement formatting
- Date handling
- Edge cases

## Dependencies

```xml
<dependencies>
    <!-- JUnit 5 for testing -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-params</artifactId>
        <version>5.10.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Future Enhancements (Out of Scope)

While not required for this test, real-world improvements could include:
- Persistent storage (database)
- BigDecimal for precise money calculations
- Account interest calculations
- Transaction reversal/correction
- Multi-currency support
- Audit logging
- RESTful API endpoints

## Author

Implementation for Skypay Technical Test
Date: December 2025

## License

See LICENSE file in the project root.
