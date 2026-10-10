package com.gdb.domain;

import java.util.HashMap;
import java.util.Map;

public class AccountRulesEngine {
    // Step 1 - Savings lookup tables: minimum balance and interest rate keyed by tenure bucket.
    private static final Map<String, Double> SAVINGS_MIN_BALANCE = new HashMap<>();
    private static final Map<String, Double> SAVINGS_INTEREST_RATE = new HashMap<>();

    static {
        SAVINGS_MIN_BALANCE.put("new", 10000.0);
        SAVINGS_MIN_BALANCE.put("standard", 7500.0);
        SAVINGS_MIN_BALANCE.put("premium", 5000.0);
        SAVINGS_MIN_BALANCE.put("privilege", 2500.0);

        SAVINGS_INTEREST_RATE.put("new", 2.70);
        SAVINGS_INTEREST_RATE.put("standard", 3.00);
        SAVINGS_INTEREST_RATE.put("premium", 3.50);
        SAVINGS_INTEREST_RATE.put("privilege", 4.00);
    }

    public static String getSavingsBucket(int tenureYears) {
        if (tenureYears >= 5) {
            return "privilege";
        } else if (tenureYears >= 3) {
            return "premium";
        } else if (tenureYears >= 1) {
            return "standard";
        } else {
            return "new";
        }
    }

    public static double getSavingsMinBalance(int tenureYears) {
        return SAVINGS_MIN_BALANCE.getOrDefault(getSavingsBucket(tenureYears), 10000.0);
    }

    public static double getSavingsInterestRate(int tenureYears) {
        return SAVINGS_INTEREST_RATE.getOrDefault(getSavingsBucket(tenureYears), 2.70);
    }

    public static double getCurrentOverdraftLimit(double monthlyTurnover) {
        return Math.max(25000.0, monthlyTurnover * 2.5);
    }

    public static double getFDInterestRate(int months) {
        if (months >= 36) {
            return 7.50;
        } else if (months >= 12) {
            return 6.50;
        } else {
            return 5.00;
        }
    }
}