package org.arkaan.simpleatm.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.arkaan.simpleatm.dto.response.AccountDto;
import org.arkaan.simpleatm.util.AuthUtil;
import org.arkaan.simpleatm.util.Constant;
import org.springframework.stereotype.Component;

@Component
public class WithdrawServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String base64 = (String) req.getAttribute("token");
        AccountDto account = AuthUtil.decodeBase64(base64);
        req.setAttribute("account", account);
        req.setAttribute("MAX_WITHDRAW_AMOUNT", Constant.MAX_WITHDRAW_AMOUNT);
        req.setAttribute("MIN_WITHDRAW_AMOUNT", Constant.MIN_WITHDRAW_AMOUNT);
        req.setAttribute("AMOUNT_OPTIONS", Constant.AMOUNT_OPTIONS);
        req.setAttribute("AMOUNT_MULTIPLE", Constant.AMOUNT_MULTIPLE);
        req.getRequestDispatcher("withdraw.jsp").forward(req, resp);
    }
}
