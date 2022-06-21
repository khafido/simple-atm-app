package org.arkaan.simpleatm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.arkaan.simpleatm.datamodel.Account;
import org.arkaan.simpleatm.datamodel.AccountRepo;
import org.arkaan.simpleatm.datamodel.Transaction;
import org.arkaan.simpleatm.datamodel.Transaction.Status;
import org.arkaan.simpleatm.datamodel.TransactionRepo;
import org.arkaan.simpleatm.datamodel.Type;
import org.arkaan.simpleatm.util.Pair;

public class ATMService {
    
    private final TransactionRepo transactionRepo;
    private final AccountRepo accountRepo;
    private final DateTimeFormatter dateTimeFormatter;
    
    public ATMService(TransactionRepo transactionRepo, AccountRepo accountRepo) {
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    }
    
    public boolean authenticate(int accountNumber, int pin) {
        Optional<Account> account = accountRepo.findOne(accountNumber);
        boolean result = false;
        if (account.isPresent()) {
            Account principal = account.get();
            if (principal.getPin() == pin) {
                result = true;
            }
        }
        return result;
    }
    
    public Status withdraw(int accountNumber, int amount) {
        Optional<Account> account = accountRepo.findOne(accountNumber);
        Status status = Status.FAILED;
        
        if (account.isPresent()) {
            Account principal = account.get();
            String detail;
            String date = LocalDateTime.now().format(dateTimeFormatter);
            if (amount > principal.getBalance()) {
                detail = String.format("Insufficient balance: $%d [%s]", amount, date);
                System.out.println(detail);
            } else {
                principal.reduceBalance(amount);
                status = Status.SUCCESS;
                detail = String.format("Amount: $%d [%s]", amount, date);
                System.out.printf("Withdrawn: $%d [%s] %n", amount, date);
            }
            transactionRepo.save(new Transaction(
                    Type.WITHDRAWAL, detail, status, accountNumber));
        }
        
        return status;
    }
    
    public Status deposit(int accountNumber, int amount) {
        Optional<Account> account = accountRepo.findOne(accountNumber);
        Status status = Status.FAILED;
        
        if (account.isPresent()) {
            Account principal = account.get();
            String date = LocalDateTime.now().format(dateTimeFormatter);
            principal.addBalance(amount);
            status = Status.SUCCESS;
            String detail = String.format("Amount: $%d [%s]", amount, date);
            
            transactionRepo.save(new Transaction(
                    Type.DEPOSIT, detail, status, accountNumber));
        } else {
            System.out.println("Account not found");
        }
        
        return status;
    }
    
    public Pair<Status, String> transfer(int accountNumber, int destination, int amount, String ref) {
        Optional<Account> account = accountRepo.findOne(accountNumber);
        Optional<Account> dest = accountRepo.findOne(destination);
        
        Status status = Status.FAILED;
        String msg = "Transfer failed.\n";
        
        if (account.isPresent() && dest.isPresent()) {
            Account sender = account.get();
            Account receiver = dest.get();
            String date = LocalDateTime.now().format(dateTimeFormatter);
            
            if (sender.getBalance() > amount) {
                sender.reduceBalance(amount);
                receiver.addBalance(amount);
                String senderDetail = String.format("Destination: xxx%.3d. Amount: $%d. Ref: %s. [%s]",
                        destination, amount, ref, date);
                String receiverDetail = String.format("From: xxx%.3d. Amount: $%d. Ref: %s. [%s]",
                        accountNumber, amount, ref, date);
                
                status = Status.SUCCESS;
                
                transactionRepo.save(new Transaction(
                        Type.TRANSFER, senderDetail, status, accountNumber));
                transactionRepo.save(new Transaction(
                        Type.TRANSFER, receiverDetail, status, destination));
                
                msg = String.format("Transfer Summary (%s) %nDestination account\t: %s %nAmount\t\t\t: $%s %nRef. Number\t\t: %s %nBalance\t\t\t: $%d %n",
                        date, destination, amount, ref, sender.getBalance());
            } else {
                String detail = String.format("Insufficient balance: $%d [%s]", amount, date);
                System.out.println(detail);
                
                transactionRepo.save(new Transaction(
                        Type.TRANSFER, detail, status, accountNumber));
            }
        }
        
        return new Pair<Status, String>(status, msg);
    }
    
    public void displayTransactionHistory(int accountNumber) {
        Optional<Account> account = accountRepo.findOne(accountNumber);
        if (!account.isPresent()) {
            System.out.println("Account not found");
            return;
        }
        
        System.out.println("===================\nTransaction History\n");
        System.out.printf("%-5s %-15s %-10s %s %n", "NO", "TYPE", "STATUS", "DETAIL");
        
        List<Transaction> transactionList = transactionRepo.findByAccountNumber(accountNumber);
        int length = transactionList.size();
        
        if (length > 10) transactionList = transactionList.subList(length - 10, length);
        
        Collections.reverse(transactionList);
        
        int i = 1;
        
        for (Transaction tr : transactionList) {
            System.out.printf("%-5s %-15s %-10s %s %n", i++, tr.getType(), tr.getStatus(), tr.getDetail());
        }
        System.out.println();
    }
    
    public int getAccountBalance(int accountNumber) {
        Optional<Account> account = accountRepo.findOne(accountNumber);
        if (!account.isPresent()) {
            System.out.println("Account not found");
            return -99;
        }
        return account.get().getBalance();
    }
    
    public void saveToFile() {
        accountRepo.saveAll();
        transactionRepo.saveAll();
    }
}
