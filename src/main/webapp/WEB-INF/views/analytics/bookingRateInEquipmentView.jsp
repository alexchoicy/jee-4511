<%@ page import="com.cems.Model.Analytics.BookingRate" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String bookingRate = (String) request.getAttribute("bookingRate");%>
<html>
<head>
    <title>Title</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.2/dist/chart.umd.min.js"></script>
</head>
<body class="bg-light">
<jsp:include page="../Components/Nav.jsp"/>
<canvas id="lineChart" width="800" height="400"></canvas>
</body>


<script>
    document.addEventListener("DOMContentLoaded", function () {
        const bookingRateData = '<%= bookingRate%>';

        const json = JSON.parse(bookingRateData);

        const ctx = document.getElementById('lineChart').getContext('2d');
        console.log(json);
        const lineChart = new Chart(ctx, {
            type: 'line',
            data: json.chartData,
            options: {
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        },
                        max: json.maxBookingRate + 1
                    }
                }
            }
        })
    });
</script>


</html>
