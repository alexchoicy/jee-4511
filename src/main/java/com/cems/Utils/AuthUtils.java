package com.cems.Utils;

import com.cems.Enums.UserRoles;
import com.cems.Model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthUtils {
    public static boolean isLogged(HttpServletRequest request) {
        // false to avoid creating a new session
        return request.getSession(false) != null && request.getSession().getAttribute("user") != null;
    }

    public static void redirectToLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void redirectToIndex(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(request.getContextPath() + "/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAdmin(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        return user.getRole().equals(UserRoles.ADMIN);
    }
}
