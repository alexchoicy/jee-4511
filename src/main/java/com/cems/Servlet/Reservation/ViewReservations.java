package com.cems.Servlet.Reservation;

import com.cems.Model.Display.ReservationDisplay;
import com.cems.Model.User;
import com.cems.Utils.AuthUtils;
import com.cems.database.ReservationManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ViewReservations", value = "/reservations")
public class ViewReservations extends HttpServlet {

    private ReservationManager reservationManager;

    @Override
    public void init() {
        reservationManager = new ReservationManager();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        if (!AuthUtils.isLogged(request)) {
            AuthUtils.redirectToLogin(request, response);
            return;
        }
        User user = (User) request.getSession().getAttribute("user");
        ReservationDisplay reservationDisplay = reservationManager.getReservations(user);
    }
}
