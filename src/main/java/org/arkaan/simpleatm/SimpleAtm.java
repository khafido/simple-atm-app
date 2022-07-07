package org.arkaan.simpleatm;

import org.arkaan.simpleatm.datamodel.Card;
import org.arkaan.simpleatm.datamodel.ATMRepository;
import org.arkaan.simpleatm.datamodel.Transaction;
import org.arkaan.simpleatm.util.Constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public class SimpleAtm {
    enum State {
        AUTHENTICATED,
        IDLE,
        OFFLINE
    }

    private Card currentUser;
    private final ATMRepository atmRepository;
    private State state;
    private final Scanner stdIn;
    private final Random random;
    private final DateTimeFormatter dateTimeFormatter;

    SimpleAtm(ATMRepository atmRepository) {
        this.atmRepository = atmRepository;
        state = State.IDLE;
        stdIn = new Scanner(System.in);
        random = new Random();
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    private void setCurrentUser(Card currentUser) {
        this.currentUser = currentUser;
    }

    void authenticate() {
        System.out.println("Enter your account number:");
        String accountNumber = stdIn.next();
        if (!validateAuth(accountNumber, "Account Number")) return;
        System.out.println("Enter your pin:");
        String pin = stdIn.next();
        if (!validateAuth(pin, "PIN")) return;
        Optional<Card> auth = atmRepository.findOne(Integer.valueOf(accountNumber));
        boolean isValid = false;
        if (auth.isPresent()) {
            Card card = auth.get();
            if (Integer.valueOf(pin).equals(card.getPin())) {
                setCurrentUser(card);
                state = State.AUTHENTICATED;
                isValid = true;
            }
        }
        if (!isValid) System.out.println("Invalid account number / pin");
    }
    
    private boolean validateAuth(String input, String s) {
	if (input.length() != 6) {
	    System.out.printf("%s should have 6 digits length%n", s);
	    return false;
	}
	if (!input.matches("^[0-9]+")) {
	    System.out.printf("%s should only contains number%n", s);
	    return false;
	}
	return true;
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
                if (!amountInput.matches("^[0-9]+")) {
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
        try {
            String date = LocalDateTime.now().format(dateTimeFormatter);
            Transaction.Status status = atmRepository
                    .withdrawMoney(amount, currentUser, date);
            if (status == Transaction.Status.FAILED) {
                System.out.println("Withdraw failed.");
            } else {
                System.out.printf("Summary%nDate : %s%nWithdraw : $%d%nBalance: $%d%n%n",
                        date, amount, currentUser.getAccountBalance());
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please select appropriate input.");
        }

        return afterSummary();
    }

    private void depositMoney() {
        int[] amountList = Constant.AMOUNT_OPTIONS;
        System.out.println("==============\n1. $10\n2. $50\n3. $100\n4. Other\n5. Back\n");
        int amount = stdIn.nextInt();
        try {
            Transaction.Status status = atmRepository
                    .depositMoney(amountList[amount - 1], currentUser);
            if (status == Transaction.Status.FAILED) {
                System.out.println("Deposit failed.");
            } else {
                System.out.println("Deposit success.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please select appropriate input.");
        }
    }

    private boolean transferMoney() {
        System.out.println("=================");
        System.out.println("||  Transfer  ||");
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
        System.out.println("1. Confirm transfer\n2. Cancel");
        int confirm = stdIn.nextInt();
        if (confirm == 2) return true;
        
        if (!destinationInput.matches("^[0-9]+")) {
            System.out.println("Invalid account");
            return true;
        }
        
        if (!amountInput.matches("^[0-9]+")) {
            System.out.println("Invalid amount");
            return true;
        }
        
        int amount = Integer.parseInt(amountInput);
        if (amount < Constant.MIN_WITHDRAW_AMOUNT) {
            System.out.printf("Minimum amount to transfer is $%d", Constant.MIN_WITHDRAW_AMOUNT);
            return true;
        }
        
        if (amount > Constant.MAX_WITHDRAW_AMOUNT) {
            System.out.printf("Maximum amount to transfer is $%d%n", Constant.MAX_WITHDRAW_AMOUNT);
            return true;
        }
        
        String date = LocalDateTime.now().format(dateTimeFormatter);
        
        Transaction.Status status = atmRepository
                .transferMoney(amount, currentUser, destinationInput, ref, date);
        if (status == Transaction.Status.FAILED) {
            System.out.println("Transfer failed.\n");
        } else {
            System.out.printf("Fund Transfer Summary (%s) %nDestination account\t: %s %nAmount\t\t\t: $%s %nRef. Number\t\t: %s %nBalance\t\t\t: $%d %n%n",
                    date, destinationInput, amount, ref, currentUser.getAccountBalance());
        }

        return afterSummary();
    }

    private void viewTransactionHistory() {
        atmRepository.displayTransactionHistory(currentUser);
    }

    private void getBalance() {
        System.out.printf("Your balance: $%d%n%n", currentUser.getAccountBalance());
    }

    State getState() {
        return state;
    }

    private void removeAuth() {
        setCurrentUser(null);
        stdIn.reset();
        state = State.IDLE;
    }

    private boolean afterSummary() {
        int selected = 99;
        while (selected != 2) {
            System.out.println(
                    "1. Transaction\n" +
                    "2. Exit\n" +
                    "Choose option[2]: ");
            selected = stdIn.nextInt();
            if (selected == 1) return true;
        }
        removeAuth();
        return false;
    }

    void displayMenu() {
        int selected = 99;
        while (selected != 3) {
            System.out.println(
                    "=====================\n" +
                    "||     Welcome     ||\n" +
                    "=====================\n" +
                    "1. Withdraw\n" +
                    "2. Transfer\n" +
                    "3. Exit\n" +
                    "Choose option[3]: ");
            selected = stdIn.nextInt();
            switch (selected) {
                case 1: {
                    if(!withdrawMoney()) return;
                    break;
                }
                case 2: {
                    if (!transferMoney()) return;
                    break;
                }
                case 3: {
                    removeAuth();
                    break;
                }
            }
        }
    }
}

