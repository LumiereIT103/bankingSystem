package bank.menu;

import java.util.Scanner;

public class BankingMenu {

    private final CustomerMenu customerMenu;
    private final AccountMenu accountMenu;
    private final TransactionMenu transactionMenu;

    private final Scanner scanner;

    public BankingMenu(
            CustomerMenu customerMenu,
            AccountMenu accountMenu,
            TransactionMenu transactionMenu,
            Scanner scanner
    ) {
        this.customerMenu = customerMenu;
        this.accountMenu = accountMenu;
        this.transactionMenu = transactionMenu;
        this.scanner = scanner;
    }

    public void start() {

        while (true) {

            System.out.println();
            System.out.println("========================================");
            System.out.println("      BANKING MANAGEMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Customer Management");
            System.out.println("2. Account Management");
            System.out.println("3. Transactions");
            System.out.println("0. Exit");
            System.out.print("Choose: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1" -> customerMenu.show();

                case "2" -> accountMenu.show();

                case "3" -> transactionMenu.show();

                case "0" -> {
                    System.out.println();
                    System.out.println(
                            "Thank you for using the Banking Management System."
                    );
                    return;
                }

                default ->
                        System.out.println(
                                "Invalid choice."
                        );
            }
        }
    }
}