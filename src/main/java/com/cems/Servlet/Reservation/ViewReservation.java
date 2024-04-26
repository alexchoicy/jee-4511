package com.cems.Servlet.Reservation;

import com.cems.Model.Reservations;
import com.cems.Utils.AuthUtils;
import com.cems.Utils.ParseUtil;
import com.cems.database.ReservationManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ViewReservation", value = "/reservations/records")
public class ViewReservation extends HttpServlet {
    private ReservationManager reservationManager;

    @Override
    public void init() {
        reservationManager = new ReservationManager();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthUtils.isLogged(req)) {
            AuthUtils.redirectToLogin(req, resp);
            return;
        }

        int recordID = ParseUtil.tryParseInt(req.getParameter("recordID"), 0);
        if (recordID == 0 ) {
            resp.sendRedirect(req.getContextPath() + "/reservations");
        }

        try {
            Reservations reservation = reservationManager.getReservation(recordID);
            req.setAttribute("reservation", reservation);
            req.getRequestDispatcher("/WEB-INF/views/reservation/detail.jsp").forward(req,resp);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
