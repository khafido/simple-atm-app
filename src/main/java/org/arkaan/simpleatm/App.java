package org.arkaan.simpleatm;

import org.arkaan.simpleatm.datamodel.ATMRepository;
import org.arkaan.simpleatm.datamodel.Card;

class App {

    public static void main(String[] args) {
        ATMRepository atmRepository = new ATMRepository();
        atmRepository.addAccount(new Card(123456, "user1", 1_000, 776643));
        atmRepository.addAccount(new Card(123456, "user2", 1_000, 774921));
        atmRepository.addAccount(new Card(123456, "user3", 1_000, 777106));
        SimpleAtm app = new SimpleAtm(atmRepository);

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
                case OFFLINE: {
                    break;
                }
            }
        } while (app.getState() != SimpleAtm.State.OFFLINE);
    }
}
