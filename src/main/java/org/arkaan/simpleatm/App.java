package org.arkaan.simpleatm;

import org.arkaan.simpleatm.datamodel.Card;
import org.arkaan.simpleatm.datamodel.ATMRepository;
import org.arkaan.simpleatm.datamodel.Transaction;

import java.util.Optional;
import java.util.Scanner;

public class App {
    enum State {
        AUTHENTICATED,
        IDLE,
        AUTHENTICATING,
        OFFLINE
    }

    private Card currentUser;
    private final ATMRepository atmRepository;
    private State state;
    private final Scanner stdIn;

    App(ATMRepository atmRepository) {
        this.atmRepository = atmRepository;
        state = State.IDLE;
        stdIn = new Scanner(System.in);
    }

    private void setCurrentUser(Card currentUser) {
        this.currentUser = currentUser;
    }

    void authenticate() {
        System.out.println("Enter your card name:");
        String username = stdIn.nextLine();
        Optional<Card> auth = atmRepository.findOne(username);
        if (auth.isPresent()) {
            Card card = auth.get();
            System.out.println("Enter your pin:");
            String pin = stdIn.nextLine();
            if (pin.equals(card.getPin())) {
                setCurrentUser(card);
                state = State.AUTHENTICATED;
            } else {
                System.out.println("Wrong pin");
            }
        } else {
            System.out.println("Invalid account");
        }
    }

    void withdrawMoney() {
        double[] amountList = {50_000, 100_000, 500_000};
        System.out.println("==============");
        System.out.println("1. Rp. 50000");
        System.out.println("2. Rp. 100000");
        System.out.println("3. Rp. 500000");
        System.out.print("Choose amount: ");
        int amount = stdIn.nextInt();
        try {
            Transaction.Status status = atmRepository
                    .withdrawMoney(amountList[amount - 1], currentUser);
            if (status == Transaction.Status.FAILED) {
                System.out.println("Withdraw failed.");
            } else {
                System.out.println("Success.");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please select appropriate input.");
        }
    }

    void depositMoney() {
        double[] amountList = {50_000, 100_000, 500_000};
        System.out.println("==============");
        System.out.println("1. Rp. 50000");
        System.out.println("2. Rp. 100000");
        System.out.println("3. Rp. 500000");
        System.out.print("Choose amount: ");
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

    void transferMoney() {
        System.out.println("=================");
        System.out.println("[ Transfer money ]");
        System.out.print("Enter account number (destination): ");
        String recipient = stdIn.next();
        System.out.print("Enter amount: ");
        double amount = stdIn.nextDouble();
        Transaction.Status status = atmRepository
                .transferMoney(amount, currentUser, Long.valueOf(recipient));
        if (status == Transaction.Status.FAILED) {
            System.out.println("Transfer failed. Please check your account balance or destination number");
        } else {
            System.out.println("Transaction success.");
        }
    }

    void viewTransactionHistory() {
        atmRepository.displayTransactionHistory(currentUser);

    }

    void getBalance() {
        System.out.printf("%s%f\n", "Your balance: Rp. ", currentUser.getAccountBalance());
    }

    State getState() {
        return state;
    }

    boolean isAuthenticated() {
        return currentUser != null;
    }

    void displayMenu() {
        int selected = 99;
        while (selected != 0) {
            System.out.println("=====================");
            System.out.println("||     Welcome     ||");
            System.out.println("=====================");
            System.out.println("1. Withdraw Money");
            System.out.println("2. Transfer Money");
            System.out.println("3. Deposit Money");
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
                    setCurrentUser(null);
                    stdIn.reset();
//                    state = State.IDLE;
                }
            }
        }
    }
}

class SimpleAtm {

    public static void main(String[] args) {
        ATMRepository atmRepository = new ATMRepository();
        atmRepository.addAccount(new Card("123456", "user1", 1_000_000, "0776648292736"));
        atmRepository.addAccount(new Card("123456", "user2", 1_000_000, "0774924824224"));
        atmRepository.addAccount(new Card("123456", "user3", 1_000_000, "0777103938848"));
        App app = new App(atmRepository);

        do {
            switch (app.getState()) {
                case IDLE: {
                    app.authenticate();
                    break;
                }
                case AUTHENTICATED: {
                    app.displayMenu();
                    break;
                }
            }
        } while (app.isAuthenticated());
    }
}
