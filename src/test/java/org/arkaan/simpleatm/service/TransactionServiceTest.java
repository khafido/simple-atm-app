package org.arkaan.simpleatm.service;

import org.arkaan.simpleatm.dto.internal.TransactionDto;
import org.arkaan.simpleatm.model.Account;
import org.arkaan.simpleatm.model.Status;
import org.arkaan.simpleatm.model.Transaction;
import org.arkaan.simpleatm.model.Type;
import org.arkaan.simpleatm.repository.Repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionServiceTest {

    private final TransactionRepository repository = mock(TransactionRepository.class);
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(repository);
    }

    @Test
    void addOne_WillReturnNewTransaction() {
        Type type = Type.TRANSFER;
        String detail = "transfer";
        Status status = Status.SUCCESS;
        LocalDate date = LocalDate.now();
        Account account = mock(Account.class);
        Transaction tr = new Transaction(type, detail, status, date, account);

        when(repository.save(any(Transaction.class)))
                .thenReturn(tr);

        TransactionDto dto = new TransactionDto(type, detail, status, date, account);

        Transaction result = transactionService.addOne(dto);

        assertNotNull(result);
        assertEquals("transfer", result.getDetail());
        assertEquals(Type.TRANSFER, result.getType());
    }
}
