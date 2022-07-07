package org.arkaan.simpleatm.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.arkaan.simpleatm.datamodel.Transaction;
import org.arkaan.simpleatm.datamodel.Type;
import org.arkaan.simpleatm.datamodel.Transaction.Status;
import org.arkaan.simpleatm.util.Helper;

public class TransactionRepo implements Repository.TransactionRepository {

    private final String csvPath;
    private final List<Transaction> data;
    public TransactionRepo(String csvPath) {
        this.csvPath = csvPath;
        data = new ArrayList<>();
        initData();
    }
    
    // type, detail, status, account number

    protected void initData() {
        try (FileReader fileReader = new FileReader(csvPath)) {
            BufferedReader reader = new BufferedReader(fileReader);
            while (reader.ready()) {
                String[] row = reader.readLine().split(",");
                save(new Transaction(
                        Helper.nextTransactionId(),
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
        return null;
    }
    

    @Override
    public Transaction remove(int id) {
        return null;
    }
    
    @Override
    public List<Transaction> findByAccountNumber(int accountNumber) {
        return data.stream()
                .filter(it -> it.getAccountNumber() == accountNumber)
                .collect(Collectors.toList());
    }

    @Override
    public void saveAll() {
        saveToCsv(csvPath, data);
    }
}
