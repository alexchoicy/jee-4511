<%@ page import="com.cems.Model.Users" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.Console" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>User List</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid black;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>

<h1>User List</h1>

<table>
    <thead>
    <tr>
        <th>User ID</th>
        <th>Username</th>
        <th>Password</th>
        <th>Phone Number</th>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Role</th>
    </tr>
    </thead>
    <tbody>
    <%
        System.out.println("User List Page");
    %>

    <% ArrayList<Users> users = (ArrayList<Users>) request.getAttribute("users");
        for (Users user : users) { %>
    <tr>
        <td><%= user.getUserId() %></td>
        <td><%= user.getUsername() %></td>
        <td><%= user.getPassword() %></td>
        <td><%= user.getPhoneNumber() %></td>
        <td><%= user.getFirstName() %></td>
        <td><%= user.getLastName() %></td>
        <td><%= user.getRole().getValue() %></td>
    </tr>
    <% } %>
    </tbody>
</table>

</body>
</html>
