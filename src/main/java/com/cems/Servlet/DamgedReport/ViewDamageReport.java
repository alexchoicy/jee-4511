package com.cems.Servlet.DamgedReport;

import com.cems.Enums.DamagedStatus;
import com.cems.Model.Display.DamagesReportDisplay;
import com.cems.Utils.ParseUtil;
import com.cems.database.DamagesReportManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ViewDamageReport", value = "/damages/detail")
public class ViewDamageReport extends HttpServlet {

    private DamagesReportManager damagesReportManager;

    @Override
    public void init() throws ServletException {
        damagesReportManager = new DamagesReportManager();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int recordID = ParseUtil.tryParseInt(req.getParameter("recordID"), 0);
        DamagesReportDisplay damagesReportDisplay = damagesReportManager.getDamageReport(recordID);

        req.setAttribute("damageData", damagesReportDisplay);
        req.getRequestDispatcher("/WEB-INF/views/damagedReport/detail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int recordID = ParseUtil.tryParseInt(req.getParameter("recordID"), 0);
        DamagedStatus status = DamagedStatus.valueOf(ParseUtil.tryParseInt(req.getParameter("action"), 0));
        if (recordID == 0 || status == null) {
            resp.getWriter().println("Invalid request");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean success = false;
        switch (status) {
            case CONFIRMED:
                success = damagesReportManager.confirmDamageReport(recordID);
                break;
            case DISCARDED:
                success = damagesReportManager.discardDamageReport(recordID);
                break;
            case REPAIRED:
                success = damagesReportManager.repairedDamageReport(recordID);
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
