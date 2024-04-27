package com.cems.Servlet.DamgedReport;


import com.cems.Model.Display.DamagesReportDisplay;
import com.cems.Model.User;
import com.cems.Utils.ParseUtil;
import com.cems.database.DamagesReportManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreateDamagesReport", value = "/damages/create")
public class CreateDamagesReport extends HttpServlet {
    private DamagesReportManager damagesReportManager;

    @Override
    public void init() {
        damagesReportManager = new DamagesReportManager();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int itemID = ParseUtil.tryParseInt(req.getParameter("itemID"), 0);
        if (itemID == 0) {
            resp.sendRedirect(req.getContextPath() + "/damages");
        }
        int recordID = ParseUtil.tryParseInt(req.getParameter("recordID"), 0);

        if (recordID == 0) {
            //TODO: allow to create a report but not related to a booking
        }

        DamagesReportDisplay detail = damagesReportManager.getCreateDamagesDetail(itemID, recordID);

        req.setAttribute("damageData", detail);
        req.getRequestDispatcher("/WEB-INF/views/damagedReport/create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int itemID = ParseUtil.tryParseInt(req.getParameter("itemID"), 0);
        if (itemID == 0) {
            resp.sendRedirect(req.getContextPath() + "/damages");
        }
        int recordID = ParseUtil.tryParseInt(req.getParameter("recordID"), 0);

        if (recordID == 0) {
            //TODO: allow to create a report but not related to a booking
        }
        System.out.println("itemID " + itemID + " record " + recordID);
        User user = (User) req.getSession().getAttribute("user");
        String damageDetail = req.getParameter("damageDetail");

        boolean success = damagesReportManager.createDamageDetail(itemID, user, recordID, damageDetail);
        resp.sendRedirect(req.getContextPath() + "/damages");
    }
}
