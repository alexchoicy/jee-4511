package com.cems.Servlet.Equipment;

import com.cems.Exceptions.EquipmentNotFoundException;
import com.cems.Exceptions.HasItemsException;
import com.cems.Exceptions.ItemNotFoundException;
import com.cems.Exceptions.ItemsInUseException;
import com.cems.Model.Display.EquipmentDisplay;
import com.cems.Model.EquipmentItem;
import com.cems.Model.Location;
import com.cems.Model.Request.CreateEquipmentItem;
import com.cems.Utils.AuthUtils;
import com.cems.Utils.ParseUtil;
import com.cems.database.EquipmentManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "equipmentDetailServlet", value = "/equipment/*")
public class EquipmentDetailServlet extends HttpServlet {
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
            GetEquipmentDetail(request, response, equipmentId);
            return;
        } else if (pathParts.length == 2) {
            System.out.println("search: " + search);
            GetEquipmentDetailWithSearch(request, response, equipmentId, search);
            return;
        }
        String action = pathParts[2];

        switch (action) {
            case "edit":
                if (!AuthUtils.isAdmin(request)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                EditEquipment(request, response, equipmentId);
                break;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthUtils.isAdmin(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String pathInfo = request.getPathInfo(); // /{id}/items/{id}
        String[] pathParts = pathInfo.split("/");
        for (int i = 0; i < pathParts.length; i++) {
            System.out.println(i + ": " + pathParts[i]);
        }
        String value = pathParts[1];
        int equipmentId;
        try {
            equipmentId = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        if (pathParts.length == 2) {
            DeleteEquipment(request, response, equipmentId);
            return;
        }
        String items = pathParts[3];
        int itemId;
        try {
            itemId = Integer.parseInt(items);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        deleteEquipmentItem(request, response, equipmentId, itemId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthUtils.isAdmin(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        ;
        Map<String, String[]> paramMap = request.getParameterMap();

        for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + Arrays.toString(entry.getValue()));
        }

        System.out.println("Request Payload: " + request.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
        String pathInfo = request.getPathInfo(); // /{id}/items/{id}
        String[] pathParts = pathInfo.split("/");
        for (int i = 0; i < pathParts.length; i++) {
            System.out.println(i + ": " + pathParts[i]);
        }
        String value = pathParts[1];
        int equipmentId;
        try {
            equipmentId = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // {id}
        if (pathParts.length == 2) {
            System.out.println("numberOfNewItems: " + request.getParameter("numberOfNewItems"));
            int numberOfNewItems = ParseUtil.tryParseInt(request.getParameter("numberOfNewItems"), 0);
            System.out.println("numberOfNewItems: " + numberOfNewItems);
            boolean addNewItem = true;
            if (numberOfNewItems > 0) {
                ArrayList<CreateEquipmentItem> errorItems = CreateNewItems(request, response, equipmentId, numberOfNewItems);
                if (!errorItems.isEmpty()) {
                    addNewItem = false;
                    request.setAttribute("errorItems", errorItems);
                }
            }

            boolean editStatus = EditEquipmentDetail(request, response, equipmentId);
            if (!editStatus) {
                request.setAttribute("error", "Error editing equipment");

            }
            if (addNewItem && editStatus) {
                response.sendRedirect(request.getContextPath() + "/equipment/" + equipmentId);
            } else {
                EquipmentDisplay equipmentDisplay = equipmentManager.getEquipmentDetail(equipmentId);
                request.setAttribute("equipmentDisplay", equipmentDisplay);
                request.getRequestDispatcher("/WEB-INF/views/equipment/editDetail.jsp").forward(request, response);
            }
            return;
        }

        String items = pathParts[3];
        int itemId;
        try {
            itemId = Integer.parseInt(items);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
    }

    private ArrayList<CreateEquipmentItem> CreateNewItems(HttpServletRequest request, HttpServletResponse response, int equipmentId, int numberOfNewItems) {
        ArrayList<CreateEquipmentItem> newItems = new ArrayList<>();

        for (int i = 1; i <= numberOfNewItems; i++) {
            System.out.println("i: " + i);
            String serialNumber = request.getParameter("serialNumber[" + i + "]");
            int locationId = ParseUtil.tryParseInt(request.getParameter("locationId[" + i + "]"), 0);
            int status = ParseUtil.tryParseInt(request.getParameter("status[" + i + "]"), 0);
            System.out.println("serialNumber: " + serialNumber + " locationId: " + locationId + " status: " + status);
            CreateEquipmentItem item = new CreateEquipmentItem(serialNumber, status, locationId);
            newItems.add(item);
        }

        System.out.println("Size :" + newItems.size());


        return equipmentManager.createEquipmentItems(equipmentId, newItems);
    }

    private boolean EditEquipmentDetail(HttpServletRequest request, HttpServletResponse response, int equipmentId) throws IOException {
        String name = request.getParameter("itemName");
        String description = request.getParameter("itemDescription");

        if (name == null || name.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }

        description = description == null ? "" : description;

        String isStaffOnlyString = request.getParameter("isStaffOnly");
        boolean isStaffOnly = isStaffOnlyString != null && isStaffOnlyString.equals("on");

        String isListedString = request.getParameter("isListed");
        boolean isListed = isListedString != null && isListedString.equals("on");


        System.out.println("name: " + name + " description: " + description + " isStaffOnly: " + isStaffOnly + " isListed: " + isListed);
        return equipmentManager.editEquipment(equipmentId, name, description, isStaffOnly, isListed);
    }


    private void deleteEquipmentItem(HttpServletRequest request, HttpServletResponse response, int equipmentId, int itemId) throws IOException {
        try {
            boolean status = equipmentManager.deleteEquipmentItem(equipmentId, itemId);
            response.setContentType("text/plain");
            response.getWriter().write(status ? "true" : "false");
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        } catch (ItemNotFoundException e) {
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(e.getMessage());
        } catch (ItemsInUseException e) {
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write(e.getMessage());
        }
    }

    private void DeleteEquipment(HttpServletRequest request, HttpServletResponse response, int equipmentId) throws IOException {
        try {
            boolean status = equipmentManager.deleteEquipment(equipmentId);
            response.setContentType("text/plain");
            response.getWriter().write(status ? "true" : "false");

        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        } catch (HasItemsException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.setContentType("text/plain");
            response.getWriter().write(e.getMessage());
        } catch (EquipmentNotFoundException e) {
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(e.getMessage());
        }
    }

    private void EditEquipment(HttpServletRequest request, HttpServletResponse response, int equipmentId) {
        EquipmentDisplay equipmentDisplay = equipmentManager.getEquipmentDetail(equipmentId);

        if (equipmentDisplay == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("equipmentDisplay", equipmentDisplay);

        try {
            request.getRequestDispatcher("/WEB-INF/views/equipment/editDetail.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void GetEquipmentDetail(HttpServletRequest request, HttpServletResponse response, int equipmentId) throws ServletException, IOException {
        EquipmentDisplay equipmentDisplay = equipmentManager.getEquipmentDetail(equipmentId);

        if (equipmentDisplay == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        System.out.println("equipmentDisplay: " + equipmentDisplay);
        request.setAttribute("equipmentDisplay", equipmentDisplay);

        request.getRequestDispatcher("/WEB-INF/views/equipment/detail.jsp").forward(request, response);
    }

    private void GetEquipmentDetailWithSearch(HttpServletRequest request, HttpServletResponse response, int equipmentId, String search) throws ServletException, IOException {
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
