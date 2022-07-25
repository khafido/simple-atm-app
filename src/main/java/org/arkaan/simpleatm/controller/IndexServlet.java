package org.arkaan.simpleatm.controller;

import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String base64 = (String) req.getAttribute("token");
        PrintWriter pw = resp.getWriter();
        if (base64 == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            pw.write("Unauthorized");
            pw.close();
            return;
        }
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
