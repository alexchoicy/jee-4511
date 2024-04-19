package com.cems.Servlet.Reservation;

import com.cems.Utils.AuthUtils;
import com.cems.Utils.CookieUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ReservationCart", value = "/cart")
public class ReservationCartView extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!AuthUtils.isLogged(req)) {
            AuthUtils.redirectToLogin(req, resp);
            return;
        }

        String value = CookieUtils.getCookie(req, "reservationCart");
        ArrayList<com.cems.Model.Request.ReservationCart> cart = gson.fromJson(value, new TypeToken<ArrayList<com.cems.Model.Request.ReservationCart>>() {
        }.getType());

        req.setAttribute("cartItems", cart);
        req.getRequestDispatcher("/WEB-INF/views/ReservationCart.jsp").forward(req, resp);
    }
}
