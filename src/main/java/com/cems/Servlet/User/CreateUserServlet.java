/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.cems.Servlet.User;

import com.cems.Enums.UserRoles;
import com.cems.Model.Users;
import com.cems.Utils.AuthUtils;
import com.cems.database.UserManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author MarkYu
 */
@WebServlet(name = "CreateUserServlet", value = "/createUser")
public class CreateUserServlet extends HttpServlet {

    private UserManager userManager;
    
    @Override
    public void init() {
        userManager = new UserManager();
    }
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!AuthUtils.isLogged(request)) {
            AuthUtils.redirectToLogin(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        request.setAttribute("user", user);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/user/createUser.jsp");
        requestDispatcher.forward(request, response);
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!AuthUtils.isLogged(request)) {
            AuthUtils.redirectToLogin(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        
        if(!user.getRole().equals(UserRoles.ADMIN)){
            response.sendRedirect(request.getContextPath());
        }

        request.setAttribute("user", user);
        
        String username = request.getParameter("Username");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String password = request.getParameter("Password");
        String passwordConfirm = request.getParameter("passwordConfirm");
        int roleNum = Integer.parseInt(request.getParameter("Role"));
        
        UserRoles role = UserRoles.getRoles(roleNum);
        
        Users newUser = new Users();
        
        if (username.isEmpty() || username == null || firstName.isEmpty() || firstName == null || lastName.isEmpty() || lastName == null || password.isEmpty() || password == null || passwordConfirm.isEmpty() || passwordConfirm == null) {
            request.setAttribute("noUsername", "You need to enter All the information");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/user/createUser.jsp");
            try {
                requestDispatcher.forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        
        newUser.setUsername(username);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setRole(role);

        if (!password.isEmpty()) {
            if (!password.equals(passwordConfirm) || passwordConfirm.isEmpty()) {
                System.out.println("passwordDiff");
                System.out.println("Diff " + password + " " + passwordConfirm);
                System.out.println("newPasswordConfirm: [" + passwordConfirm + "]");
                request.setAttribute("passwordDiff", "The new password is different. Please check again.");
                System.out.println("newPassword: [" + password + "]");
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/user/createUser.jsp");
                try {
                    requestDispatcher.forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            newUser.setPassword(password);
        } else {
            newUser.setPassword(user.getPassword());
        }
        
        boolean createSuccess = userManager.CreateUser(newUser);
        
        if (!createSuccess) {
            request.setAttribute("createFailure", "This Username already exists.");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/user/createUser.jsp");
            try {
                requestDispatcher.forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        request.setAttribute("createSuccess", "Create Success");     
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/user/createUser.jsp");
        try {
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
