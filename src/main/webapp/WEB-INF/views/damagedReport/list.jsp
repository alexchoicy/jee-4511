<%@ page import="java.util.ArrayList" %>
<%@ page import="com.cems.Model.Display.DamagesReportDisplay" %>
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

<%
    ArrayList<DamagesReportDisplay> damagesReportDisplays = (ArrayList<DamagesReportDisplay>) request.getAttribute("damagesReportDisplays");

%>

<div class="mx-2">
    <table class="table table-striped table-hover text-center" style="table-layout: fixed; width: 100%">
        <thead>
        <th>Damage Report ID</th>
        <th>Status</th>
        <th>Created At</th>
        </thead>
        <tbody id="body">
        <%
            if (damagesReportDisplays != null || !damagesReportDisplays.isEmpty()) {
            for (DamagesReportDisplay damagesReportDisplay : damagesReportDisplays) {
        %>
        <tr>
            <td><%= damagesReportDisplay.getRecordID()%>
            </td>
            <td><%=damagesReportDisplay.getDamagedStatus().getName()%>
            </td>
            <td><%=damagesReportDisplay.getLogDatetime()%>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/damages/detail?recordID=<%= damagesReportDisplay.getRecordID()%>">Detail</a>
            </td>
        </tr>

        <% }} %>

        </tbody>
    </table>
</div>

</body>
</html>
