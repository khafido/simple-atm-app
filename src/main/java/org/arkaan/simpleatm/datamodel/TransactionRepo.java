package org.arkaan.simpleatm.datamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionRepo implements Repository<Transaction> {
    
    private List<Transaction> transactionList;
    
    public TransactionRepo() {
        transactionList = new ArrayList<Transaction>();
    }
    
    @Override
    public Optional<Transaction> findOne(int id) {
        return transactionList.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public Transaction save(Transaction data) {
        boolean result = transactionList.add(data);
        return result ? data : null;
    }
    
    @Override
    public Transaction update(int id, Transaction data) {
        Optional<Transaction> tr = findOne(id);
        if (tr.isPresent()) {
            transactionList.add(id, data);
            return data;
        }
        return null;
    }
    

    @Override
    public Transaction remove(int id) {
        Optional<Transaction> tr = findOne(id);
        if (tr.isPresent()) {
            transactionList.remove(id);
            return tr.get();
        }
        return null;
    }
    
    public List<Transaction> findByAccountNumber(int accountNumber) {
        return transactionList.stream()
                .filter(it -> it.getAccountNumber() == accountNumber)
                .collect(Collectors.toList());
    }
}
