<%-- 
    Document   : deliveryList
    Created on : 2024年4月22日, 上午10:38:31
    Author     : MarkYu
--%>

<%@ page import="com.cems.Model.Display.DeliveryDisplay" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.Console" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Delivery List</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
    </head>
    <body class="bg-light">
        <%@include file="../Components/Nav.jsp"%>
        <br>
        <% if (userBean.getRole() == UserRoles.ADMIN) { %>
        <a href="${pageContext.request.contextPath}/createDelivery" class="btn btn-primary" style="float: right; margin-right: 20px; margin-bottom: 20px;">Create Delivery</a>
        <% }%>

        <% if (session.getAttribute("pickupSuccess") != null) {%>
        <div style="display:inline; width: 500px; margin-left: 20px;" class="alert alert-success" role="alert">
            <%= session.getAttribute("pickupSuccess")%>
        </div>
        <% session.removeAttribute("pickupSuccess"); %>
        <% }%>

        <% if (session.getAttribute("arriveSuccess") != null) {%>
        <div style="display:inline; width: 500px; margin-left: 20px;" class="alert alert-success" role="alert">
            <%= session.getAttribute("arriveSuccess")%>
        </div>
        <% session.removeAttribute("arriveSuccess"); %>
        <% }%>

        <div style="margin-top: 30px;margin-left: 20px; margin-right: 20px;">
            <table class="table table-striped table-hover text-center">
                <thead>
                    <tr>
                        <th>Delivery ID</th>
                        <th>Delivery By</th>
                        <th>Delivery Deadline</th>
                        <th>Pickup Time</th>
                        <th>Arrive Time</th>
                            <% if (userBean.getRole() != UserRoles.ADMIN) { %>
                        <th>Action</th>
                            <% } %>
                        <th>Detail</th>
                    </tr>
                </thead>
                <tbody>
                    <% ArrayList<DeliveryDisplay> deliveryDisplay = (ArrayList<DeliveryDisplay>) request.getAttribute("deliveryDisplay");
                        for (DeliveryDisplay delivery : deliveryDisplay) {
                            if (userBean.getRole() == UserRoles.ADMIN || delivery.getDeliveryBy() == userBean.getUserId()) {
                    %>
                    <tr>
                <form class="row g-3" method="post" action="${pageContext.request.contextPath}/deliveryList">
                    <td><%= delivery.getDeliveryId()%></td>
                    <input hidden type="number" name="deliveryId" value="<%= delivery.getDeliveryId()%>">
                    <td><%= delivery.getDeliveryBy()%></td>
                    <td><%= delivery.getDeadline()%></td>

                    <% if (delivery.getPickupDateTime() == null) { %>
                    <td>Still Not Picked Up</td>
                    <% } else {%>
                    <td><%= delivery.getPickupDateTime()%></td>
                    <% }%>
                    <input hidden type="text" name="pickupDateTime" value="<%= delivery.getPickupDateTime()%>">

                    <% if (delivery.getArriveDateTime() == null) { %>
                    <td>Not delivered</td>
                    <% } else {%>
                    <td><%= delivery.getArriveDateTime()%></td>
                    <% }%>
                    <input hidden type="text" name="arriveDateTime" value="<%= delivery.getArriveDateTime()%>">

                    <% if (userBean.getRole() != UserRoles.ADMIN) { %>
                    <td>
                        <button class="btn btn-primary" type="submit" name="pickup" value="pickup" onclick="return confirmPickup()">Pickup</button>
                        <button class="btn btn-primary" type="submit" name="arrive" value="arrive" onclick="return confirmArrive()">Arrive</button>
                    </td>
                    <% } %>

                    <td><a href="${pageContext.request.contextPath}/deliveryItemDetail">Detail</a></td>
                </form>
                </tr>
                <% }
                    }%>
                </tbody>
            </table>
        </div>

        <script>
            function confirmPickup() {
                return confirm("Confirm to Pickup?");
            }

            function confirmArrive() {
                return confirm("Confirm to Arrive?");
            }
        </script>
    </body>
</html>


