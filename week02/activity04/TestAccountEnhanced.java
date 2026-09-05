package week02.activity04;

import java.util.ArrayList;
import java.util.List;

public class TestAccountEnhanced {
    private static void displayAccountInfo(Account acc) {
        System.out.println(
                "Account #" + acc.getAccountNumber() + " | " +
                acc.getName() + " (" + acc.getAge() + " yrs) | " +
                acc.getAccountType() + " | ₹" + acc.getBalance() + " | " +
                acc.getStatus() + " | PIN: " +
                (acc.hasPin() ? "Yes" : "No")
        );
    }

    public static void main(String[] args) {

        List<Account> accounts = new ArrayList<>();

        System.out.println("=".repeat(60));
        System.out.println("ENHANCED ACCOUNT TEST (BOOLEAN RETURNS)");
        System.out.println("=".repeat(60));

        // -------------------------------------------------
        // TEST 1: Valid Account Creation
        // -------------------------------------------------
        System.out.println();
        System.out.println(">>> Test 1: Valid Account Creation");

        Account acc1 = new Account(
                1001,
                "John Doe",
                25,
                1000.0,
                "Savings"
        );

        accounts.add(acc1);
        displayAccountInfo(acc1);


        // -------------------------------------------------
        // TEST 2: Invalid Age
        // -------------------------------------------------
        System.out.println(">>> Test 2: Invalid Age (under 18)");
        System.out.println("Creating account with age 16");

        Account acc2 = new Account(
                1002,
                "Young Kid",
                16,
                500.0,
                "Savings"
        );

        accounts.add(acc2);

        System.out.println("Age auto-corrected to: " + acc2.getAge());
        displayAccountInfo(acc2);


        // -------------------------------------------------
        // TEST 3: Invalid Account Type
        // -------------------------------------------------
        System.out.println(">>> Test 3: Invalid Account Type");
        System.out.println("Creating account with type \"Invalid\"");

        Account acc3 = new Account(
                1003,
                "Test User",
                25,
                500.0,
                "Invalid"
        );

        accounts.add(acc3);

        System.out.println(
                "Account type defaulted to: " + acc3.getAccountType()
        );

        displayAccountInfo(acc3);


        // -------------------------------------------------
        // TEST 4: Minimum Balance Enforcement
        // -------------------------------------------------
        System.out.println(">>> Test 4: Minimum Balance Enforcement on Creation");
        System.out.println("Creating Savings account with ₹300 (below minimum)");

        Account acc4 = new Account(
                1004,
                "Bob Wilson",
                25,
                300.0,
                "Savings"
        );

        accounts.add(acc4);

        System.out.println(
                "Balance auto-corrected to minimum: ₹" + acc4.getBalance()
        );

        displayAccountInfo(acc4);


        // -------------------------------------------------
        // TEST 5: Withdrawal with Minimum Balance
        // -------------------------------------------------
        System.out.println(">>> Test 5: Withdrawal with Minimum Balance");

        Account acc5 = new Account(
                1005,
                "Alice Brown",
                30,
                1200.0,
                "Current"
        );

        acc5.setPin(1234);
        accounts.add(acc5);

        System.out.print("Initial: ");
        displayAccountInfo(acc5);

        double withdrawal1 = 200.0;

        System.out.print("Withdrawing ₹" + withdrawal1 + ": ");

        if (acc5.withdraw(withdrawal1, 1234)) {
            System.out.println("SUCCESS");
            System.out.println("New balance: ₹" + acc5.getBalance());
        } else {
            System.out.println("FAILED");
        }

        System.out.print("After withdrawal: ");
        displayAccountInfo(acc5);

        double withdrawal2 = 900.0;

        System.out.print(
                "Withdrawing ₹" + withdrawal2 +
                " (would leave ₹-100): "
        );

        if (acc5.withdraw(withdrawal2, 1234)) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED (Minimum balance violation)");
            System.out.println("Current balance: ₹" + acc5.getBalance());
        }


        // -------------------------------------------------
        // TEST 6: Account Status Management
        // -------------------------------------------------
        System.out.println(">>> Test 6: Account Status Management");

        Account acc6 = new Account(
                1006,
                "Charlie Green",
                35,
                2000.0,
                "Savings"
        );

        accounts.add(acc6);

        System.out.print("Initial: ");
        displayAccountInfo(acc6);

        System.out.print("Closing account: ");

        if (acc6.closeAccount()) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED");
        }

        System.out.print("After close: ");
        displayAccountInfo(acc6);

        double closedDeposit = 500.0;

        System.out.print(
                "\nDepositing ₹" + closedDeposit +
                " to closed account: "
        );

        if (acc6.deposit(closedDeposit)) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED (Account inactive)");
        }

        System.out.print("Reopening account: ");

        if (acc6.reopenAccount()) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED");
        }

        System.out.print("After reopen: ");
        displayAccountInfo(acc6);


        // -------------------------------------------------
        // TEST 7: PIN Protection
        // -------------------------------------------------
        System.out.println(">>> Test 7: PIN Protection");

        Account acc7 = new Account(
                1007,
                "Diana Prince",
                28,
                1500.0,
                "Savings"
        );

        accounts.add(acc7);

        System.out.print("Setting PIN 1234: ");

        if (acc7.setPin(1234)) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED");
        }

        double pinWithdrawal1 = 200.0;

        System.out.print(
                "Withdrawing ₹" + pinWithdrawal1 +
                " with correct PIN (1234): "
        );

        if (acc7.withdraw(pinWithdrawal1, 1234)) {
            System.out.println("SUCCESS");
            System.out.println("New balance: ₹" + acc7.getBalance());
        } else {
            System.out.println("FAILED");
        }

        double pinWithdrawal2 = 100.0;

        System.out.print(
                "Withdrawing ₹" + pinWithdrawal2 +
                " with incorrect PIN (9999): "
        );

        if (acc7.withdraw(pinWithdrawal2, 9999)) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED (Incorrect PIN)");
        }


        // Account with no PIN
        Account acc8 = new Account(
                1008,
                "No Pin User",
                30,
                1000.0,
                "Savings"
        );

        System.out.print(
                "Withdrawing ₹100.0 with PIN not set: "
        );

        if (acc8.withdraw(100.0, 1234)) {
            System.out.println("SUCCESS");
        } else {
            System.out.println("FAILED (PIN not set)");
        }


        // -------------------------------------------------
        // TEST 8: All Accounts Summary
        // -------------------------------------------------
        System.out.println(">>> Test 8: All Accounts Summary");

        for (Account acc : accounts) {
            displayAccountInfo(acc);
        }

        System.out.println("=".repeat(60));
        System.out.println("ENHANCED TEST COMPLETED!");
        System.out.println("=".repeat(60));
    }
}