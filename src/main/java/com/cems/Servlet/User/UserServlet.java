package com.cems.Servlet.User;

import com.cems.Model.Users;
import com.cems.Utils.AuthUtils;
import com.cems.database.UserManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "getUserServlet", value = "/users")
public class UserServlet extends HttpServlet {

    private UserManager userManager;
    @Override
    public void init() {
        userManager = new UserManager();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!AuthUtils.isLoggedin(request)) {
            AuthUtils.rediectToLogin(request, response);
            return;
        }

        System.out.println("UserServlet doGet");
        ArrayList<Users> users = userManager.getUsers();
//        System.out.println(users);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/user/users.jsp");
        request.setAttribute("users", users);
        requestDispatcher.forward(request, response);

    }
}
