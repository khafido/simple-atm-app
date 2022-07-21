package org.arkaan.simpleatm.service;


import org.arkaan.simpleatm.dto.internal.PrincipalDto;
import org.arkaan.simpleatm.error.AuthenticationException;
import org.arkaan.simpleatm.error.DuplicateAccountNumberException;
import org.arkaan.simpleatm.error.NotFoundException;
import org.arkaan.simpleatm.model.Account;
import org.arkaan.simpleatm.repository.Repository.AccountRepository;
import org.arkaan.simpleatm.util.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {

    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final TransactionService transactionService = mock(TransactionService.class);
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountRepository, transactionService);
    }

    @Test
    void getOne_willThrowNotFoundException_whenAccountNotInDatabase() {
        int accountNumber = 123456;
        when(accountRepository.findOne(accountNumber)).thenReturn(Optional.empty());

        NotFoundException result = assertThrows(NotFoundException.class,
                () -> accountService.getOne(accountNumber));

        assertEquals("Account not found.", result.getMessage());
    }

    @Test
    void getOne_willReturnAccount_whenAccountIsInDatabase() {
        int accountNumber = 777555;
        Account account = new Account(accountNumber, 123456, "account", 777);
        when(accountRepository.findOne(accountNumber)).thenReturn(Optional.of(account));

        Account result = accountService.getOne(accountNumber);

        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
        assertEquals("account", result.getName());
        assertEquals(777, result.getBalance());
    }

    @Test
    void addOne_willThrowDuplicateAccountNumberException_whenAccountNumberAlreadyExist() {
        when(accountRepository.save(any(Account.class)))
                .thenThrow(new DuplicateAccountNumberException(776643));

        PrincipalDto add = new PrincipalDto(776643, 123456, "acc", 778);

        DuplicateAccountNumberException result = assertThrows(DuplicateAccountNumberException.class,
                () -> accountService.addOne(add));

        assertEquals("There can't be 2 different accounts with the same Account Number: 776643",
                result.getMessage());
    }

    @Test
    void addOne_WillReturnAccount_whenAccountNumberNotInDatabase() {
        PrincipalDto add = new PrincipalDto(776643, 123456, "acc", 778);
        Account account = new Account(
                add.getAccountNumber(), add.getPin(),
                add.getName(), add.getBalance());

        when(accountRepository.save(any(Account.class)))
                .thenReturn(account);

        Account result = accountService.addOne(add);

        assertNotNull(result);
        assertEquals(add.getAccountNumber(), result.getAccountNumber());
        assertEquals(add.getPin(), result.getPin());
        assertEquals(add.getName(), result.getName());
        assertEquals(add.getBalance(), result.getBalance());
    }

    @Test
    void authenticate_WillThrowAuthenticationException_whenPinIncorrect() {
        Account account = new Account(777666, 123432, "test", 888);
        when(accountRepository.findOne(777666)).thenReturn(Optional.of(account));
        int wrongPin = 434343;

        AuthenticationException result = assertThrows(AuthenticationException.class,
                () -> accountService.authenticate(777666, wrongPin));

        assertEquals(Constant.AUTH_FAIL_MESSAGE, result.getMessage());
    }
}
