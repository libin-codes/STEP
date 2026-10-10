package com.gdb.service;

import com.gdb.domain.IAccount;
import com.gdb.domain.Transaction;
import com.gdb.domain.AccountFactory;
import com.gdb.exceptions.AccountException;
import com.gdb.exceptions.InvalidPinException;
import com.gdb.logging.TransactionLogger;
import com.gdb.command.DepositCommand;
import com.gdb.command.WithdrawCommand;
import com.gdb.command.TransferCommand;
import com.gdb.command.TransactionCommand;

import java.util.*;

public class AccountService {
    // ============================================================
    // 📝 STEP 1: Declare Fields
    // ============================================================
    private final Map<Integer, IAccount> accounts;
    private final TransactionLogger logger;
    private final TransferService transferService;
    private int nextAccountNumber;

    // ============================================================
    // 📝 STEP 2: Constructor
    //
    // INSTRUCTIONS:
    //   1. Accept TransactionLogger parameter.
    //   2. Assign this.logger = logger.
    //   3. Initialize accounts = new HashMap<>().
    //   4. Initialize transferService = new TransferService().
    //   5. Initialize nextAccountNumber = 1001.
    // ============================================================
    public AccountService(TransactionLogger logger) {
        this.logger = logger;
        this.accounts = new HashMap<>();
        this.transferService = new TransferService();
        this.nextAccountNumber = 1001;
    }

    // ============================================================
    // 📝 STEP 3: Implement openAccount
    //
    // INSTRUCTIONS:
    //   1. int accountNumber = nextAccountNumber++;
    //   2. IAccount account = AccountFactory.createAccount(type, accountNumber, name, age, initialBalance);
    //   3. accounts.put(accountNumber, account);
    //   4. return account;
    // ============================================================
    public IAccount openAccount(String type, String name, int age, double initialBalance)
            throws AccountException {
        int accountNumber = nextAccountNumber++;
        IAccount account = AccountFactory.createAccount(type, accountNumber, name, age, initialBalance);
        accounts.put(accountNumber, account);
        return account;
    }

    // ============================================================
    // 📝 STEP 4: Implement closeAccount
    //
    // INSTRUCTIONS:
    //   1. IAccount account = accounts.get(accountNumber);
    //   2. If null -> throw new AccountException("Account not found: " + accountNumber);
    //   3. If !account.verifyPin(pin) -> throw new InvalidPinException("Incorrect PIN");
    //   4. Call account.closeAccount();
    // ============================================================
    public void closeAccount(int accountNumber, int pin) throws AccountException {
        IAccount account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        if (!account.verifyPin(pin)) {
            throw new InvalidPinException("Incorrect PIN");
        }
        account.closeAccount();
    }

    // ============================================================
    // 📝 STEP 5: Implement deposit
    //
    // INSTRUCTIONS:
    //   1. Look up account; throw AccountException if not found.
    //   2. DepositCommand cmd = new DepositCommand(account, amount);
    //   3. cmd.execute();
    //   4. logger.log(cmd);
    //   5. return cmd.getTransaction();
    // ============================================================
    public Transaction deposit(int accountNumber, double amount) throws Exception {
        IAccount account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        DepositCommand cmd = new DepositCommand(account, amount);
        cmd.execute();
        logger.log(cmd);
        return cmd.getTransaction();
    }

    // ============================================================
    // 📝 STEP 6: Implement withdraw
    //
    // INSTRUCTIONS:
    //   1. Look up account; throw AccountException if not found.
    //   2. WithdrawCommand cmd = new WithdrawCommand(account, amount, pin);
    //   3. cmd.execute();
    //   4. logger.log(cmd);
    //   5. return cmd.getTransaction();
    // ============================================================
    public Transaction withdraw(int accountNumber, double amount, int pin) throws Exception {
        IAccount account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountException("Account not found: " + accountNumber);
        }
        WithdrawCommand cmd = new WithdrawCommand(account, amount, pin);
        cmd.execute();
        logger.log(cmd);
        return cmd.getTransaction();
    }

    // ============================================================
    // 📝 STEP 7: Implement transfer
    //
    // INSTRUCTIONS:
    //   1. Look up fromAccount and toAccount; throw AccountException if either missing.
    //   2. TransferCommand cmd = new TransferCommand(fromAccount, toAccount, amount, pin);
    //   3. cmd.execute();
    //   4. logger.log(cmd);
    //   5. return cmd.getTransaction();
    // ============================================================
    public Transaction transfer(int fromAccountNumber, int toAccountNumber,
                                double amount, int pin) throws Exception {
        IAccount fromAccount = accounts.get(fromAccountNumber);
        if (fromAccount == null) {
            throw new AccountException("Account not found: " + fromAccountNumber);
        }
        IAccount toAccount = accounts.get(toAccountNumber);
        if (toAccount == null) {
            throw new AccountException("Account not found: " + toAccountNumber);
        }
        TransferCommand cmd = new TransferCommand(fromAccount, toAccount, amount, pin);
        cmd.execute();
        logger.log(cmd);
        return cmd.getTransaction();
    }

    // ============================================================
    // 📝 STEP 8: Implement getAccount
    //
    // INSTRUCTIONS:
    //   return accounts.get(accountNumber);
    // ============================================================
    public IAccount getAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }

    // ============================================================
    // 📝 STEP 9: Implement getAllAccounts
    //
    // INSTRUCTIONS:
    //   return new ArrayList<>(accounts.values());
    // ============================================================
    public List<IAccount> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }

    // ============================================================
    // 📝 STEP 10: Implement getTransactionHistory
    //
    // INSTRUCTIONS:
    //   return logger != null ? logger.readAll() : new ArrayList<>();
    // ============================================================
    public List<TransactionCommand> getTransactionHistory() {
        return logger != null ? logger.readAll() : new ArrayList<>();
    }

    // ============================================================
    // 📝 STEP 11: Implement getNextAccountNumber
    //
    // INSTRUCTIONS:
    //   return nextAccountNumber;
    // ============================================================
    public int getNextAccountNumber() {
        return nextAccountNumber;
    }
}