package bank.util;

import java.math.BigDecimal;

public final class InputValidator {

    private InputValidator() {
    }

    public static String requireText(
            String value,
            String fieldName
    ) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " is required."
            );
        }

        return value.trim();
    }

    public static long requirePositiveLong(
            String value,
            String fieldName
    ) {

        try {

            long number = Long.parseLong(value);

            if (number <= 0) {
                throw new IllegalArgumentException(
                        fieldName +
                                " must be greater than zero."
                );
            }

            return number;

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    fieldName +
                            " must be a valid number."
            );
        }
    }

    public static BigDecimal requirePositiveAmount(
            String value
    ) {

        try {

            BigDecimal amount =
                    new BigDecimal(value);

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                        "Amount must be greater than zero."
                );
            }

            return amount;

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Amount must be a valid number."
            );
        }
    }

    public static int requirePositiveInt(
            String value,
            String fieldName
    ) {

        try {

            int number = Integer.parseInt(value);

            if (number <= 0) {
                throw new IllegalArgumentException(
                        fieldName +
                                " must be greater than zero."
                );
            }

            return number;

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    fieldName +
                            " must be a valid number."
            );
        }
    }
}