<%@ page import="java.util.ArrayList" %>
<%@ page import="com.cems.Model.Equipment" %>
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
<jsp:include page="Components/Nav.jsp"/>
<% ArrayList<Equipment> wishListed = (ArrayList<Equipment>) request.getAttribute("wishListed"); %>


<div class="mx-2">
    <table class="table table-striped table-hover text-center" style="table-layout: fixed; width: 100%">
        <thead>
        <th scope="col">Staff Only</th>
        <th scope="col">ID</th>
        <th scope="col">Item Name</th>
        <th scope="col">Available Quantity</th>
        </thead>
        <tbody id="body">
        <% if (wishListed != null && !wishListed.isEmpty()) {
            for (Equipment equipment : wishListed) { %>
        <tr>
            <td><input type="checkbox" class="form-check-input" id="staffOnly" name="staffOnly"
                       value="<%= equipment.getId() %>"></td>
            <td><%= equipment.getId() %>
            </td>
            <td><%= equipment.getName() %>
            </td>
            <td><%= equipment.getAvailableQuantity() %>
            </td>
            <td>
                <a href="${pageContext.request.contextPath}/wishList/remove?id=<%= equipment.getId() %>"
                   class="btn btn-danger">Remove</a>
            </td>
        </tr>
        <% }
        } %>

        </tbody>
    </table>
</div>
</body>
</html>
