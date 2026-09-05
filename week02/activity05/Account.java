package week02.activity05;

public class Account {

    // ===== Constants =====

    private static final double MIN_BALANCE_SAVINGS = 500.0;
    private static final double MIN_BALANCE_CURRENT = 1000.0;

    private static final int MIN_AGE = 18;

    private static final int MIN_PIN = 1000;
    private static final int MAX_PIN = 9999;


    // ===== Fields =====

    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private Integer pin;


    // ===== Constructor =====

    public Account(
            int accountNumber,
            String name,
            int age,
            double initialBalance,
            String accountType)
            throws IllegalArgumentException {

        // Validate age
        if (age < MIN_AGE) {
            throw new IllegalArgumentException(
                    "Age must be at least " + MIN_AGE
            );
        }

        // Validate account type
        if (!accountType.equals("Savings")
                && !accountType.equals("Current")) {

            throw new IllegalArgumentException(
                    "Account type must be Savings or Current"
            );
        }

        // Validate minimum balance
        double minimumBalance;

        if (accountType.equals("Savings")) {
            minimumBalance = MIN_BALANCE_SAVINGS;
        } else {
            minimumBalance = MIN_BALANCE_CURRENT;
        }

        if (initialBalance < minimumBalance) {
            throw new IllegalArgumentException(
                    "Initial balance must be at least ₹"
                    + minimumBalance
            );
        }

        // Initialize fields
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = null;
    }


    // ===== Business Methods =====

    public void deposit(double amount)
            throws InvalidAmountException, InactiveAccountException {

        // Check if account is active
        validateActive();

        // Check if amount is positive
        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Deposit amount must be greater than zero"
            );
        }

        // Add amount to balance
        this.balance += amount;
    }


    public void withdraw(
            double amount,
            int pin)
            throws InvalidAmountException,
            InsufficientBalanceException,
            MinimumBalanceViolationException,
            InactiveAccountException,
            InvalidPinException {

        // Check if account is active
        validateActive();

        // Check if PIN is set
        if (!hasPin()) {
            throw new InvalidPinException(
                    "PIN is not set"
            );
        }

        // Verify PIN
        if (!verifyPin(pin)) {
            throw new InvalidPinException(
                    "Incorrect PIN"
            );
        }

        // Check if amount is positive
        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be greater than zero"
            );
        }

        // Check if sufficient balance
        if (amount > this.balance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance"
            );
        }

        // Check minimum balance after withdrawal
        double remainingBalance = this.balance - amount;

        if (remainingBalance < getMinimumBalance()) {
            throw new MinimumBalanceViolationException(
                    "Withdrawal would violate minimum balance of ₹"
                    + getMinimumBalance()
            );
        }

        // Deduct amount
        this.balance -= amount;
    }


    // ===== Account Status Management =====

    public void closeAccount()
            throws IllegalStateException {

        // Check if already closed
        if (this.status.equals("Inactive")) {
            throw new IllegalStateException(
                    "Account is already closed"
            );
        }

        // Set status to inactive
        this.status = "Inactive";
    }


    public void reopenAccount()
            throws IllegalStateException {

        // Check if already active
        if (this.status.equals("Active")) {
            throw new IllegalStateException(
                    "Account is already active"
            );
        }

        // Set status to active
        this.status = "Active";
    }


    // ===== PIN Management =====

    public void setPin(int pin)
            throws IllegalArgumentException {

        // Validate PIN
        if (pin < MIN_PIN || pin > MAX_PIN) {
            throw new IllegalArgumentException(
                    "PIN must be a 4-digit number"
            );
        }

        // Set PIN
        this.pin = pin;
    }


    public boolean verifyPin(int pin) {

        return this.pin != null
                && this.pin.equals(pin);
    }


    public boolean hasPin() {

        return this.pin != null;
    }


    // ===== Helper Methods =====

    private double getMinimumBalance() {

        if (this.accountType.equals("Savings")) {
            return MIN_BALANCE_SAVINGS;
        }

        return MIN_BALANCE_CURRENT;
    }


    private void validateActive()
            throws InactiveAccountException {

        if (this.status.equals("Inactive")) {
            throw new InactiveAccountException(
                    "Account is inactive"
            );
        }
    }


    // ===== Getters =====

    public int getAccountNumber() {
        return this.accountNumber;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public double getBalance() {
        return this.balance;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public String getStatus() {
        return this.status;
    }
}