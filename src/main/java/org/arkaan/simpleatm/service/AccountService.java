package org.arkaan.simpleatm.service;

import org.arkaan.simpleatm.dto.internal.PrincipalDto;
import org.arkaan.simpleatm.error.AuthenticationException;
import org.arkaan.simpleatm.error.DuplicateAccountNumberException;
import org.arkaan.simpleatm.error.NotFoundException;
import org.arkaan.simpleatm.model.Account;
import org.arkaan.simpleatm.repository.Repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public AccountService(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    public Account getOne(int accountNumber) {
        Optional<Account> one = accountRepository.findOne(accountNumber);
        if (!one.isPresent()) {
            throw new NotFoundException("Account");
        }
        return one.get();
    }

    public Account addOne(PrincipalDto dto) throws DuplicateAccountNumberException {
        Account account = new Account(
                dto.getAccountNumber(),
                dto.getPin(),
                dto.getName(),
                dto.getBalance());

        return accountRepository.save(account);
    }

    public Account authenticate(int accountNumber, int pin) {
        try {
            Account account = getOne(accountNumber);
            if (account.getPin() != pin) {
                throw new AuthenticationException();
            }
            return account;
        } catch (NotFoundException e) {
            throw new AuthenticationException();
        }
    }

    public Account withdrawFund(int accountNumber, int amount) {
        return null;
    }
}
