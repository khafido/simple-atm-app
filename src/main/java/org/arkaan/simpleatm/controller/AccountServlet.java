package org.arkaan.simpleatm.controller;

import org.arkaan.simpleatm.dto.request.AuthDto;
import org.arkaan.simpleatm.dto.response.AccountDto;
import org.arkaan.simpleatm.dto.response.Response;
import org.arkaan.simpleatm.error.AuthenticationException;
import org.arkaan.simpleatm.model.Account;
import org.arkaan.simpleatm.model.Status;
import org.arkaan.simpleatm.service.AccountService;
import org.arkaan.simpleatm.util.AuthUtil;
import org.arkaan.simpleatm.util.Mapper;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class AccountServlet extends HttpServlet {

    private AccountService accountService;

    public AccountServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String account = req.getParameter("account");
        String pin = req.getParameter("pin");
        AuthDto authDto = new AuthDto(Integer.parseInt(account), Integer.parseInt(pin));
        Response<AccountDto> auth = authenticate(authDto);
        
        if (auth.getStatus().equals(Status.FAILED)) {
            PrintWriter writer = resp.getWriter();
            writer.write(auth.getMsg());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.close();
            return;
        }
        
        String base64 = AuthUtil.encodeBase64(auth.getPayload());
        Cookie authCookie = new Cookie("auth", base64);
        resp.addCookie(authCookie);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private Response<AccountDto> authenticate(AuthDto dto) {
        try {
            Account account = accountService.authenticate(dto.getAccountNumber(), dto.getPin());
            AccountDto payload = Mapper.mapAccountDto(account);
            return new Response<>(Status.SUCCESS, "success", payload);
        } catch (AuthenticationException e) {
            return new Response<>(Status.FAILED, e.getMessage(), null);
        }
    }
}
