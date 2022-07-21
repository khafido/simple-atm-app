package org.arkaan.simpleatm.controller;

import org.arkaan.simpleatm.dto.request.AuthDto;
import org.arkaan.simpleatm.dto.response.AccountDto;
import org.arkaan.simpleatm.dto.response.Response;
import org.arkaan.simpleatm.error.AuthenticationException;
import org.arkaan.simpleatm.model.Account;
import org.arkaan.simpleatm.model.Status;
import org.arkaan.simpleatm.service.AccountService;
import org.arkaan.simpleatm.util.Mapper;
import org.springframework.stereotype.Controller;

@Controller
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public Response<AccountDto> authenticate(AuthDto dto) {
        try {
            Account account = accountService.authenticate(dto.getAccountNumber(), dto.getPin());
            AccountDto payload = Mapper.mapAccountDto(account);
            return new Response<>(Status.SUCCESS, "success", payload);
        } catch (AuthenticationException e) {
            return new Response<>(Status.FAILED, e.getMessage(), null);
        }
    }
}
