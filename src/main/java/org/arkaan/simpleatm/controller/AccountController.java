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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class AccountController extends HttpServlet {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        Account account = accountService.authenticate(776643, 123456);
        writer.write("<h1> Hello From " + account.getName() + " </h1>");
        writer.close();
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
