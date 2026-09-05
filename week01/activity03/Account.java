package week01.activity03;

public class Account {

    // Constants
    final static int MIN_BALANCE_SAVINGS = 500;
    final static int MIN_BALANCE_CURRENT = 1000;

    // Attributes
    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
    private Integer pin = null;

    Account(
            int accountNumber,
            String name,
            int age,
            double initialBalance,
            String accountType) {

        // validate the age and set age 
        if (!isValidAge(age)) {
            this.age = 18;
        } else {
            this.age = age;
        }

        // validate account type and set account type
        if (!isValidAccountType(accountType)) {
            this.accountType = "Savings";
        } else {
            this.accountType = accountType;
        }

        // validate minimum balance and set balance

        if (this.accountType.equals("Savings") && initialBalance < MIN_BALANCE_SAVINGS) {
            this.balance = MIN_BALANCE_SAVINGS;
        } else if (this.accountType.equals("Current") && initialBalance < MIN_BALANCE_CURRENT) {
            this.balance = MIN_BALANCE_CURRENT;
        } else {
            this.balance = initialBalance;
        }

        this.accountNumber = accountNumber;
        this.name = name;
        this.status = "Active";
    }

    // ------------ HELPER FUNCTIONS ---------------

    private boolean isValidAge(int age) {
        return age >= 18;
    }

    private boolean isValidAccountType(String accountType) {
        return (accountType.equals("Savings") || accountType.equals("Current"));
    }


    // ----------- METHODS --------------   

    public boolean verifyPin(int pin) {
        return this.pin != null && this.pin.equals(pin);
    }

    public boolean hasPin() {
        return this.pin != null;
    }
    
    public boolean deposit(double amount) {
        if (this.status.equals("Inactive") || amount <= 0) {
            return false;
        }

        this.balance += amount;
        return true;

    }

    public boolean withdraw(double amount, int pin) {
        if (this.status.equals("Inactive")
                || amount > this.balance
                || amount <= 0
                || (accountType.equals("Savings") 
                    && this.balance - amount < MIN_BALANCE_SAVINGS
                || accountType.equals("Current") 
                    && this.balance - amount < MIN_BALANCE_CURRENT)
                || !verifyPin(pin)) {
            return false;
        }

        this.balance -= amount;
        return true;
    }

    public boolean closeAccount() {
        if (this.status.equals("Inactive")) {
            return false;
        }
        this.status = "Inactive";
        return true;
    }

    public boolean reopenAccount() {
        if (this.status.equals("Active")) {
            return false;
        }
        this.status = "Active";
        return true;
    }

    // --------- GETTERS ------------

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
        return accountType;
    }

    public String getStatus() {
        return status;
    }

    // --------- SETTERS ------------


    public void setName(String name) {
        this.name = name;
    }

    public boolean setAge(int age) {

        if (!isValidAge(age)) {
            return false;
        }

        this.age = age;
        return true;
    }

}