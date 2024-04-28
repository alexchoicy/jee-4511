package com.cems.Servlet.Reservation;

import com.cems.Model.Location;
import com.cems.Model.Request.ReservationCart;
import com.cems.Model.User;
import com.cems.Utils.AuthUtils;
import com.cems.Utils.CookieUtils;
import com.cems.Utils.ParseUtil;
import com.cems.database.LocationManager;
import com.cems.database.ReservationManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

@WebServlet(name = "CreateReservation", value = "/reservation/create")
public class CreateReservation extends HttpServlet {
    private LocationManager locationManager;
    private ReservationManager reservationManager;

    @Override
    public void init() {
        locationManager = new LocationManager();
        reservationManager = new ReservationManager();
    }

    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthUtils.isLogged(req)) {
            AuthUtils.redirectToLogin(req, resp);
            return;
        }
        String value = CookieUtils.getCookie(req, "reservationCart");
        ArrayList<ReservationCart> cart = gson.fromJson(value, new TypeToken<ArrayList<ReservationCart>>() {
        }.getType());
        if(cart == null || cart.isEmpty()) {
            ArrayList<String> errors = new ArrayList<>();
            errors.add("Please Add Item the your Reservation Cart");
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/views/reservation/create.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("cartItems", cart);
        req.getRequestDispatcher("/WEB-INF/views/reservation/create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String value = CookieUtils.getCookie(req, "reservationCart");
        ArrayList<ReservationCart> cart = gson.fromJson(value, new TypeToken<ArrayList<ReservationCart>>() {
        }.getType());

        if(cart.isEmpty() || cart == null) {
            ArrayList<String> errors = new ArrayList<>();
            errors.add("Please Add Item the your Reservation Cart");
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/views/reservation/create.jsp").forward(req, resp);
            return;
        }

        String startDate = req.getParameter("startDate");
        String startTime = req.getParameter("startTime");
        String endDate = req.getParameter("endDate");
        String endTime = req.getParameter("endTime");
        Timestamp startDateTime = ParseUtil.tryParseDateTime(startDate, startTime);
        Timestamp endDateTime = ParseUtil.tryParseDateTime(endDate, endTime);

        req.setAttribute("cartItems",cart);
        if (startDateTime == null || endDateTime == null) {
            //error
            ArrayList<String> errors = new ArrayList<>();
            errors.add("Invalid DateTime");
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/views/reservation/create.jsp").forward(req, resp);
            return;
        }

        System.out.printf("Start : %s", startDateTime);
        System.out.printf("End : %s", endDateTime);

        Timestamp today = new Timestamp(System.currentTimeMillis());

        System.out.printf("Today : %s", today);

        if(startDateTime.before(today) || endDateTime.before(today)) {
            ArrayList<String> errors = new ArrayList<>();
            errors.add("Invalid StartDateTime/EndDateTime before today");
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/views/reservation/create.jsp").forward(req, resp);
            return;
        }
        long timeDifference = endDateTime.getTime() - startDateTime.getTime();
        long diffInMinutes = timeDifference / (60 * 1000);
        if (diffInMinutes < 60) {
            ArrayList<String> errors = new ArrayList<>();
            errors.add("The reservation duration must be at least 1 hour.");
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/views/reservation/create.jsp").forward(req, resp);
            return;
        }

        long diffInDays = timeDifference / (24 * 60 * 60 * 1000);
        if (diffInDays > 3) {
            ArrayList<String> errors = new ArrayList<>();
            errors.add("The reservation duration cannot exceed 3 days.");
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/views/reservation/create.jsp").forward(req, resp);
            return;
        }

        int locationID = ParseUtil.tryParseInt(req.getParameter("locationID"), 0);

        if (locationID == 0) {
            ArrayList<String> errors = new ArrayList<>();
            errors.add("Invalid Location");
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/views/reservation/create.jsp").forward(req, resp);
            return;
        }

        Location location = locationManager.getLocation(locationID);

        if (location == null) {
            //incorrect location ID
            ArrayList<String> errors = new ArrayList<>();
            errors.add("Invalid Location");
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/WEB-INF/views/reservation/create.jsp").forward(req, resp);
            return;
        }

        ArrayList<String> errorMessage = reservationManager.createReservation(user, cart, locationID, startDateTime, endDateTime);
        if (errorMessage.isEmpty()) {
            //success
            Cookie cartCookie = new Cookie("reservationCart", "");
            cartCookie.setMaxAge(0);
            cartCookie.setPath(req.getContextPath());
            resp.addCookie(cartCookie);
            resp.sendRedirect(req.getContextPath() + "/reservations");
            return;
        }
        req.setAttribute("errors", errorMessage);

        req.getRequestDispatcher("/WEB-INF/views/reservation/create.jsp").forward(req, resp);

    }
}
