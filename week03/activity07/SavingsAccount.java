package week03.activity07;

public class SavingsAccount extends Account {

    private double minBalance;
    private double interestRate;

    public SavingsAccount(int accountNumber, String name, int age, double balance, double minBalance, double interestRate) {
        super(accountNumber, name, age, balance, "SAVINGS");
        this.minBalance = minBalance;
        this.interestRate = interestRate;
    }

    public SavingsAccount(int accountNumber, String name, int age, double balance) {
        this(accountNumber, name, age, balance, 1000.0, 4.0);
    }

    public void applyInterest() {
        double interest = this.balance * (this.interestRate / 100.0);
        this.balance += interest;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0 || !isActive()) {
            return false;
        }
        if ((this.balance - amount) < this.minBalance) {
            return false;
        }
        this.balance -= amount;
        return true;
    }

    public double getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(double minBalance) {
        this.minBalance = minBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
