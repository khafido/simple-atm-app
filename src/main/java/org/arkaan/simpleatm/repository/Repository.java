package org.arkaan.simpleatm.repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

    default void saveToCsv(String csvPath, List<T> data) {
        try (FileWriter fileWriter = new FileWriter(csvPath, false)) {
            BufferedWriter writer = new BufferedWriter(fileWriter);
            for (T d : data) {
                writer.append(d.toString());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to save: file not found");
        }
    }

    interface AccountRepository extends Repository<Account> {

    }

    interface TransactionRepository extends Repository<Transaction> {
        List<Transaction> findByAccountNumber(int accountNumber);
    }

}