package org.arkaan.simpleatm;

import org.arkaan.simpleatm.repository.AccountRepo;
import org.arkaan.simpleatm.repository.TransactionRepo;
import org.arkaan.simpleatm.service.ATMService;
import org.arkaan.simpleatm.controller.AtmController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.IOException;
import java.util.*;


@SpringBootApplication
public class App {
    
    public static void main(String[] args) {
        Map<String, Object> properties = new HashMap<>();

        new SpringApplicationBuilder(App.class)
                .properties(properties)
                .build()
                .run(args);
    }

    @Bean
    public CommandLineRunner runner() {
        return args -> {
            AccountRepo accountRepo;
            TransactionRepo transactionRepo;

            if (args.length == 0) {
                System.out.println("Please provide csv file");
                return;
            } else {
                accountRepo = new AccountRepo(args[0]);

                File resource = new File(System.getProperty("user.home") + "/atm-simulation");
                String trxCsvPath;

                if (!resource.exists()) {
                    resource.mkdir();
                }

                File trxCsv = new File(resource.getPath() + "/transactions.csv");
                try {
                    trxCsv.createNewFile();
                } catch (IOException e) {
                    System.out.println("Failed to create file");
                }

                trxCsvPath = trxCsv.getPath();
                System.out.println("Transaction history file: " + trxCsvPath + "\n");
                transactionRepo = new TransactionRepo(trxCsvPath);
            }

            ATMService atmService = new ATMService(transactionRepo, accountRepo);

            AtmController atm = new AtmController(atmService);

            do {
                try {
                    switch (atm.getState()) {
                        case IDLE: {
                            atm.authenticate();
                            break;
                        }
                        case AUTHENTICATED: {
                            atm.displayMenu();
                            break;
                        }
                        case OFFLINE: {
                            break;
                        }
                    }
                } catch (NoSuchElementException e) {
                    System.out.println("Exit..");
                    System.exit(0);
                }
            } while (atm.getState() != AtmController.State.OFFLINE);
        };
    }
}
