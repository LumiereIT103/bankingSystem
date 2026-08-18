package bank.app;

import bank.dao.AccountDAO;
import bank.dao.CustomerDAO;
import bank.dao.TransactionDAO;
import bank.dao.impl.AccountDAOImpl;
import bank.dao.impl.CustomerDAOImpl;
import bank.dao.impl.TransactionDAOImpl;
import bank.menu.AccountMenu;
import bank.menu.BankingMenu;
import bank.menu.CustomerMenu;
import bank.menu.TransactionMenu;
import bank.service.AccountService;
import bank.service.CustomerService;
import bank.service.TransactionService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // =========================
        // DAO implementations
        // =========================

        CustomerDAO customerDAO =
                new CustomerDAOImpl();

        AccountDAO accountDAO =
                new AccountDAOImpl();

        TransactionDAO transactionDAO =
                new TransactionDAOImpl();

        // =========================
        // Services
        // =========================

        CustomerService customerService =
                new CustomerService(
                        customerDAO
                );

        AccountService accountService =
                new AccountService(
                        accountDAO,
                        customerDAO
                );

        TransactionService transactionService =
                new TransactionService(
                        accountDAO,
                        transactionDAO
                );

        // =========================
        // Menus
        // =========================

        CustomerMenu customerMenu =
                new CustomerMenu(
                        customerService,
                        scanner
                );

        AccountMenu accountMenu =
                new AccountMenu(
                        accountService,
                        scanner
                );

        TransactionMenu transactionMenu =
                new TransactionMenu(
                        transactionService,
                        scanner
                );

        BankingMenu bankingMenu =
                new BankingMenu(
                        customerMenu,
                        accountMenu,
                        transactionMenu,
                        scanner
                );

        // =========================
        // Start application
        // =========================

        try {
            bankingMenu.start();
        } finally {
            scanner.close();
        }
    }
}