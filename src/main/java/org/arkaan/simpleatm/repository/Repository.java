package org.arkaan.simpleatm.repository;

import java.util.List;
import java.util.Optional;

import org.arkaan.simpleatm.datamodel.Account;
import org.arkaan.simpleatm.datamodel.Transaction;

public interface Repository<T> {
    Optional<T> findOne(int id);
    T save(T data);
    T update(int id, T data);
    T remove(int id);
    void saveAll();
    
    interface AccountRepository extends Repository<Account> {
        
    }

    interface TransactionRepository extends Repository<Transaction> {
        List<Transaction> findByAccountNumber(int accountNumber);
    }

}