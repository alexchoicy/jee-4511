<%@ page import="com.cems.Model.Users" %>
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
<body>
<%
    Users user = (Users) session.getAttribute("user");
    if (user != null) {
%>
<h2>User Details</h2>
<p>User ID: <%= user.getUserId() %></p>
<p>Username: <%= user.getUsername() %></p>
<p>Phone Number: <%= user.getPhoneNumber() %></p>
<p>First Name: <%= user.getFirstName() %></p>
<p>Last Name: <%= user.getLastName() %></p>
<p>Role: <%= user.getRole() %></p>
<%
    }
%>
</body>
</html>
