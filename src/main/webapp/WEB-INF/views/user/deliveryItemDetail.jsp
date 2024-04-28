<%-- 
    Document   : deliveryItemDetail
    Created on : 2024年4月27日, 下午3:44:48
    Author     : MarkYu
--%>

<%@ page import="com.cems.Model.Display.DeliveryDisplay" %>
<%@ page import="com.cems.Model.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.Console" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
    <head>
        <title>Item Detail</title>
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
            <h4>Delivery Item</h4>
            <br>
            <div style="margin-left: 20px; margin-right: 20px;">
                <table class="table table-striped table-hover text-center" >
                    <thead>
                        <tr>
                            <th>Item ID</th>
                            <th>Item Name</th>
                            <th>Serial Number</th>
                            <th>Item current location</th>
                            <th>Destination location</th>
                            <th>Quantity</th>
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
                            <td><%= delivery.getDestionName()%></td>
                            <td>1</td>
                        </tr>
                        <% }%>
                    </tbody>
            </div>
        </div>
    </body>
</html>
