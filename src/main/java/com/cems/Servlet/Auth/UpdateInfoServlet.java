/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cems.Servlet.Auth;

import com.cems.Model.Users;
import com.cems.Utils.AuthUtils;
import com.cems.database.UserManager;
import java.io.IOException;
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
@WebServlet(name = "updateInfoServlet", value = "/information")
public class UpdateInfoServlet extends HttpServlet {

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

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/user/information.jsp");
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

        String username = request.getParameter("inputUsername");
        String firstName = request.getParameter("inputFirstName");
        String lastName = request.getParameter("inputLastName");
        String password = request.getParameter("inputPassword");
        String OldPassword = request.getParameter("inputOldPassword");
        String newPassword = request.getParameter("newPassword");

        if (!username.equals(user.getUsername())
                || !firstName.equals(user.getFirstName())
                || !lastName.equals(user.getLastName())
                || !password.equals("")) {                  //如果有改變
            if (OldPassword.isEmpty()) {                    //如果舊密碼係空設定所有嘢變番原本
                username = user.getUsername();
                firstName = user.getFirstName();
                lastName = user.getLastName();
                password = user.getPassword();
                request.setAttribute("noOldPassword", "You need to enter your old password");
                user.setUsername(username);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPassword(password);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/user/information.jsp");
                try {
                    requestDispatcher.forward(request, response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            } else {                                            //如果舊密碼唔係空
                if (OldPassword.equals(user.getPassword())) {   //檢查舊密碼係咪等於資料庫密碼 ,如果喺就檢查新密碼係唔係空
                    if (password.isEmpty()) {                   //如果新密碼係空,就淨係設定名稱 
                        user.setUsername(username);
                        user.setFirstName(firstName);
                        user.setLastName(lastName);
                    } else {
                        if (newPassword.isEmpty()) {            //如果有輸入新密碼但系冇再次輸入新密碼 
                            username = user.getUsername();
                            firstName = user.getFirstName();
                            lastName = user.getLastName();
                            password = user.getPassword();
                            request.setAttribute("passwordAgain", "Please enter your new password again");
                            user.setUsername(username);
                            user.setFirstName(firstName);
                            user.setLastName(lastName);
                            user.setPassword(password);
                            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/user/information.jsp");
                            try {
                                requestDispatcher.forward(request, response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return;
                        } else {                                //如果有再次輸入新密碼 
                            if (password.equals(newPassword)) { //檢查是否相等
                                user.setUsername(username);
                                user.setFirstName(firstName);
                                user.setLastName(lastName);
                                user.setPassword(password);
                            } else {
                                username = user.getUsername();
                                firstName = user.getFirstName();
                                lastName = user.getLastName();
                                password = user.getPassword();
                                request.setAttribute("passwordDiff", "The new password is different. Please check again.");
                                user.setUsername(username);
                                user.setFirstName(firstName);
                                user.setLastName(lastName);
                                user.setPassword(password);
                                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/user/information.jsp");
                                try {
                                    requestDispatcher.forward(request, response);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return;
                            }
                        }
                    }
                } else {                                        //如果舊密碼唔等於資料庫密碼,設定所有嘢變番原本
                    username = user.getUsername();
                    firstName = user.getFirstName();
                    lastName = user.getLastName();
                    password = user.getPassword();
                    request.setAttribute("passwordWrong", "The old password is wrong. Please check again.");
                    user.setUsername(username);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setPassword(password);
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/user/information.jsp");
                    try {
                        requestDispatcher.forward(request, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }
        } else {                                                //如果冇改變設定所有嘢變番原本
            username = user.getUsername();
            firstName = user.getFirstName();
            lastName = user.getLastName();
            password = user.getPassword();
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(password);
        }

        boolean updateSuccess = userManager.updateUserInfo(user);

        if (updateSuccess) {
            response.sendRedirect(request.getContextPath() + "/information");
        } else {
            username = user.getUsername();
            firstName = user.getFirstName();
            lastName = user.getLastName();
            password = user.getPassword();
            request.setAttribute("updateFailure", "This Username already exists.");
            user.setUsername(username);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(password);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/user/information.jsp");
            try {
                requestDispatcher.forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
    }
}
