package org.arkaan.simpleatm.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.arkaan.simpleatm.datamodel.Account;
import org.arkaan.simpleatm.util.DuplicateAccountNumberException;

public class AccountRepo implements Repository.AccountRepository {
    // name, pin, balance, account number

    private final String csvPath;
    private final List<Account> data;

    public AccountRepo(String csvPath) {
        this.csvPath = csvPath;
        data = new ArrayList<>();
        initData();
    }

    private void initData() {
        System.out.println("Loading account data..");
        try (FileReader fileReader = new FileReader(csvPath)) {
            BufferedReader reader = new BufferedReader(fileReader);
            while (reader.ready()) {
                try {
                    String[] row = reader.readLine().split(",");
                    save(new Account(
                        Integer.parseInt(row[3]),
                        Integer.parseInt(row[1]),
                        row[0], 
                        Integer.parseInt(row[2])));
                } catch (DuplicateAccountNumberException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Skipped duplicate");
                }
            }
        } catch (IOException e) {
            System.out.println("File not found");
            System.exit(0);
        } catch (NumberFormatException e) {
            System.out.println("CSV file contains invalid format");
            System.exit(0);
        }
        System.out.println("Done.\n");
    }

    @Override
    public Optional<Account> findOne(int id) {
        return data.stream()
                .filter(it -> it.getAccountNumber() == id)
                .findFirst();
    }

    @Override
    public Account save(Account newAccount) {
        boolean check = findOne(newAccount.getAccountNumber()).isPresent();
        if (!check) {
            this.data.add(newAccount);
        } else {
            throw new DuplicateAccountNumberException(newAccount.getAccountNumber());
        }
        return newAccount;
    }

    @Override
    public Account update(int id, Account data) {
        return null;
    }

    @Override
    public Account remove(int id) {
        return null;
    }

    @Override
    public void saveAll() {
        saveToCsv(csvPath, data);
    }
}
