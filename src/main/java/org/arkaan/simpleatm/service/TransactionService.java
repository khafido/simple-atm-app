package org.arkaan.simpleatm.service;

import org.arkaan.simpleatm.dto.internal.TransactionDto;
import org.arkaan.simpleatm.model.Transaction;
import org.arkaan.simpleatm.repository.Repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction addOne(TransactionDto dto) {
        Transaction transaction = new Transaction(dto.getType(), dto.getDetail(),
                dto.getStatus(), dto.getDate(),
                dto.getAccount());
        return transactionRepository.save(transaction);
    }
}
