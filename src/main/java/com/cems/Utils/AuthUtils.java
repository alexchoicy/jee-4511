package com.cems.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthUtils {
    public static boolean isLogged(HttpServletRequest request) {
        //false to avoid creating a new session
        return request.getSession(false).getAttribute("user") != null;
    }

    public static void redirectToLogin(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect(request.getContextPath() + "/login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
