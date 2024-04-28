<%-- 
    Document   : createDelivery
    Created on : 2024年4月22日, 上午10:42:06
    Author     : MarkYu
--%>

<%@ page import="com.cems.Model.Display.DeliveryDisplay" %>
<%@ page import="com.cems.Model.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.Console" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
    <head>
        <title>Create User</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
    </head>
    <body class="bg-light">
        <%@include file="../Components/Nav.jsp"%>
        <br>
        <div style="margin-left: 20px; margin-right: 20px;">
            <h4>Create Delivery</h4>
            <br>
            <% ArrayList<User> users = (ArrayList<User>) request.getAttribute("users");%>

            <form class="row g-3" method="post" action="${pageContext.request.contextPath}/createDelivery" >
                <div class="col-12">
                    <label for="Courier" class="form-label">Courier :</label>
                    <select id="Courier" name="Courier" required>
                        <% for (User user : users) {%>
                        <option value="<%= user.getUserId()%>"><%= user.getUsername()%></option>
                        <% } %>
                    </select>
                </div>
                <div>
                    <label for="deadline" class="form-label">Deadline :</label>
                    <input type="date" name="deadline">
                </div>
                <div class="col-md-6">
                    <button type="submit" class="btn btn-primary">Delegate</button>
                </div>
                <div style="margin-left: 20px; margin-right: 20px;">
                    <table class="table table-striped table-hover text-center" >
                        <thead>
                            <tr>
                                <th>Item ID</th>
                                <th>Item Name</th>
                                <th>Serial Number</th>
                                <th>Item current location</th>
                                <th>Destination location</th>
                                <th>Start Time</th>
                                <th>Delegate</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% ArrayList<DeliveryDisplay> deliveryDisplay = (ArrayList<DeliveryDisplay>) request.getAttribute("deliveryDisplay");
                                for (DeliveryDisplay delivery : deliveryDisplay) {

                            %>
                            <tr>
                                <td><%= delivery.getItemId()%></td>
                                <td><%= delivery.getItemName()%></td>
                                <td><%= delivery.getSerialNumber()%></td>
                                <td><%= delivery.getLoctionName()%></td>
                        <input hidden name="from" value="<%= delivery.getLoctionName()%>">
                        <td><%= delivery.getDestionName()%></td>
                        <input hidden name="to" value="<%= delivery.getDestionName()%>">
                        <td><%= delivery.getStartTime()%></td>
                        <td><input type="checkbox" name="selectedOrders" value="<%= delivery.getItemId()%>"></td>
                        </tr>
                        <% }%>
                        </tbody>
                </div>
            </form>
        </div>
    </div>

</body>

</html>


