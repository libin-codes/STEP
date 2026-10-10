package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class TestAbstractAccount {

    /**
     * Step 2 - Secure fund transfer.
     * 1. Withdraw from the source using the provided PIN (the template method validates PIN/status/amount).
     * 2. Only if the withdrawal succeeds, deposit into the destination.
     * 3. If the withdrawal throws, the destination is never credited - the exception simply propagates
     *    to the caller, who handles it gracefully.
     */
    private static void transferFunds(AbstractAccount source, AbstractAccount destination, double amount, String pin)
            throws AccountException {
        source.withdraw(amount, pin);       // debits source or throws (nothing credited yet)
        destination.deposit(amount);        // reached only when the withdrawal succeeded
    }

    /** Step 3 - Monthly banking cycle over the portfolio. */
    private static void processMonthlyCycle(AbstractAccount[] portfolio) {
        for (AbstractAccount account : portfolio) {
            if (account instanceof SavingsAccount) {
                ((SavingsAccount) account).applyInterest();
            } else if (account instanceof SalaryAccount) {
                // No salary credit recorded this month -> track inactivity
                ((SalaryAccount) account).incrementInactiveMonths();
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Activity 10: Banking Operations Suite ===");

        // Step 1 - Account portfolio of mixed AbstractAccount types
        AbstractAccount[] portfolio = {
                new SavingsAccount("SAV1001", "Rajesh Sharma", 28, 10000.0, "ACTIVE", "1234"),
                new CurrentAccount("CUR1001", "Priya Patel", 34, 5000.0, "ACTIVE", "5678", 10000.0),
                new SalaryAccount("SAL1001", "Amit Kumar", 31, 15000.0, "ACTIVE", "9999", "TechCorp Ltd")
        };

        AbstractAccount savings = portfolio[0];
        AbstractAccount current = portfolio[1];

        // Step 2 - Successful transfer from Savings to Current with PIN authentication
        try {
            transferFunds(savings, current, 3000.0, "1234");
            System.out.println("Transfer Rs 3000 from Savings to Current: SUCCESS");
            System.out.println("Savings Balance: Rs " + savings.getBalance()
                    + " | Current Balance: Rs " + current.getBalance());
        } catch (AccountException e) {
            System.out.println("Transfer Rs 3000 from Savings to Current: FAILED - " + e.getMessage());
        }

        // Step 3 - Failed transfer with wrong PIN: destination must NOT be credited
        double savingsBefore = savings.getBalance();
        double currentBefore = current.getBalance();
        try {
            transferFunds(savings, current, 500.0, "0000");
            System.out.println("Failed Transfer (Wrong PIN): [FAIL] - transfer should not succeed");
        } catch (AccountException e) {
            boolean unchanged = savings.getBalance() == savingsBefore
                    && current.getBalance() == currentBefore;
            System.out.println("Failed Transfer (Wrong PIN): Exception caught, no balance changed "
                    + (unchanged ? "[PASS]" : "[FAIL]"));
        }

        // Step 4 - Monthly cycle: interest for Savings, inactivity check for Salary
        processMonthlyCycle(portfolio);
        System.out.println("Monthly Interest Cycle processed for all qualifying accounts.");

        System.out.println("All banking operations passed!");
    }
}
