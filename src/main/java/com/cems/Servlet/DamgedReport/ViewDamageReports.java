package com.cems.Servlet.DamgedReport;

import com.cems.Model.Display.DamagesReportDisplay;
import com.cems.database.DamagesReportManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ViewDamageReports", value = "/damages")
public class ViewDamageReports extends HttpServlet {

    private DamagesReportManager damagesReportManager;
    @Override
    public void init() throws ServletException {
        damagesReportManager = new DamagesReportManager();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ArrayList<DamagesReportDisplay> damagesReportDisplays = damagesReportManager.getDamageReports();

        req.setAttribute("damagesReportDisplays", damagesReportDisplays);
        req.getRequestDispatcher("/WEB-INF/views/damagedReport/list.jsp").forward(req, resp);
    }
}
