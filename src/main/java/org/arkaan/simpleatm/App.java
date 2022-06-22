package org.arkaan.simpleatm;

import org.arkaan.simpleatm.util.Pair;
import org.arkaan.simpleatm.datamodel.Transaction;
import org.arkaan.simpleatm.datamodel.Transaction.Status;
import org.arkaan.simpleatm.repository.AccountRepo;
import org.arkaan.simpleatm.repository.TransactionRepo;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

class SimpleAtm {
    enum State {
        AUTHENTICATED,
        IDLE,
        OFFLINE
    }

    private State state;
    private final Scanner stdIn;
    private final Random random;
    private final ATMService atmService;
    private int currentAccount;
    
    SimpleAtm(ATMService atmService) {
        state = State.IDLE;
        stdIn = new Scanner(System.in);
        random = new Random();
        this.atmService = atmService;
    }
    
    private void setCurrentAccount(int accountNumber) {
        currentAccount = accountNumber;
    }

    void authenticate() {
        System.out.println("===========================");
        System.out.println("||          ATM          ||");
        System.out.println("===========================");
        System.out.println("Enter your account number:");
        String account = stdIn.next();
        if (!validateAuth(account, "Account Number")) return;
        System.out.println("Enter your pin:");
        String pin = stdIn.next();
        if (!validateAuth(pin, "PIN")) return;
        
        int accountNumber = Integer.parseInt(account);
        boolean isValid = atmService.authenticate(accountNumber, Integer.parseInt(pin));
        if (isValid) {
            state = State.AUTHENTICATED;
            setCurrentAccount(accountNumber);
        } else {
            System.out.println("Invalid account number / pin");
        }
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

    private void withdrawMoney() {
        Integer[] amountList = {10, 50, 100};
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
                amount = Integer.valueOf(amountInput);
                if (amount % 10 != 0) {
                    System.out.println("Invalid amount");
                    continue;
                }
                if (amount > 1000.0) {
                    System.out.println("Maximum amount to withdraw is $1000");
                    continue;
                }
                break;
            }
        } else if (amountSelect >= 5) {
            return;
        } else {
            amount = amountList[amountSelect - 1];
        }
        
        Transaction.Status status = atmService.withdraw(currentAccount, amount);
        if (status == Transaction.Status.FAILED) {
            System.out.println("Withdraw failed.");
        } else {
            System.out.println("Withdraw success.");
        }
    }

    private void depositMoney() {
        Integer[] amountList = {10, 50, 100};
        System.out.println("==============\n1. $10\n2. $50\n3. $100\n4. Other\n5. Back\n");
        int amount = stdIn.nextInt();
        try {
            Transaction.Status status = atmService
                    .deposit(currentAccount, amountList[amount - 1]);
            if (status == Transaction.Status.FAILED) {
                System.out.println("Deposit failed.");
            } else {
                System.out.println("Deposit success.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please select appropriate input.");
        }
    }

    private void transferMoney() {
        System.out.println("=================");
        System.out.println("||  Transfer  ||");
        System.out.print("Enter account number (destination): ");
        stdIn.nextLine();
        String destinationInput = stdIn.nextLine();
        if (destinationInput.trim().isEmpty()) return;
        System.out.print("Enter amount: ");
        String amountInput = stdIn.nextLine();
        if (amountInput.trim().isEmpty()) return;
        
        String ref = String.format("%04d", random.nextInt(999999));
        System.out.println("\n=================================");
        System.out.printf("Transfer Confirmation %nDestination account\t: %s %nAmount\t\t\t: $%s %nRef. Number\t\t: %s %n",
        	destinationInput, amountInput, ref);
        System.out.println("=================================\n");
        System.out.println("1. Confirm transfer\n2. Cancel");
        int confirm = stdIn.nextInt();
        if (confirm == 2) return;
        
        if (!destinationInput.matches("^[0-9]+")) {
            System.out.println("Invalid account");
            return;
        }
        
        if (!amountInput.matches("^[0-9]+")) {
            System.out.println("Invalid amount");
            return;
        }
        
        Integer amount = Integer.valueOf(amountInput);
        if (amount < 1) {
            System.out.println("Minimum amount to transfer is $1");
            return;
        }
        
        if (amount > 1000) {
            System.out.println("Maximum amount to transfer is $1000");
            return;
        }
        
        Pair<Status, String> result = atmService.transfer(
                currentAccount, Integer.parseInt(destinationInput), amount, ref);
        
        if (result.getFirst() == Transaction.Status.FAILED) {
            System.out.println("Transfer failed.\n");
        } else {
            System.out.println("\n=================================");
            System.out.println(result.getSecond());
            System.out.println("=================================\n");
        }
    }

    private void viewTransactionHistory() {
        atmService.displayTransactionHistory(currentAccount);
    }

    private void getBalance() {
        System.out.printf("Your balance: $%d%n%n", atmService.getAccountBalance(currentAccount));
    }

    State getState() {
        return state;
    }

    void displayMenu() {
        int selected = 99;
        while (selected != 0) {
            System.out.println("=====================");
            System.out.println("||     Welcome     ||");
            System.out.println("=====================");
            System.out.println("1. Withdraw");
            System.out.println("2. Transfer");
            System.out.println("3. Deposit");
            System.out.println("4. View Balance");
            System.out.println("5. Transaction History");
            System.out.println("0. Exit");
            selected = stdIn.nextInt();
            switch (selected) {
                case 1: {
                    withdrawMoney();
                    break;
                }
                case 2: {
                    transferMoney();
                    break;
                }
                case 3: {
                    depositMoney();
                    break;
                }
                case 4: {
                    getBalance();
                    break;
                }
                case 5: {
                    viewTransactionHistory();
                    break;
                }
                case 0: {
                    atmService.saveToFile();
                    setCurrentAccount(-99);
                    stdIn.reset();
                    state = State.IDLE;
                }
            }
        }
    }
}

public class App {
    
    public static void main(String[] args) {
        AccountRepo accountRepo;
        TransactionRepo transactionRepo;
        
        if (args.length == 0) {
            System.out.println("Please provide csv file");
            return;
        } else {
            accountRepo = new AccountRepo(args[0]);
            
            File resource = new File(System.getProperty("user.home") + "/atm-simulation"); 
            String trxCsvPath;
            
            if (!resource.exists()) {
                resource.mkdir();
            }
            
            File trxCsv = new File(resource.getPath() + "/transactions.csv");
            try {
                trxCsv.createNewFile();
            } catch (IOException e) {
                System.out.println("Failed to create file");
            }
            
            trxCsvPath = trxCsv.getPath();
            System.out.println("Transaction history file: " + trxCsvPath + "\n");
            transactionRepo = new TransactionRepo(trxCsvPath);
        }
        
        ATMService atmService = new ATMService(transactionRepo, accountRepo);
        
        SimpleAtm atm = new SimpleAtm(atmService);

        do {
            try {                
                switch (atm.getState()) {
                    case IDLE: {
                        atm.authenticate();
                        break;
                    }
                    case AUTHENTICATED: {
                        atm.displayMenu();
                        break;
                    }
                    case OFFLINE: {
                        break;
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.println("Exit..");
                System.exit(0);
            }
        } while (atm.getState() != SimpleAtm.State.OFFLINE);
    }
}
