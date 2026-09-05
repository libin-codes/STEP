package week01.activity01;

public class Account {

    // Attributes
    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String accountType;
    private String status;
  

    Account(
            int accountNumber,
            String name,
            int age,
            double initialBalance,
            String accountType) {
        this.age = age;
        this.accountType = accountType;
        this.balance = initialBalance;
        this.accountNumber = accountNumber;
        this.name = name;
        this.status = "Active";
    }
    // ----------- METHODS --------------   
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }

        this.balance += amount;
        return true;

    }

    public boolean withdraw(double amount, int pin) {
        if (amount > this.balance || amount <= 0) {
            return false;
        }

        this.balance -= amount;
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

    public void setAge(int age) {
        this.age = age;
    }

}