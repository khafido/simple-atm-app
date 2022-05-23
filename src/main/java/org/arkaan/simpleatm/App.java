package org.arkaan.simpleatm;

import org.arkaan.simpleatm.datamodel.Card;
import org.arkaan.simpleatm.repository.ATMRepository;

import java.util.Optional;
import java.util.Scanner;

public class App {

    private Card currentUser;
    private final ATMRepository atmRepository;

    App(ATMRepository atmRepository) {
        this.atmRepository = atmRepository;
    }

    private void setCurrentUser(Card currentUser) {
        this.currentUser = currentUser;
    }

    boolean authenticate() {
        System.out.println("Enter your card name:");
        Scanner in = new Scanner(System.in);
        String username = in.nextLine();
        Optional<Card> auth = atmRepository.findOne(username);
        if (auth.isPresent()) {
            Card card = auth.get();
            System.out.println("Enter your pin:");
            String pin = in.nextLine();
            if (pin.equals(card.getPin())) {
                setCurrentUser(card);
                return true;
            }
        }
        in.close();
        return false;
    }

    void getUserBalance() {
        if (currentUser == null) {
            System.out.println("no user");
        } else {
            System.out.println(currentUser.getCustomer().getBalance());
        }
    }
}

class SimpleAtm {
    public static void main(String[] args) {
        ATMRepository atmRepository = new ATMRepository();
        atmRepository.addAccount(new Card("123456", "user1", 1_000_000));
        App app = new App(atmRepository);
        boolean authenticate = app.authenticate();
        if (authenticate) {
            app.getUserBalance();
        } else {
            System.out.println("wrong credential");
        }
    }
}
