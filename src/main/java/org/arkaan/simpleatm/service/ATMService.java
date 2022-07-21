package org.arkaan.simpleatm.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.arkaan.simpleatm.model.Account;
import org.arkaan.simpleatm.model.Transaction;
import org.arkaan.simpleatm.model.Status;
import org.arkaan.simpleatm.dto.response.Response;
import org.arkaan.simpleatm.dto.response.TransferDto;
import org.arkaan.simpleatm.dto.response.WithdrawDto;
import org.arkaan.simpleatm.repository.Repository.AccountRepository;
import org.arkaan.simpleatm.repository.Repository.TransactionRepository;
import org.arkaan.simpleatm.model.Type;
import org.arkaan.simpleatm.util.Helper;

public class ATMService {
    
    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepo;
    private final DateTimeFormatter dateTimeFormatter;
    
    public ATMService(
            TransactionRepository transactionRepo, 
            AccountRepository accountRepo) {
        
        this.transactionRepo = transactionRepo;
        this.accountRepo = accountRepo;
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
    
    public Response<WithdrawDto> withdraw(int accountNumber, int amount) {
        Optional<Account> account = accountRepo.findOne(accountNumber);
        Status status = Status.FAILED;
        String detail = "Account not found.";
        WithdrawDto payload = null;

        if (account.isPresent()) {
            Account principal = account.get();
            String date = LocalDateTime.now().format(dateTimeFormatter);
            if (amount > principal.getBalance()) {
                detail = String.format("Insufficient balance: $%d [%s]", amount, date);
            } else {
                principal.reduceBalance(amount);
                status = Status.SUCCESS;
                detail = String.format("Amount: $%d [%s]", amount, date);
                payload = new WithdrawDto(date, amount, principal.getBalance());
            }

            transactionRepo.save(new Transaction(
                    Helper.nextTransactionId(), Type.WITHDRAWAL, detail, status, accountNumber));

        }
        
        return new Response<>(status, detail, payload);
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
                    Helper.nextTransactionId(), Type.DEPOSIT, detail, status, accountNumber));
        } else {
            System.out.println("Account not found");
        }
        
        return status;
    }
    
    public Response<TransferDto> transfer(int accountNumber, int destination, int amount, String ref) {
        Optional<Account> account = accountRepo.findOne(accountNumber);
        Optional<Account> dest = accountRepo.findOne(destination);
        
        Status status = Status.FAILED;
        String msg = "Account not found.";
        TransferDto payload = null;
        
        if (account.isPresent() && dest.isPresent()) {
            Account sender = account.get();
            Account receiver = dest.get();
            String date = LocalDateTime.now().format(dateTimeFormatter);
            
            if (sender.getBalance() > amount) {
                sender.reduceBalance(amount);
                receiver.addBalance(amount);
                String senderDetail = String.format("Destination: %d. Amount: $%d. Ref: %s. [%s]",
                        destination, amount, ref, date);
                String receiverDetail = String.format("From: %d. Amount: $%d. Ref: %s. [%s]",
                        accountNumber, amount, ref, date);
                
                status = Status.SUCCESS;
                
                transactionRepo.save(new Transaction(
                        Helper.nextTransactionId(), Type.TRANSFER, senderDetail, status, accountNumber));
                transactionRepo.save(new Transaction(
                        Helper.nextTransactionId(), Type.TRANSFER, receiverDetail, status, destination));
                payload = new TransferDto(destination, amount, ref, sender.getBalance(), date);
                msg = "Transfer Success.";
            } else {
                msg = String.format("Insufficient balance: $%d [%s]", amount, date);
                transactionRepo.save(new Transaction(
                        Helper.nextTransactionId(), Type.TRANSFER, msg, status, accountNumber));
            }
        }
        
        return new Response<>(status, msg, payload);
    }
    
    public Response<List<Transaction>> getTransactionHistory(int accountNumber) {
        Optional<Account> account = accountRepo.findOne(accountNumber);
        String msg = "Account not found";
        List<Transaction> payload = null;
        Status status = Status.FAILED;

        if (account.isPresent()) {
            List<Transaction> transactionList = transactionRepo.findByAccountNumber(accountNumber);
            int length = transactionList.size();
            if (length > 10) transactionList = transactionList.subList(length - 10, length);
            Collections.reverse(transactionList);
            payload = transactionList;
            status = Status.SUCCESS;
            msg = "Success";
        }

        return new Response<>(status, msg, payload);
    }
    
    public int getAccountBalance(int accountNumber) {
        Optional<Account> account = accountRepo.findOne(accountNumber);
        if (!account.isPresent()) {
            System.out.println("Account not found");
            return -99;
        }
        return account.get().getBalance();
    }
    
    public void saveAll() {
        accountRepo.saveAll();
        transactionRepo.saveAll();
    }
}
