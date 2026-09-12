package week03.activity07;

public class Account {

    // ===== Constants =====
    protected static final int MIN_AGE = 18;
    protected static final int MIN_PIN = 1000;
    protected static final int MAX_PIN = 9999;

    // ===== Fields =====
    protected int accountNumber;
    protected String name;
    protected int age;
    protected double balance;
    protected String accountType;
    protected String status;
    protected Integer pin;

    // ===== Constructors =====
    public Account() {
        this.status = "Active";
        this.pin = null;
    }

    public Account(int accountNumber, String name, int age, double balance, String accountType) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = balance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = null;
    }

    public Account(int accountNumber, String name, int age, double balance) {
        this(accountNumber, name, age, balance, "GENERAL");
    }

    // ===== Business Methods =====
    public boolean deposit(double amount) {
        if (amount <= 0 || !isActive()) {
            return false;
        }
        this.balance += amount;
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || !isActive() || amount > this.balance) {
            return false;
        }
        this.balance -= amount;
        return true;
    }

    public boolean withdraw(double amount, int pin) {
        if (!verifyPin(pin)) {
            return false;
        }
        return withdraw(amount);
    }

    // ===== PIN Management =====
    public boolean setPin(int pin) {
        if (pin < MIN_PIN || pin > MAX_PIN) {
            return false;
        }
        this.pin = pin;
        return true;
    }

    public boolean verifyPin(int pin) {
        return this.pin != null && this.pin.equals(pin);
    }

    public boolean hasPin() {
        return this.pin != null;
    }

    // ===== Status Management =====
    public boolean closeAccount() {
        if ("Inactive".equals(this.status)) {
            return false;
        }
        this.status = "Inactive";
        return true;
    }

    public boolean reopenAccount() {
        if ("Active".equals(this.status)) {
            return false;
        }
        this.status = "Active";
        return true;
    }

    public boolean isActive() {
        return "Active".equalsIgnoreCase(this.status);
    }

    // ===== Getters and Setters =====
    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPin() {
        return pin;
    }

    @Override
    public String toString() {
        return "Account #" + accountNumber + " | " + name + " (" + age + " yrs) | "
                + accountType + " | ₹" + balance + " | " + status
                + " | PIN: " + (hasPin() ? "Yes" : "No");
    }
}
