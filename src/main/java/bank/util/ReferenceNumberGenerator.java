package bank.util;

public final class ReferenceNumberGenerator {

    private static long transactionSequence = 1;
    private static long transferSequence = 1;

    private ReferenceNumberGenerator() {
    }

    public static String generateTransactionReference() {

        return String.format(
                "TXN-%06d",
                transactionSequence++
        );
    }

    public static String generateTransferReference() {

        return String.format(
                "TRF-%06d",
                transferSequence++
        );
    }
}