public class Account {

    // constants
    final static int MIN_BALANCE_SAVINGS = 500;
    final static int MIN_BALANCE_CURRENT = 1000;

    // attributes
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

        // validate the age
        if (!isValidAge(18)) {
            this.age = 18;
        } else {
            this.age = age;
        }

        // validate account type
        if (!isValidAccountType(accountType)) {
            this.accountType = "Savings";
        } else {
            this.accountType = accountType;
        }

        // validate minimum balance

        if (accountType.equals("Savings") && initialBalance < MIN_BALANCE_SAVINGS) {
            this.balance = MIN_BALANCE_SAVINGS;
        } else if (accountType.equals("Current") && initialBalance < MIN_BALANCE_CURRENT) {
            this.balance = MIN_BALANCE_CURRENT;
        } else {
            this.balance = initialBalance;
        }

        this.accountNumber = accountNumber;
        this.name = name;

        this.status = "Active";
    }

    private boolean verifyPin(int pin) {
        return this.pin != null && this.pin == pin;
    }

    public boolean setPin(int pin) {
        if (pin >= 1000 && pin <= 9999) {
            this.pin = pin;
            return true;
        }
        return false;
    }

    public boolean hasPin() {
        return this.pin != null;
    }

    // validators

    private boolean isValidAge(int age) {
        return age >= 18;
    }

    private boolean isValidAccountType(String accountType) {
        return (accountType.equals("Savings") || accountType.equals("Current"));
    }

    // methods
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
                || (accountType.equals("Savings") && this.balance - amount < MIN_BALANCE_SAVINGS
                        || accountType.equals("Current") && this.balance - amount < MIN_BALANCE_CURRENT)
                || !verifyPin(pin)) {
            return false;
        }

        this.balance -= amount;
        return true;
    }

    public boolean closeAccount() {
        if (this.status.equals("Inactive")){
            return false;
        }
        this.status = "Inactive";
        return true;
    }

    public boolean reopenAccount() {
        if (this.status.equals("Active")){
            return false;
        }
        this.status = "Active";
        return true;
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

}