package org.arkaan.simpleatm.controller;

import org.arkaan.simpleatm.dto.response.Response;
import org.arkaan.simpleatm.dto.response.TransferDto;
import org.arkaan.simpleatm.dto.response.WithdrawDto;
import org.arkaan.simpleatm.service.ATMService;
import org.arkaan.simpleatm.util.Constant;
import org.arkaan.simpleatm.model.Transaction;
import org.arkaan.simpleatm.model.Status;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static org.arkaan.simpleatm.util.Helper.*;

public class AtmController {
    public enum State {
        AUTHENTICATED,
        IDLE,
        OFFLINE
    }

    private State state;
    private final Scanner stdIn;
    private final Random random;
    private final ATMService atmService;
    private int currentAccount;

    public AtmController(ATMService atmService) {
        state = State.IDLE;
        stdIn = new Scanner(System.in);
        random = new Random();
        this.atmService = atmService;
    }

    private void setCurrentAccount(int accountNumber) {
        currentAccount = accountNumber;
    }

    public void authenticate() {
        System.out.println(
                "===========================\n" +
                "||          ATM          ||\n" +
                "===========================");
        System.out.println("Enter your account number:");
        String account = stdIn.next();
        if (isInvalidAuthInput(account, "Account Number")) return;
        System.out.println("Enter your pin:");
        String pin = stdIn.next();
        if (isInvalidAuthInput(pin, "PIN")) return;

        int accountNumber = Integer.parseInt(account);
        boolean isValid = atmService.authenticate(accountNumber, Integer.parseInt(pin));
        if (isValid) {
            state = State.AUTHENTICATED;
            setCurrentAccount(accountNumber);
        } else {
            System.out.println("Invalid account number / pin");
        }
    }

    private boolean isInvalidAuthInput(String input, String msg) {
        if (!isSixDigits(input)) {
            System.out.printf("%s should have 6 digits length%n", msg);
            return true;
        }
        if (isNotNumber(input)) {
            System.out.printf("%s should only contains number%n", msg);
            return true;
        }
        return false;
    }

    private boolean withdrawMoney() {
        int[] amountList = Constant.AMOUNT_OPTIONS;
        System.out.println("==============\n1. $10\n2. $50\n3. $100\n4. Other\n5. Back\n");
        System.out.print("Choose amount: ");
        int amountSelect = stdIn.nextInt();
        int amount;
        if (amountSelect == 4) {
            while (true) {
                System.out.print("Enter amount: ");
                String amountInput = stdIn.next();
                if (isNotNumber(amountInput)) {
                    System.out.println("Invalid amount");
                    continue;
                }
                amount = Integer.parseInt(amountInput);
                if (amount % Constant.AMOUNT_MULTIPLE != 0) {
                    System.out.println("Invalid amount");
                    continue;
                }
                if (amount > Constant.MAX_WITHDRAW_AMOUNT) {
                    System.out.printf("Maximum amount to withdraw is $%d", Constant.MAX_WITHDRAW_AMOUNT);
                    continue;
                }
                break;
            }
        } else if (amountSelect >= 5) {
            return true;
        } else {
            amount = amountList[amountSelect - 1];
        }

        Response<WithdrawDto> result = atmService.withdraw(currentAccount, amount);
        if (result.getStatus() == Status.FAILED) {
            System.out.println(result.getMsg());
        } else {
            WithdrawDto payload = result.getPayload();
            System.out.printf("Summary%nDate : %s%nWithdraw : $%d%nBalance: $%d%n%n",
                    payload.getDate(), payload.getAmount(), payload.getBalance());
        }
        return afterSummary();
    }

    private boolean depositMoney() {
        int[] amountList = Constant.AMOUNT_OPTIONS;
        System.out.println("==============\n1. $10\n2. $50\n3. $100\n4. Other\n5. Back\n");
        int amount = stdIn.nextInt();
        try {
            Status status = atmService
                    .deposit(currentAccount, amountList[amount - 1]);
            if (status == Status.FAILED) {
                System.out.println("Deposit failed.");
            } else {
                System.out.println("Deposit success.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please select appropriate input.\n");
        }
        return afterSummary();
    }

    private boolean transferMoney() {
        System.out.println(
                "================\n" +
                "||  Transfer  ||\n" +
                "================");
        System.out.print("Enter account number (destination): ");
        stdIn.nextLine();
        String destinationInput = stdIn.nextLine();
        if (destinationInput.trim().isEmpty()) return true;
        System.out.print("Enter amount: ");
        String amountInput = stdIn.nextLine();
        if (amountInput.trim().isEmpty()) return true;

        String ref = String.format("%04d", random.nextInt(999999));
        System.out.println("\n=================================");
        System.out.printf("Transfer Confirmation %nDestination account\t: %s %nAmount\t\t\t: $%s %nRef. Number\t\t: %s %n",
                destinationInput, amountInput, ref);
        System.out.println("=================================\n");
        System.out.print("1. Confirm transfer\n2. Cancel\nChoose options[2]: ");
        int confirm = stdIn.nextInt();
        if (confirm == 2) return true;

        if (isNotNumber(destinationInput)) {
            System.out.println("Invalid account");
            return true;
        }

        if (isNotNumber(amountInput)) {
            System.out.println("Invalid amount");
            return true;
        }

        int amount = Integer.parseInt(amountInput);
        if (amount < Constant.MIN_WITHDRAW_AMOUNT) {
            System.out.printf("Minimum amount to transfer is $%d%n", Constant.MIN_WITHDRAW_AMOUNT);
            return true;
        }

        if (amount > Constant.MAX_WITHDRAW_AMOUNT) {
            System.out.printf("Maximum amount to transfer is $%d%n", Constant.MAX_WITHDRAW_AMOUNT);
            return true;
        }

        Response<TransferDto> result = atmService.transfer(
                currentAccount, Integer.parseInt(destinationInput), amount, ref);

        if (result.getStatus() == Status.FAILED) {
            System.out.println(result.getMsg());
        } else {
            TransferDto payload = result.getPayload();
            String summary = String.format("Fund Transfer Summary (%s) %nDestination account\t: %s %nAmount\t\t\t: $%s %nRef. Number\t\t: %s %nBalance\t\t\t: $%d %n",
                    payload.getDate(), payload.getDestination(), amount, ref, payload.getBalance());
            System.out.println(summary);
        }

        return afterSummary();
    }

    private boolean viewTransactionHistory() {
        Response<List<Transaction>> result = atmService.getTransactionHistory(currentAccount);

        if (result.getStatus() == Status.FAILED) {
            System.out.println(result.getMsg());
        } else {
            List<?> transactionList = result.getPayload();
            System.out.println("===================\nTransaction History\n");
            System.out.printf("%-10s %-15s %-10s %s %n", "ID", "TYPE", "STATUS", "DETAIL");
            for (Object t : transactionList) {
                if (t instanceof Transaction) {
                    Transaction tr = (Transaction) t;
                    System.out.printf("%-10s %-15s %-10s %s %n", tr.getId(), tr.getType(), tr.getStatus(), tr.getDetail());
                }
            }
            System.out.println();
        }
        return afterSummary();
    }

    private boolean getBalance() {
        System.out.printf("Your balance: $%d%n%n", atmService.getAccountBalance(currentAccount));
        return afterSummary();
    }

    public State getState() {
        return state;
    }

    private boolean afterSummary() {
        int selected = 99;
        while (selected != 2) {
            System.out.println("1. Transaction\n2. Exit");
            System.out.print("Choose option[2]: ");
            selected = stdIn.nextInt();
            if (selected == 1) return true;
        }
        atmService.saveAll();
        removeAuth();
        return false;
    }

    private void removeAuth() {
        stdIn.reset();
        state = State.IDLE;
        setCurrentAccount(Constant.UNAUTHORIZED);
    }

    public void displayMenu() {
        int selected = 99;
        while (selected != 5) {
            System.out.print(
                    "=====================\n" +
                    "||     Welcome     ||\n" +
                    "=====================\n" +
                    "1. Withdraw\n" +
                    "2. Transfer\n" +
                    "3. View Balance\n" +
                    "4. Transaction History\n" +
                    "5. Exit\n" +
                    "Choose option[5]: ");
            selected = stdIn.nextInt();
            switch (selected) {
                case 1: {
                    if (!withdrawMoney()) return;
                    break;
                }
                case 2: {
                    if (!transferMoney()) return;
                    break;
                }
                case 3: {
                    if (!getBalance()) return;
                    break;
                }
                case 4: {
                    if (!viewTransactionHistory()) return;
                    break;
                }
                case 5: {
                    atmService.saveAll();
                    removeAuth();
                    break;
                }
            }
        }
    }
}
