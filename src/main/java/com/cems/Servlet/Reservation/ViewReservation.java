package com.cems.Servlet.Reservation;

import com.cems.Enums.page.ReservationAction;
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
        if (recordID == 0) {
            resp.sendRedirect(req.getContextPath() + "/reservations");
        }

        try {
            Reservations reservation = reservationManager.getReservation(recordID);
            req.setAttribute("reservation", reservation);
            req.getRequestDispatcher("/WEB-INF/views/reservation/detail.jsp").forward(req, resp);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthUtils.isLogged(req)) {
            AuthUtils.redirectToLogin(req, resp);
            return;
        }

        int recordID = ParseUtil.tryParseInt(req.getParameter("recordID"), 0);
        if (recordID == 0) {
            resp.sendRedirect(req.getContextPath() + "/reservations");
        }

        ReservationAction action = ReservationAction.getAction(ParseUtil.tryParseInt(req.getParameter("action"), 0));
        boolean success = false;
        switch (action) {
            case APPROVE:
                success = reservationManager.approveReservation(recordID);
                break;
            case DECLINE:
                success = reservationManager.declineReservation(recordID);
                break;
            case CHECK_IN:
                success = reservationManager.checkInReservation(recordID);
                break;
            case CHECK_OUT:
                success = reservationManager.checkOutReservation(recordID);
                break;
        }

        if (!success) {
            resp.getWriter().println("Failed to perform action");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
