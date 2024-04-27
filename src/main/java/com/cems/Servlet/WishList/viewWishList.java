package com.cems.Servlet.WishList;

import com.cems.Model.Equipment;
import com.cems.Model.User;
import com.cems.database.WishListManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "viewWishList", value = "/wishList")
public class viewWishList extends HttpServlet {
    private WishListManager wishListManager;

    @Override
    public void init() throws ServletException {
        wishListManager = new WishListManager();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        try {
            ArrayList<Equipment> equipmentList = wishListManager.getWishListed(user.getUserId());
            req.setAttribute("wishListed", equipmentList);
            req.getRequestDispatcher("/WEB-INF/views/WishedList.jsp").forward(req, resp);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
