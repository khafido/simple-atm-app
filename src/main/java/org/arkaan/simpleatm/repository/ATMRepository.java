package org.arkaan.simpleatm.repository;

import org.arkaan.simpleatm.datamodel.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ATMRepository {
    private final List<Card> availableAccountList = new ArrayList<>();

    public void addAccount(Card account) {
        availableAccountList.add(account);
    }

    public void closeAccount(Card account) {
        availableAccountList.remove(account);
    }

    public Optional<Card> findOne(String username) {
        Card result = null;
        for (Card c : availableAccountList) {
            if (c.getCustomer().getName().equals(username)) {
                result = c;
                break;
            }
        }
        return Optional.ofNullable(result);
    }

    public List<Card> getAvailableAccountList() {
        return availableAccountList;
    }
}
