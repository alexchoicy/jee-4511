package com.cems.Servlet.Equipment;

import com.cems.Model.EquipmentDisplay;
import com.cems.Model.EquipmentItem;
import com.cems.Model.Location;
import com.cems.Utils.AuthUtils;
import com.cems.database.EquipmentManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "equipmentDetailServlet", value = "/equipment/*")
public class EquipmentDetailServlet extends HttpServlet{
    private EquipmentManager equipmentManager;
    public void init() {
        equipmentManager = new EquipmentManager();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthUtils.isLogged(request)) {
            AuthUtils.redirectToLogin(request, response);
            return;
        }

        String search = request.getParameter("search");
        System.out.println("search: " + search);
        String pathInfo = request.getPathInfo(); // /{id}
        String[] pathParts = pathInfo.split("/");
        for (String part : pathParts) {
            System.out.println(part);
        }
        String value = pathParts[1];
        int equipmentId;
        try {
            equipmentId = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        System.out.println(pathParts.length);
        if (pathParts.length == 2 && search == null) {
            getEquipmentDetail(request, response, equipmentId);
            return;
        } else if (pathParts.length == 2) {
            System.out.println("search: " + search);
            getEquipmentDetailWithSearch(request, response, equipmentId, search);
            return;
        }
        String action = pathParts[2];
    }

    private void getEquipmentDetail(HttpServletRequest request, HttpServletResponse response, int equipmentId) throws ServletException, IOException {
        EquipmentDisplay equipmentDisplay = equipmentManager.getEquipmentDetail(equipmentId);

        if (equipmentDisplay == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        System.out.println("equipmentDisplay: " + equipmentDisplay);
        request.setAttribute("equipmentDisplay", equipmentDisplay);

        request.getRequestDispatcher("/WEB-INF/views/equipment/detail.jsp").forward(request, response);
    }

    private void getEquipmentDetailWithSearch(HttpServletRequest request, HttpServletResponse response, int equipmentId, String search) throws ServletException, IOException {
        ArrayList<EquipmentItem> items = equipmentManager.getEquipmentItemWithSearch(equipmentId, search);

        StringBuilder jsonBuilder = new StringBuilder();

        jsonBuilder.append("[");

        for (int i = 0; i < items.size(); i++) {
            EquipmentItem item = items.get(i);
            System.out.println("item: " + item.getId());
            jsonBuilder.append("{");

            jsonBuilder.append("\"id\":").append(item.getId()).append(",");
            jsonBuilder.append("\"serialNumber\":\"").append(item.getSerialNumber()).append("\",");
            jsonBuilder.append("\"borrowedTimes\":").append(item.getBorrowedTimes()).append(",");
            jsonBuilder.append("\"status\":\"").append(item.getStatus().name()).append("\",");
            jsonBuilder.append("\"equipmentId\":").append(item.getEquipmentId()).append(",");

            Location location = item.getLocation();
            if (location != null) {
                jsonBuilder.append("\"location\":{");
                jsonBuilder.append("\"id\":").append(location.getId()).append(",");
                jsonBuilder.append("\"name\":\"").append(location.getName()).append("\",");
                jsonBuilder.append("\"address\":\"").append(location.getAddress()).append("\"");
                jsonBuilder.append("}");
            }

            jsonBuilder.append("}");

            if (i < items.size() - 1) {
                jsonBuilder.append(", ");
            }
        }

        jsonBuilder.append("]");

        response.setContentType("application/json");
        response.getWriter().write(jsonBuilder.toString());
    }

}
