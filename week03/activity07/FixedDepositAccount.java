package week03.activity07;

public class FixedDepositAccount extends Account {

    private int tenureMonths;
    private double interestRate;

    public FixedDepositAccount(int accountNumber, String name, int age, double balance, int tenureMonths, double interestRate) {
        super(accountNumber, name, age, balance, "FIXED_DEPOSIT");
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
    }

    public FixedDepositAccount(int accountNumber, String name, int age, double balance) {
        this(accountNumber, name, age, balance, 12, 6.5);
    }

    public double calculateMaturityAmount() {
        double timeYears = (double) this.tenureMonths / 12.0;
        return this.balance * Math.pow(1.0 + (this.interestRate / 100.0), timeYears);
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public void setTenureMonths(int tenureMonths) {
        this.tenureMonths = tenureMonths;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
