package com.cems.Servlet.Analytics;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "BookingRateInEquipmentServlet", value = "/analytics/bookingRateInEquipment")
public class BookingRateInEquipmentServet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/analytics/bookingRateInEquipment.jsp").forward(req, resp);
    }
}
