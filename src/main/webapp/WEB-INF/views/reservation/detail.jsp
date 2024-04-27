<%@ page import="com.cems.Model.Reservations" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.cems.Model.EquipmentItem" %>
<%@ page import="com.cems.Enums.ReservationStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <% Reservations reservation = (Reservations) request.getAttribute("reservation");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    %>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            document.querySelectorAll('.btn').forEach(button => {
                button.addEventListener('click', function () {
                    const action = this.getAttribute('data-action');

                    fetch('${pageContext.request.contextPath}/reservations/records?recordID=' + <%= reservation.getId()%>, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: 'action=' + action,
                    })
                        .then(response => {
                            if (response.ok) {
                                location.reload();
                            }
                            throw new Error('Request failed.');
                        })
                        .catch(error => {
                            console.log(error);
                        });

                });
            });
        });
    </script>


</head>
<body class="bg-light">
<%@include file="../Components/Nav.jsp" %>

<br>
<% if (userBean.getRole() == UserRoles.TECHNICIAN || userBean.getRole() == UserRoles.ADMIN) { %>
<div class="d-flex justify-content-end m-2">
    <% if (reservation.getStatus() == ReservationStatus.PENDING) {%>
    <button class="btn btn-success m-2" data-action="1">Approve</button>
    <button class="btn btn-danger m-2" data-action="2">Decline</button>
    <% } else if (reservation.getStatus() == ReservationStatus.APPROVED) { %>
    <button class="btn btn-primary m-2" data-action="3">Check in</button>
    <% } else if (reservation.getStatus() == ReservationStatus.ACTIVE) { %>
    <button class="btn btn-primary m-2" data-action="4">Check out</button>
    <% } %>
</div>
<% } %>

<div class="container-fluid m-5">
    <div class="row">
        <div class="col-11 rounded-1 bg-white my-3 p-1">
            <div class="row p-3">
                <div class="col-lg-8">
                    <div class="row">
                        <div class="col">Reservation Name</div>
                        <div class="col">
                            <div><%= reservation.getUser().getFirstName() + " " + reservation.getUser().getLastName()%>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">Status</div>
                        <div class="col">
                            <div><%= reservation.getStatus()%>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">Location</div>
                        <div class="col">
                            <div><%= reservation.getDestination().getName()%>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">Start Time</div>
                        <div class="col">
                            <div><%= sdf.format(reservation.getStartTime())%>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">End Time</div>
                        <div class="col">
                            <div><%= sdf.format(reservation.getEndTime())%>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">Check out Time</div>
                        <div class="col">
                            <div><%= reservation.getCheckin_time() == null ? "Haven't pick up yet" : sdf.format(reservation.getCheckin_time())%>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col">Return Time</div>
                        <div class="col">
                            <div><%= reservation.getCheckout_time() == null ? "Haven't Return yet" : sdf.format(reservation.getCheckout_time())%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-11 rounded-1 bg-white my-3 p-1">
                <div class="p-3">
                    <table class="table text-center">
                        <thead>
                        <tr>
                            <th scope="col">Item ID</th>
                            <th scope="col">Item Name</th>
                            <th scope="col">Item Serial Number</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <% for (EquipmentItem item : reservation.getItems()) {%>
                        <tr>
                            <td>
                                <%= item.getId()%>
                            </td>
                            <td>
                                <%= item.getEquipmentName()%>
                            </td>
                            <td>
                                <%= item.getSerialNumber()%>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/damages/create?itemID=<%= item.getId()%>&recordID=<%= reservation.getId()%>" >Report Damages</a>
                            </td>
                        </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
