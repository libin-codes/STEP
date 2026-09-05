package week02.activity05;

/**
 * Thrown when withdrawal would violate minimum balance requirement.
 */
public class MinimumBalanceViolationException extends AccountException {

    public MinimumBalanceViolationException(String message) {
        super(message);
    }
}