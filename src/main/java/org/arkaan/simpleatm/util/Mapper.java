package org.arkaan.simpleatm.util;

import org.arkaan.simpleatm.dto.response.AccountDto;
import org.arkaan.simpleatm.model.Account;

public final class Mapper {

    public static AccountDto mapAccountDto(Account account) {
        return new AccountDto(account.getAccountNumber(),
                account.getName(), account.getBalance());
    }
}
