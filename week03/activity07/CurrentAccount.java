package week03.activity07;

public class CurrentAccount extends Account {

    private double overdraftLimit;

    public CurrentAccount(int accountNumber, String name, int age, double balance, double overdraftLimit) {
        super(accountNumber, name, age, balance, "CURRENT");
        this.overdraftLimit = overdraftLimit;
    }

    public CurrentAccount(int accountNumber, String name, int age, double balance) {
        this(accountNumber, name, age, balance, 25000.0);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0 || !isActive()) {
            return false;
        }
        if (amount > (this.balance + this.overdraftLimit)) {
            return false;
        }
        this.balance -= amount;
        return true;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}
