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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author MarkYu
 */
@WebServlet(name = "DeliveryListServlet", value = "/deliveryList")
public class DeliveryListServlet extends HttpServlet {

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
        ArrayList<DeliveryDisplay> deliveryDisplay = deliveryManager.getDeliveryRecods();

        request.setAttribute("deliveryDisplay", deliveryDisplay);
        request.setAttribute("user", user);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/views/user/deliveryList.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!AuthUtils.isLogged(request)) {
            AuthUtils.redirectToLogin(request, response);
            return;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        request.setAttribute("user", user);

        String pickup = request.getParameter("pickup");
        String arrive = request.getParameter("arrive");
        String CheckPickupDateTime = request.getParameter("pickupDateTime");
        String CheckArriveDateTime = request.getParameter("arriveDateTime");
        int deliveryId = Integer.parseInt(request.getParameter("deliveryId"));

        DeliveryDisplay newDelivery = new DeliveryDisplay();

        if (pickup != null) {
            java.time.LocalDateTime pickupDateTime = java.time.LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String pickupDateTimeString = pickupDateTime.format(formatter);

            newDelivery.setDeliveryId(deliveryId);
            newDelivery.setPickupDateTime(pickupDateTimeString);

            boolean updateSuccess = deliveryManager.updatePickupTime(newDelivery);

            request.setAttribute("pickupSuccess", "Pickup Success");
            request.getSession().setAttribute("pickupSuccess", "Pickup Success");
            response.sendRedirect(request.getContextPath() + "/deliveryList");
            return;
        }

        if (arrive != null) {
            java.time.LocalDateTime arriveDateTime = java.time.LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String arriveDateTimeString = arriveDateTime.format(formatter);

            newDelivery.setDeliveryId(deliveryId);
            newDelivery.setArriveDateTime(arriveDateTimeString);

            boolean updateSuccess = deliveryManager.updateArriveTime(newDelivery);

            request.setAttribute("arriveSuccess", "Arrive Success");
            request.getSession().setAttribute("arriveSuccess", "Arrive Success");
            response.sendRedirect(request.getContextPath() + "/deliveryList");
            return;
        }
    }
}
