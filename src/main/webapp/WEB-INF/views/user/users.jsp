<%@ page import="com.cems.Model.Users" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.Console" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>User List</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
    </head>
    <body>
        <%@include file="../Components/Nav.jsp"%>
        <br>
        <h1 style="display: inline; margin-left: 20px;">User List</h1>
        <a href="${pageContext.request.contextPath}/createUser" class="btn btn-primary" style="float: right; margin-right: 20px">Create User</a>
        <br>
        <div style="margin-left: 20px; margin-right: 20px;">
            <table class="table table-striped table-hover text-center" >
                <thead>
                    <tr>
                        <th>User ID</th>
                        <th>Username</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Role</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% ArrayList<Users> users = (ArrayList<Users>) request.getAttribute("users");
                        for (Users user : users) {
                            String modalId = "exampleModal" + user.getUserId();
                    %>
                    <tr>
                        <td><%= user.getUserId()%></td>
                        <td><%= user.getUsername()%></td>
                        <td><%= user.getFirstName()%></td>
                        <td><%= user.getLastName()%></td>
                        <td><%= user.getRole().name()%></td>
                        <td>
                            <button type="button" class="btn btn-primary">Edit</button>
                            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#<%= modalId%>">
                                Remove
                            </button>

                            <!-- Modal -->
                            <form>
                                <div class="modal fade" id="<%= modalId%>" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h1 class="modal-title fs-5" id="exampleModalLabel">Remove User</h1>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body">
                                                You are currently deleting the action of user <b style="color: red; display: inline;"><%= user.getUsername()%></b>.<br>Are you sure you want to execute it?<br>Once confirmed, you will not be able to restore it.
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                <button type="button" class="btn btn-primary" style="background-color: red">Confirm</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </td>
                    </tr>
                    <% }%>
                </tbody>
            </table>
        </div>

    </body>
</html>
