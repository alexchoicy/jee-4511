/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.cems.Servlet.User;

import com.cems.Enums.UserRoles;
import com.cems.Model.Display.DeliveryDisplay;
import com.cems.Model.User;
import com.cems.Utils.AuthUtils;
import com.cems.database.DeliveryManager;
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
@WebServlet(name = "deliveryItemDetailServlet", value = "/deliveryItemDetail")
public class deliveryItemDetailServlet extends HttpServlet {
    private DeliveryManager deliveryManager;

    @Override
    public void init() {
        deliveryManager = new DeliveryManager();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!AuthUtils.isLogged(request)) {
            AuthUtils.redirectToLogin(request, response);
            return;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        System.out.println("DeliveryListServlet doGet");
        ArrayList<DeliveryDisplay> deliveryDisplay = deliveryManager.getDelivery();

        request.setAttribute("deliveryDisplay", deliveryDisplay);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/user/deliveryItemDetail.jsp");
        requestDispatcher.forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!AuthUtils.isLogged(request)) {
            AuthUtils.redirectToLogin(request, response);
            return;
        }
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        request.setAttribute("user", user);
    }
}
