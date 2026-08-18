package bank.util;

public class AccountNumberGenerator {
    private static long nextAccountNumber = 1000000001L;

    private AccountNumberGenerator() {
    }

    public static String generate() {
        return String.valueOf(nextAccountNumber++);
    }
}
