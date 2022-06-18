package org.arkaan.simpleatm.datamodel;

import org.arkaan.simpleatm.datamodel.Transaction.Status;
import org.arkaan.simpleatm.util.DuplicateAccountNumberException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Optional;

public class ATMRepository {
    private final Set<Card> availableAccountList = new HashSet<>();

    public void addAccount(Card account) {
        boolean check = availableAccountList.add(account);
        if (!check) throw new DuplicateAccountNumberException(
                account.getAccountNumber());
    }

    public void closeAccount(Card account) {
        availableAccountList.remove(account);
    }

    public Optional<Card> findOne(Integer accountNumber) {
        return availableAccountList.stream()
                .filter(it -> it.getAccountNumber().equals(accountNumber))
                .findFirst();
    }

    public Status withdrawMoney(Integer amount, Card card, String date) {
        Account customer = card.getCustomer();
        assert customer != null;
        Status status;
        if (amount > customer.getBalance()) {
            System.out.printf("Insufficient balance: $%d %n", amount);
            status = Status.FAILED;
        } else {
            customer.reduceBalance(amount);
            status = Status.SUCCESS;
        }
        String desc = String.format("Amount: $%d [%s]", amount, date);
        customer.addTransaction(
                new Transaction(Type.WITHDRAWAL, desc, status));
        return status;
    }

    public Status depositMoney(Integer amount, Card card) {
        Account customer = card.getCustomer();
        customer.addBalance(amount);
        customer.addTransaction(
                new Transaction(
                        Type.DEPOSIT, 
                        String.format("Amount: $%d", amount), 
                        Status.SUCCESS));
        return Status.SUCCESS;
    }

    public Status transferMoney(Integer amount, Card from, String to, String ref, String date) {
        Optional<Card> byAccountNumber = findOne(Integer.valueOf(to));
        Account sender = from.getCustomer();
        Status status;
        if (byAccountNumber.isPresent()) {
            if (sender.getBalance() > amount) {
                Account recipient = byAccountNumber.get().getCustomer();
                sender.reduceBalance(amount);
                recipient.addBalance(amount);
                status = Status.SUCCESS;
                
                String msg = String.format("Destination: xxx%s. Amount: $%d. Ref: %s. [%s]",
                        to.substring(to.length() - 3), amount, ref, date);
                
                sender.addTransaction(
                        new Transaction(Type.TRANSFER, msg, status));
            } else {
            	String msg = String.format("Insufficient balance: $%d", amount);
            	System.out.println(msg);
            	status = Status.FAILED;
            	sender.addTransaction(
                            new Transaction(Type.TRANSFER, String.format("%s [%s]", msg, date), status));
            }
        } else {
            System.out.println("Invalid account");
            status = Status.FAILED;
        }
        return status;
    }

    public void displayTransactionHistory(Card card) {
        Account customer = card.getCustomer();
        System.out.println("===================\nTransaction History\n");
        System.out.printf("%-5s %-15s %-10s %s %n", "NO", "TYPE", "STATUS", "DETAIL");
        
        List<Transaction> transactionList = customer.getTransactionList();
        int length = transactionList.size();
        
        if (length > 10) {
            transactionList = transactionList.subList(length - 10, length);
        }
        
        Collections.reverse(transactionList);
        
        int i = 1;
        for (Transaction tr : transactionList) {
            System.out.printf("%-5s %-15s %-10s %s %n", i++, tr.getType(), tr.getStatus(), tr.getDetail());
        }
        System.out.println();
    }
}
