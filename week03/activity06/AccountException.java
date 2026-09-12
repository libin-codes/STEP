package week03.activity06;

/**
 * Base exception for all account-related errors.
 */
public class AccountException extends Exception {

    public AccountException(String message) {
        super(message);
    }
}
