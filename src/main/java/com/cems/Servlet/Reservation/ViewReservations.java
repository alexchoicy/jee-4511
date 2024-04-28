package com.cems.Servlet.Reservation;

import com.cems.Enums.UserRoles;
import com.cems.Model.Display.ReservationDisplay;
import com.cems.Model.User;
import com.cems.Utils.AuthUtils;
import com.cems.database.ReservationManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ViewReservations", value = "/reservations")
public class ViewReservations extends HttpServlet {

    private ReservationManager reservationManager;

    @Override
    public void init() {
        reservationManager = new ReservationManager();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthUtils.isLogged(request)) {
            AuthUtils.redirectToLogin(request, response);
            return;
        }
        User user = (User) request.getSession().getAttribute("user");
        ReservationDisplay reservationDisplay;
        if (user.getRole() == UserRoles.ADMIN || user.getRole() == UserRoles.TECHNICIAN) {
            reservationDisplay = reservationManager.getReservationAdmin();
        } else {
            reservationDisplay = reservationManager.getReservations(user);
        }
        request.setAttribute("reservations", reservationDisplay);
        request.getRequestDispatcher("/WEB-INF/views/reservation/list.jsp").forward(request, response);
    }
}
