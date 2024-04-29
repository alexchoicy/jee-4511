package com.cems.Servlet.Auth;

import com.cems.Model.User;
import com.cems.Utils.AuthUtils;
import com.cems.database.UserManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    private UserManager userManager;

    @Override
    public void init() {
        userManager = new UserManager();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        if (AuthUtils.isLogged(request)) {
            AuthUtils.redirectToIndex(request, response);
            return;
        }
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp");
        try {
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = userManager.Login(username, password);
        if (user == null) {
            request.setAttribute("error", "Invalid username or password");
            request.setAttribute("username", username);
            request.setAttribute("password", password);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp");
            try {
                requestDispatcher.forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(60 * 60 * 24);

        AuthUtils.redirectToIndex(request, response);
    }

}
