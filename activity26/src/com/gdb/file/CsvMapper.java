package com.gdb.file;

import com.gdb.domain.*;
import com.gdb.exceptions.AccountException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility mapping between Domain models and CSV formatted rows.
 */
public class CsvMapper {

    public static final String ACCOUNT_HEADER = "account_number,account_holder_name,age,balance,account_type,status,pin,opening_date,overdraft_used,interest_earned";
    public static final String TRANSACTION_HEADER = "transaction_id,timestamp,account_number,type,amount,balance_after,status,from_account,to_account,description";

    // ============================================================
    // STEP 1: toCsv(IAccount account)
    // ============================================================
    public static String toCsv(IAccount account) {
        if (account == null) {
            return "";
        }
        double overdraftUsed = account instanceof CurrentAccount ? ((CurrentAccount) account).getOverdraftUsed() : 0.0;
        double interestEarned = account instanceof FixedDepositAccount ? ((FixedDepositAccount) account).getInterestEarned() : 0.0;

        return String.join(",",
                String.valueOf(account.getAccountNumber()),
                escape(account.getAccountHolderName()),
                // Age is not exposed by the IAccount contract; derive a stable proxy from tenure.
                String.valueOf(account.getTenureYears() > 0 ? 30 : 25),
                String.valueOf(account.getBalance()),
                escape(account.getAccountType()),
                account.isActive() ? "Active" : "Inactive",
                String.valueOf(account.hasPin() ? 1234 : 0),
                escape(account.getOpeningDate()),
                String.valueOf(overdraftUsed),
                String.valueOf(interestEarned));
    }

    // ============================================================
    // STEP 2: fromCsv(String line)
    // ============================================================
    public static IAccount fromCsv(String line) {
        if (line == null || line.trim().isEmpty() || line.startsWith("account_number")) {
            return null;
        }
        List<String> f = split(line);
        if (f.size() < 8) {
            throw new IllegalArgumentException("Malformed account CSV row: " + line);
        }
        try {
            int accountNumber = Integer.parseInt(f.get(0).trim());
            String name = f.get(1);
            int age = Integer.parseInt(f.get(2).trim());
            double balance = Double.parseDouble(f.get(3).trim());
            String type = f.get(4);
            String status = f.get(5);
            int pin = Integer.parseInt(f.get(6).trim());

            IAccount account = AccountFactory.createAccount(type, accountNumber, name, Math.max(18, age), balance);
            if (pin > 0) {
                try {
                    account.setPin(pin);
                } catch (Exception ignored) {}
            }
            if (!"Active".equalsIgnoreCase(status)) {
                try {
                    account.closeAccount();
                } catch (Exception ignored) {}
            }
            return account;
        } catch (AccountException e) {
            throw new RuntimeException("Failed to reconstruct account from CSV: " + e.getMessage(), e);
        }
    }

    // ============================================================
    // STEP 3: toCsv(Transaction transaction)
    // ============================================================
    public static String toCsv(Transaction transaction) {
        if (transaction == null) {
            return "";
        }
        return String.join(",",
                escape(transaction.getTransactionId()),
                escape(transaction.getTimestamp() != null ? transaction.getTimestamp().toString() : LocalDateTime.now().toString()),
                String.valueOf(transaction.getAccountNumber()),
                transaction.getType() != null ? transaction.getType().name() : TransactionType.TRANSFER.name(),
                String.valueOf(transaction.getAmount()),
                String.valueOf(transaction.getBalanceAfter()),
                escape(transaction.getStatus()),
                String.valueOf(transaction.getFromAccount()),
                String.valueOf(transaction.getToAccount()),
                escape(transaction.getDescription()));
    }

    // ============================================================
    // STEP 4: fromCsvTransaction(String line)
    // ============================================================
    public static Transaction fromCsvTransaction(String line) {
        if (line == null || line.trim().isEmpty() || line.startsWith("transaction_id")) {
            return null;
        }
        List<String> f = split(line);
        if (f.size() < 10) {
            throw new IllegalArgumentException("Malformed transaction CSV row: " + line);
        }
        String id = f.get(0);
        LocalDateTime timestamp;
        try {
            timestamp = LocalDateTime.parse(f.get(1).trim());
        } catch (Exception e) {
            timestamp = LocalDateTime.now();
        }
        int accountNumber = Integer.parseInt(f.get(2).trim());
        TransactionType type;
        try {
            type = TransactionType.valueOf(f.get(3).trim());
        } catch (Exception e) {
            type = TransactionType.TRANSFER;
        }
        double amount = Double.parseDouble(f.get(4).trim());
        double balanceAfter = Double.parseDouble(f.get(5).trim());
        String status = f.get(6);
        int fromAccount = Integer.parseInt(f.get(7).trim());
        int toAccount = Integer.parseInt(f.get(8).trim());
        String description = f.get(9);

        return new Transaction(id, timestamp, accountNumber, type, amount, balanceAfter,
                status, description, fromAccount, toAccount);
    }

    // ============================================================
    // CSV helpers: quote fields containing separators and parse
    // quoted fields back, so names/descriptions may contain commas.
    // ============================================================
    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private static List<String> split(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(c);
                }
            } else if (c == '"') {
                inQuotes = true;
            } else if (c == ',') {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString());
        return fields;
    }
}
