package com.cems.Servlet.Auth;

import com.cems.Model.Users;
import com.cems.Utils.AuthUtils;
import com.cems.database.UserManager;
import com.mysql.cj.Session;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "logoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    public void init() {
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession(false).invalidate();
        response.sendRedirect(request.getContextPath() + "/login");
//        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/auth/logout.jsp");
//        try {
//            requestDispatcher.forward(request, response);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
