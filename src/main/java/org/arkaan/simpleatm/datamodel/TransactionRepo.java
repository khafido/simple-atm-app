package org.arkaan.simpleatm.datamodel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.arkaan.simpleatm.datamodel.Transaction.Status;

public class TransactionRepo extends AbstractRepository<Transaction> {
    
    public TransactionRepo() {
        super(new ArrayList<>(), null);
    }
    
    public TransactionRepo(String csvPath) {
        super(new ArrayList<>(), csvPath);
    }
    
    // type, detail, status, account number
    
    @Override
    protected void initData(String csvPath) {
        try (FileReader fileReader = new FileReader(csvPath)) {
            BufferedReader reader = new BufferedReader(fileReader);
            while (reader.ready()) {
                String[] row = reader.readLine().split(",");
                save(new Transaction(
                        Type.valueOf(row[0]),
                        row[1], Status.valueOf(row[2]),
                        Integer.parseInt(row[3])));
            }
        } catch (IOException e) {
            System.out.println("File not found");
            System.exit(0);
        } catch (NumberFormatException e) {
            System.out.println("CSV file contains invalid format");
            System.exit(0);
        }
    }
    
    @Override
    public Optional<Transaction> findOne(int id) {
        return data.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public Transaction save(Transaction newTransaction) {
        data.add(newTransaction);
        return newTransaction;
    }
    
    @Override
    public Transaction update(int id, Transaction newData) {
        Optional<Transaction> tr = findOne(id);
        if (tr.isPresent()) {
            data.set(id, newData);
            return newData;
        }
        return null;
    }
    

    @Override
    public Transaction remove(int id) {
        Optional<Transaction> tr = findOne(id);
        if (tr.isPresent()) {
            data.remove(id);
            return tr.get();
        }
        return null;
    }
    
    public List<Transaction> findByAccountNumber(int accountNumber) {
        return data.stream()
                .filter(it -> it.getAccountNumber() == accountNumber)
                .collect(Collectors.toList());
    }
}
