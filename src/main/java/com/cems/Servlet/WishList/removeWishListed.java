package com.cems.Servlet.WishList;

import com.cems.Model.User;
import com.cems.Utils.ParseUtil;
import com.cems.database.WishListManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "removeWishListed", value = "/wishList/remove")
public class removeWishListed extends HttpServlet {
    private WishListManager wishListManager;

    @Override
    public void init() throws ServletException {
        wishListManager = new WishListManager();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        int equipment_id = ParseUtil.tryParseInt(req.getParameter("id"), 0);
        if (equipment_id == 0) {
            resp.sendRedirect(req.getContextPath() + "/wishList");
            return;
        }
        wishListManager.removeWishListed(user.getUserId(), equipment_id);
        resp.sendRedirect(req.getContextPath() + "/wishList");
    }
}
