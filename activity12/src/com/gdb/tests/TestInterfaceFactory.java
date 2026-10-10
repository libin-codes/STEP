package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.exceptions.*;

public class TestInterfaceFactory {
    public static void main(String[] args) {
        System.out.println("=== Activity 12: Factory-Driven System Suite ===");

        // NOTE: If you completed Activity 11 successfully, paste your working IAccount.java and AccountFactory.java into src/com/gdb/domain (replacing the provided versions).

        // Step 1 & 3: Instantiate Savings via AccountFactory, drive deposit/withdraw through IAccount,
        // and verify the Savings minimum balance rule through the interface.
        boolean savingsOk = false;
        try {
            IAccount savings = AccountFactory.createAccount("SAVINGS", "SAV-1001", "Alice", 30, 5000.0, "ACTIVE", "1234");

            // Step 2: deposit through the interface reference (no cast).
            savings.deposit(5000.0);  // balance -> 10000.0

            boolean depositOk = (savings.getBalance() == 10000.0);

            // A withdrawal that keeps the balance at/above the minimum (1000.0) succeeds.
            boolean withinLimitOk = false;
            try {
                savings.withdraw(3000.0, "1234");  // balance -> 7000.0
                withinLimitOk = true;
            } catch (MinimumBalanceViolationException e) {
                withinLimitOk = false;
            }

            // A withdrawal that would breach the minimum balance must be rejected.
            boolean minBalanceRejected = false;
            try {
                savings.withdraw(7000.0, "1234");  // would drop below Rs 1000 -> rejected
            } catch (MinimumBalanceViolationException e) {
                minBalanceRejected = true;
            }

            savingsOk = depositOk && withinLimitOk && minBalanceRejected;
        } catch (Exception e) {
            savingsOk = false;
            System.out.println("[Test 1] Savings Account Creation & Deposit: [FAIL] - " + e.getMessage());
        }
        System.out.println("[Test 1] Savings Account Creation & Deposit: " + (savingsOk ? "[PASS]" : "[FAIL]"));

        // Step 4: Verify Current account overdraft up to its limit (25000.0) succeeds via the interface,
        // and that exceeding balance + overdraft limit throws.
        boolean currentOk = false;
        try {
            IAccount current = AccountFactory.createAccount("CURRENT", "CUR-2002", "Bob", 35, 5000.0, "ACTIVE", "5678");

            current.deposit(5000.0);  // balance -> 10000.0 (interface-driven, no cast)

            // Within overdraft limit: 25000 <= balance(10000) + limit(25000) -> succeeds, balance goes negative.
            boolean overdraftSucceeded = false;
            try {
                current.withdraw(25000.0, "5678");  // balance -> -15000.0
                overdraftSucceeded = current.getBalance() < 0;
            } catch (AccountException e) {
                overdraftSucceeded = false;
            }

            // Exceeding the overdraft limit: 20000 > balance(-15000) + limit(25000) = 10000 -> rejected.
            boolean overLimitRejected = false;
            try {
                current.withdraw(20000.0, "5678");
            } catch (AccountException e) {
                overLimitRejected = true;
            }

            currentOk = overdraftSucceeded && overLimitRejected;
        } catch (Exception e) {
            currentOk = false;
            System.out.println("[Test 2] Current Account Overdraft Withdrawal: [FAIL] - " + e.getMessage());
        }
        System.out.println("[Test 2] Current Account Overdraft Withdrawal: " + (currentOk ? "[PASS]" : "[FAIL]"));

        // Step 5: Verify FixedDeposit rejects premature withdrawals through the interface.
        boolean fdOk = false;
        try {
            IAccount fixedDeposit = AccountFactory.createAccount("FIXED_DEPOSIT", "FD-3003", "Carol", 40, 50000.0, "ACTIVE", "1111");

            try {
                fixedDeposit.withdraw(1000.0, "1111");  // premature withdrawal -> must throw AccountException
            } catch (AccountException e) {
                fdOk = true;
            }
        } catch (Exception e) {
            fdOk = false;
            System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: [FAIL] - " + e.getMessage());
        }
        System.out.println("[Test 3] Fixed Deposit Premature Withdrawal Block: " + (fdOk ? "[PASS]" : "[FAIL]"));

        // Step 6: Verify requesting an unknown account type from AccountFactory throws IllegalArgumentException
        // whose message mentions the bogus type.
        boolean invalidOk = false;
        try {
            AccountFactory.createAccount("INVALID", "X-9999", "Dave", 25, 100.0, "ACTIVE", "4321");
        } catch (IllegalArgumentException e) {
            invalidOk = e.getMessage() != null && e.getMessage().contains("INVALID");
        } catch (Exception e) {
            invalidOk = false;
            System.out.println("[Test 4] Invalid Type Rejection: [FAIL] - " + e.getMessage());
        }
        System.out.println("[Test 4] Invalid Type Rejection: " + (invalidOk ? "[PASS]" : "[FAIL]"));

        System.out.println("Factory-driven architecture successfully verified!");
    }
}