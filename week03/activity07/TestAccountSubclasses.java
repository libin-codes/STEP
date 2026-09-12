package week03.activity07;

public class TestAccountSubclasses {

    public static void main(String[] args) {
        System.out.println("=== Activity 7: Account Subclasses Test ===");

        SavingsAccount savings = new SavingsAccount(1001, "Alice", 28, 10000.0, 1000.0, 4.0);
        System.out.println("Savings Account Created: Balance Rs " + savings.getBalance() + " | Min Balance: Rs " + savings.getMinBalance());

        CurrentAccount current = new CurrentAccount(1002, "Bob", 35, 5000.0, 25000.0);
        System.out.println("Current Account Created: Overdraft Limit Rs " + current.getOverdraftLimit());

        FixedDepositAccount fd = new FixedDepositAccount(1003, "Charlie", 45, 50000.0, 12, 6.5);
        System.out.println("Fixed Deposit Created: Tenure " + fd.getTenureMonths() + " months | Interest: " + fd.getInterestRate() + "%");

        SalaryAccount salary = new SalaryAccount(1004, "Diana", 30, 20000.0, "Infosys");
        System.out.println("Salary Account Created: Employer " + salary.getEmployerName());

        System.out.println("All subclasses instantiated successfully!");
    }
}
