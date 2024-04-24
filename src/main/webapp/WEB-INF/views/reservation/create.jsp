<%@ page import="java.util.ArrayList" %>
<%@ page import="com.cems.Model.Request.ReservationCart" %>
<%@ page import="com.cems.Model.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<!DOCTYPE html>
<html>

<head>
    <title>User List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            const now = new Date();
            now.setMinutes(0);
            now.setMilliseconds(0);
            const todayDate = now.toISOString().split('T')[0];

            const hours = now.getHours().toString().padStart(2, '0');
            const currentTime = hours + "-00";
            document.getElementById('startDate').value = todayDate;
            document.getElementById('endDate').value = todayDate;

            document.getElementById('startTime').value = currentTime;
            document.getElementById('endTime').value = currentTime;
        });

        function removeItemFromCart(url) {
            fetch(url, {
                method: "DELETE",

            }).then(response => {
                if (!response.ok) {
                    return response.text().then(text => {

                        throw new Error(text);
                    });
                }
                return response.text();
            }).then(data => {
                console.log(data);
                alert("Removed");
                location.reload();
            }).catch((error) => {
                console.error('Error:', error);
                alert(error.message);
            });
        }
    </script>
</head>

<body class="bg-light">
<%@include file="../Components/Nav.jsp" %>
<%@ taglib prefix="options" uri="/WEB-INF/tlds/cems_equipment_tags.tld" %>
<%@ taglib prefix="option" uri="/WEB-INF/tlds/cems_equipment_tags.tld" %>
<% User user = (User) request.getSession().getAttribute("user");
    ArrayList<ReservationCart> cart = (ArrayList<ReservationCart>) request.getAttribute("cartItems");
    ArrayList<String> errors = (ArrayList<String>) request.getAttribute("errors");
%>
<% if (errors != null) { %>
<div class="alert alert-danger mx-5 mt-2  ">
    <ul>
        <% for (String message : errors) { %>
        <li>
            <%= message %>
        </li>
        <% } %>
    </ul>
</div>
<%} %>
<div class="container-fluid m-5">
    <div class="row">
        <div class="col-11 rounded-1 bg-white my-3 p-1">
            <form method="post" action="${pageContext.request.contextPath}/reservation/create">

                <div class="row p-3">
                    <div class="col-lg-8">
                        <div class="row">
                            <div class="col">
                                Reservation User Name :
                            </div>
                            <div class="col">
                                <%= user.getFirstName() + " " + user.getLastName()%>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col">Location</div>
                            <div class="col">
                                <div class="input-group mb-3">
                                    <label class="input-group-text"
                                           for="location">Options</label>
                                    <select class="form-select" id="location" name="locationID">
                                        <option:locationOptions hasValue="false"/>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col">
                                Start Time
                            </div>
                            <div class="col">
                                <input required type="date" name="startDate" id="startDate">
                                <input required type="time" name="startTime" id="startTime">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col">End Time</div>
                            <div class="col">
                                <div class="col">
                                    <input required type="date" name="endDate" id="endDate">
                                    <input required type="time" name="endTime" id="endTime">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row p-3">
                    <div class="border"></div>
                </div>
                <div class="row">
                    <div class="col d-flex justify-content-end">
                        <button type="submit" class="btn btn-primary mx-3 mb-2">Submit</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="row">
        <div class="col-11 rounded-1 bg-white my-3 p-1">
            <div class="p-3">
                <table class="table text-center">
                    <thead>
                    <tr>
                        <th scope="col">
                            Item ID
                        </th>
                        <th scope="col">
                            Item Name
                        </th>
                        <th scope="col">
                            Request Quantity
                        </th>
                        <th scope="col">
                            Remove
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if (cart != null) {
                        for (ReservationCart cartItem : cart) {%>
                    <tr>
                        <td>
                            <%= cartItem.getEquipmentID()%>
                        </td>
                        <td>
                            <%= cartItem.getEquipmentName()%>
                        </td>
                        <td>
                            <%= cartItem.getQuantity()%>
                        </td>
                        <td>
                            <button
                                    onclick="removeItemFromCart('${pageContext.request.contextPath}/equipment/<%= cartItem.getEquipmentID()%>/cart')"
                                    class="btn btn-danger">
                                Remove
                            </button>
                        </td>
                    </tr>
                    <% }
                    }%>
                    </tbody>
                </table>

            </div>
        </div>
    </div>

</div>


</body>

</html>