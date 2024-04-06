package com.cems.Servlet.Equipment;

import com.cems.Enums.UserRoles;
import com.cems.Model.Equipment;
import com.cems.Model.PagedResult;
import com.cems.Model.Users;
import com.cems.Utils.AuthUtils;
import com.cems.Utils.ParseUtil;
import com.cems.database.EquipmentManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name = "EquipmentServlet", value = "/equipment")
public class EquipmentServlet extends HttpServlet {

    private EquipmentManager equipmentManager;

    @Override
    public void init() throws ServletException {
        equipmentManager = new EquipmentManager();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthUtils.isLogged(request)) {
            AuthUtils.redirectToLogin(request, response);
            return;
        }
        Users user = (Users) request.getSession().getAttribute("user");
        int userId = user.getUserId();

        String searchText = request.getParameter("searchText");
        int locationId = ParseUtil.tryParseInt(request.getParameter("locationId"), 0);
        boolean showStaffOnly = ParseUtil.tryParseBoolean(request.getParameter("showStaffOnly"), false);
        boolean isListed = ParseUtil.tryParseBoolean(request.getParameter("isListed"), true);
        boolean showWishlistOnly = ParseUtil.tryParseBoolean(request.getParameter("showWishlistOnly"), false);
        boolean showAvailableOnly = ParseUtil.tryParseBoolean(request.getParameter("showAvailableOnly"), false);
        int page = ParseUtil.tryParseInt(request.getParameter("page"), 1);
        int pageSize = ParseUtil.tryParseInt(request.getParameter("pageSize"), 15);


        PagedResult<ArrayList<Equipment>> equipmentList = equipmentManager.getEquipmentList(userId, user.getRole(), searchText, locationId, showWishlistOnly, showStaffOnly, isListed, showAvailableOnly, page, pageSize);
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{");
        jsonBuilder.append("\"total\":").append(equipmentList.getTotal()).append(",");
        jsonBuilder.append("\"page\":").append(equipmentList.getPage()).append(",");
        jsonBuilder.append("\"pageSize\":").append(equipmentList.getPageSize()).append(",");
        jsonBuilder.append("\"hasNext\":").append(equipmentList.isHasNext()).append(",");
        jsonBuilder.append("\"hasPrevious\":").append(equipmentList.isHasPrevious()).append(",");
        jsonBuilder.append("\"data\":[");

        ArrayList<Equipment> dataList = equipmentList.getData();
        for (int i = 0; i < dataList.size(); i++) {
            Equipment equipment = dataList.get(i);
            jsonBuilder.append("{");
            jsonBuilder.append("\"equipment_id\":").append(equipment.getId()).append(",");
            jsonBuilder.append("\"equipment_name\":\"").append((equipment.getName())).append("\",");
            jsonBuilder.append("\"description\":\"").append(equipment.getDescription()).append("\",");
            jsonBuilder.append("\"isStaffOnly\":").append(equipment.isStaffOnly()).append(",");
            jsonBuilder.append("\"isListed\":").append(equipment.isListed()).append(",");
            jsonBuilder.append("\"availableQuantity\":").append(equipment.getAvailableQuantity()).append(",");
            jsonBuilder.append("\"isWishlisted\":").append(equipment.isWishListed());
            jsonBuilder.append("}");
            if (i < dataList.size() - 1) {
                jsonBuilder.append(",");
            }
        }


        jsonBuilder.append("]");
        jsonBuilder.append("}");

        // Set content type and write JSON response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonBuilder.toString());
    }
}
