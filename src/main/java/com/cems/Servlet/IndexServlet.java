package com.cems.Servlet;

import com.cems.Utils.AuthUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "indexServlet", value = "/")
public class IndexServlet extends HttpServlet {

    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        if (!AuthUtils.isLogged(request)) {
            AuthUtils.redirectToLogin(request, response);
            return;
        }
        try {
            request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
