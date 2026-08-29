import java.util.ArrayList;
import java.util.List;

public class TestAccount {
    private static void displayAccountInfo(Account acc) {
        System.out.println("Account #" + acc.getAccountNumber() + " | " +
                acc.getName() + " (" + acc.getAge() + " yrs) | " +
                acc.getAccountType() + " | \u20B9" + acc.getBalance() + " | " +
                acc.getStatus());
    }

    public static void main(String[] args) {
        System.out.println("=".repeat(35));
        System.out.println("GLOBAL DIGITAL BANK - ACCOUNT TEST");
        System.out.println("=".repeat(35));

        List<Account> accounts = new ArrayList<>();

        System.out.println(">>> 1. Creating Account");
        Account acc1 = new Account(1001, "John Doe", 25, 1000.0, "Active");
        accounts.add(acc1);
        System.out.println("Account created!");
        displayAccountInfo(acc1);

    
        System.out.println(">>> 2. Deposit Money");
        double validDeposit = 500.0;

        System.out.print("Depositing \u20B9" + validDeposit + ": ");
        if (acc1.deposit(validDeposit)) {
            System.out.println("SUCCESS");
            System.out.println("New balance: \u20B9" + acc1.getBalance());
        } else {
            System.out.println("FAILED");
        }

       
        double invalidDeposit = -100.0;
        System.out.print("Depositing \u20B9" + invalidDeposit + ": ");
        if (acc1.deposit(invalidDeposit)) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED (Invalid amount)");
        }

      
        System.out.println(">>> 3. Withdraw Money");
        
    
        double validWithdraw = 200.0;
        System.out.print("Withdrawing \u20B9" + validWithdraw + ": ");
        if (acc1.withdraw(validWithdraw)) {
            System.out.println("SUCCESS");
            System.out.println("New balance: \u20B9" + acc1.getBalance());
        } else {
            System.out.println("FAILED");
        }

        double invalidWithdraw = 2000.0;
        System.out.print("Withdrawing \u20B9" + invalidWithdraw + ": ");
        if (acc1.withdraw(invalidWithdraw)) {
            System.out.println("SUCCESS");
        } else {    
            System.out.println("FAILED (Insufficient balance)");
            System.out.println("Current balance: \u20B9" + acc1.getBalance());
        }

     
        System.out.println(">>> 4. Creating Another Account");
        Account acc2 = new Account(1002, "Jane Smith", 30, 2000.0, "Active");
        accounts.add(acc2);
        displayAccountInfo(acc2);

    
        System.out.println(">>> 5. All Accounts");
        for (Account acc : accounts) {
            displayAccountInfo(acc);
        }

        System.out.println("==================================================");
        System.out.println("TEST COMPLETED!");
        System.out.println("==================================================");

    }
}
