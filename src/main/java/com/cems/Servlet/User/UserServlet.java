package com.cems.Servlet.User;

import com.cems.Enums.UserRoles;
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
import javax.servlet.http.HttpSession;

@WebServlet(name = "getUserServlet", value = "/users")
public class UserServlet extends HttpServlet {

    private UserManager userManager;

    @Override
    public void init() {
        userManager = new UserManager();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!AuthUtils.isLogged(request)) {
            AuthUtils.redirectToLogin(request, response);
            return;
        }

        System.out.println("UserServlet doGet");
        ArrayList<Users> users = userManager.getUsers();
//        System.out.println(users);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/user/users.jsp");
        request.setAttribute("users", users);
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

        if (!user.getRole().equals(UserRoles.ADMIN)) {
            response.sendRedirect(request.getContextPath());
        }

        request.setAttribute("user", user);
        String oldUserName = request.getParameter("oldUserName");
        String removeUser = request.getParameter("removeUser");
        String editUserName = request.getParameter("editUserName");
        String editFirstName = request.getParameter("editFirstName");
        String editLastName = request.getParameter("editLastName");
        String editPassword = request.getParameter("editPassword");
        String newPassword = request.getParameter("newPassword");
        String oldPassword = request.getParameter("oldPassword");
        int roleNum = 0;
        int editUser = 0;

        Users newUser = new Users();

        if (removeUser != null) {
            newUser.setUsername(removeUser);
            boolean removeSuccess = userManager.RemoveUser(newUser);
            try {
                response.sendRedirect(request.getContextPath() + "/users");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (editUserName != null) {
            editUser = Integer.parseInt(request.getParameter("UserId"));
            roleNum = Integer.parseInt(request.getParameter("editRole"));
            UserRoles editRole = UserRoles.getRoles(roleNum);
            newUser.setUserId(editUser);
            newUser.setUsername(editUserName);
            newUser.setFirstName(editFirstName);
            newUser.setLastName(editLastName);
            newUser.setRole(editRole);

            if (!editPassword.isEmpty()) {
                if (!editPassword.equals(newPassword) || newPassword.isEmpty()) {
                    request.setAttribute("passwordDiff", "The new password is different. Please check again.");
                    request.getSession().setAttribute("passwordDiff", "The new password is different. Please check again.");
                    try {
                        response.sendRedirect(request.getContextPath() + "/users");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
                newUser.setPassword(editPassword);
            } else {
                newUser.setPassword(oldPassword);
            }

            boolean editSuccess = userManager.EditUser(newUser);

            if (!editSuccess) {
                request.setAttribute("editFailure", "This Username already exists.");
                request.getSession().setAttribute("editFailure", "This Username already exists.");
                response.sendRedirect(request.getContextPath() + "/users");
                try {
                    response.sendRedirect(request.getContextPath() + "/users");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }

            request.setAttribute("editSuccess", "Edit Success");
            request.getSession().setAttribute("editSuccess", "Edit Success");

            if (oldUserName.equals(user.getUsername())) {
                session.setAttribute("user", newUser);
                request.setAttribute("user", newUser);
            }
            try {
                response.sendRedirect(request.getContextPath() + "/users");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
