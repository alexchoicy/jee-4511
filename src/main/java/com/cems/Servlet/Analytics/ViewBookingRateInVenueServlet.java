package com.cems.Servlet.Analytics;

import com.cems.Model.Analytics.BookingRate;
import com.cems.Utils.AnalyticsParse;
import com.cems.database.AnalyticsManager;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "ViewBookingRateInVenueServlet", value = "/analytics/viewVenue")
public class ViewBookingRateInVenueServlet extends HttpServlet {
    private AnalyticsManager analyticsManager;
    private Gson gson = new Gson();

    @Override
    public void init() throws ServletException {
        analyticsManager = new AnalyticsManager();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int location_id = Integer.parseInt(req.getParameter("id"));
        String start_date = req.getParameter("start_date");
        String end_date = req.getParameter("end_date");

        try {
            Date start_date_timestamp = sdf.parse(start_date);
            Date end_time_timestamp = sdf.parse(end_date);

            Timestamp startOfDay = AnalyticsParse.getStartOfDayTimestamp(start_date_timestamp);
            Timestamp endOfDay = AnalyticsParse.getEndOfDayTimestamp(end_time_timestamp);

            BookingRate bookingRate = analyticsManager.getBookingRateInVenue(location_id, startOfDay, endOfDay);
            req.setAttribute("bookingRate", gson.toJson(bookingRate));
        } catch (ParseException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        req.getRequestDispatcher("/WEB-INF/views/analytics/bookingRateInVenueView.jsp").forward(req, resp);
    }
}
