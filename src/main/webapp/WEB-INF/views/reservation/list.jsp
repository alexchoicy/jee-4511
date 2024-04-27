<%@ page import="com.cems.Model.Display.ReservationDisplay" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.cems.Model.Reservations" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
</head>
<body class="bg-light">
<%@include file="../Components/Nav.jsp" %>
<%@ taglib prefix="res" uri="/WEB-INF/tlds/cems_reservations_tags.tld" %>
<%
    ReservationDisplay reservationDisplay = (ReservationDisplay) request.getAttribute("reservations");
    // If the reservationDisplay is null, create a new one and set the lists to empty
    if (reservationDisplay == null) {
        reservationDisplay = new ReservationDisplay();
        reservationDisplay.setActive(new ArrayList<Reservations>());
        reservationDisplay.setApproved(new ArrayList<Reservations>());
        reservationDisplay.setWaitingToApprove(new ArrayList<Reservations>());
        reservationDisplay.setCompleted(new ArrayList<Reservations>());
    }
%>
<br>
<div class="container-fluid">
    <div class="mt-5">
        <div class="card mt-2">
            <div class="card-header bg-primary text-white">
                Waiting to Approve
            </div>
            <div class="card-body">
                <table
                        class="table table-striped table-hover text-center"
                >
                    <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Status</th>
                        <th scope="col">Start Date</th>
                        <th scope="col">End Date</th>
                        <th scope="col">Created Date</th>
                    </tr>
                    </thead>
                    <tbody id="waitingToApproveBody">
                    <res:reservationsList reservations="<%= reservationDisplay.getWaitingToApprove()%>"/>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="card mt-2">
            <div class="card-header bg-success text-white">Approved</div>
            <div class="card-body">
                <table
                        class="table table-striped table-hover text-center"
                >
                    <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Status</th>
                        <th scope="col">Start Date</th>
                        <th scope="col">End Date</th>
                        <th scope="col">Created Date</th>
                    </tr>
                    </thead>
                    <tbody id="approvedBody">
                    <res:reservationsList reservations="<%= reservationDisplay.getApproved()%>"/>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="card mt-2">
            <div class="card-header bg-success text-white">Active</div>
            <div class="card-body">
                <table
                        class="table table-striped table-hover text-center"
                >
                    <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Status</th>
                        <th scope="col">Start Date</th>
                        <th scope="col">End Date</th>
                        <th scope="col">Created Date</th>
                    </tr>
                    </thead>
                    <tbody id="activeBody">
                    <res:reservationsList reservations="<%= reservationDisplay.getActive()%>"/>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="card mt-2">
            <div class="card-header bg-secondary text-white">
                History
            </div>
            <div class="card-body">
                <table
                        class="table table-striped table-hover text-center"
                >
                    <thead>
                    <tr>
                        <th scope="col">ID</th>
                        <th scope="col">Status</th>
                        <th scope="col">Start Date</th>
                        <th scope="col">End Date</th>
                        <th scope="col">Created Date</th>
                    </tr>
                    </thead>
                    <tbody id="historyBody">
                    <res:reservationsList reservations="<%= reservationDisplay.getCompleted()%>"/>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
