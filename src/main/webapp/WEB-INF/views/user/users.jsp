<%@ page import="com.cems.Model.User" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.Console" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
    <body class="bg-light">
        <%@include file="../Components/Nav.jsp"%>
        <br>
        <a href="${pageContext.request.contextPath}/createUser" class="btn btn-primary" style="float: right; margin-right: 20px">Create User</a>

        <% if (session.getAttribute("passwordDiff") != null) {%>
        <div style="display:inline; width: 500px; margin-left: 20px;" class="alert alert-danger" role="alert">
            <%= session.getAttribute("passwordDiff")%>
        </div>
        <% session.removeAttribute("passwordDiff"); %>
        <% }%>

        <% if (session.getAttribute("editFailure") != null) {%>
        <div style="display:inline; width: 500px; margin-left: 20px;" class="alert alert-danger" role="alert">
            <%= session.getAttribute("editFailure")%>
        </div>
        <% session.removeAttribute("editFailure"); %>
        <% }%>

        <% if (session.getAttribute("editSuccess") != null) {%>
        <div style="display:inline; width: 500px; margin-left: 20px;" class="alert alert-success" role="alert">
            <%= session.getAttribute("editSuccess")%>
        </div>
        <% session.removeAttribute("editSuccess"); %>
        <% }%>

        <br>
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
                    <% ArrayList<User> users = (ArrayList<User>) request.getAttribute("users");
                        for (User user : users) {
                            String removeUserId = "exampleModal" + user.getUserId();
                            String editUserId = "offcanvasExample" + user.getUserId();
                    %>
                    <tr>
                        <td><%= user.getUserId()%></td>
                        <td><%= user.getUsername()%></td>
                        <td><%= user.getFirstName()%></td>
                        <td><%= user.getLastName()%></td>
                        <td><%= user.getRole().name()%></td>
                        <td>
                            <button type="button" class="btn btn-primary" data-bs-toggle="offcanvas" data-bs-target="#<%= editUserId%>">
                                Edit
                            </button>
                            <div class="offcanvas offcanvas-start" tabindex="-1" id="<%= editUserId%>">
                                <div class="offcanvas-header">
                                    <h5 class="offcanvas-title" id="offcanvasExampleLabel">User Edit</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
                                </div>
                                <form class="row g-3" method="post" action="${pageContext.request.contextPath}/users">
                                    <div class="offcanvas-body">
                                        <div class="col-12">
                                            <label for="UserId" class="form-label">ID</label>
                                            <input type="number" class="form-control" name="UserId" id="UserId" value="<%= user.getUserId()%>" readonly>
                                        </div>
                                        <div class="col-12">
                                            <label for="editUserName" class="form-label">Username</label>
                                            <input type="text" class="form-control" id="editUserName" name="editUserName" value="<%= user.getUsername()%>" required>
                                        </div>
                                        <input hidden name="oldUserName" value="<%= user.getUsername()%>">
                                        <div class="col-12">
                                            <label for="editFirstName" class="form-label">First Name</label>
                                            <input type="text" class="form-control" id="editFirstName" name="editFirstName" value="<%= user.getFirstName()%>" required>
                                        </div>
                                        <div class="col-12">
                                            <label for="editLastName" class="form-label">Last Name</label>
                                            <input type="text" class="form-control" id="editLastName" name="editLastName" value="<%= user.getLastName()%>" required>
                                        </div>
                                        <div class="col-12">
                                            <label for="editPassword" class="form-label">Password</label>
                                            <input type="password" class="form-control" id="editPassword" name="editPassword">
                                        </div>
                                        <input hidden name="oldPassword" value="<%= user.getPassword()%>">
                                        <div class="col-12">
                                            <label for="newPassword" class="form-label">Please Input Password Again</label>
                                            <input type="password" class="form-control" id="newPassword" name="newPassword">
                                        </div>
                                        <br>
                                        <div class="col-12" style="margin-bottom: 10px">
                                            <label class="form-label">Role</label>
                                            <select name="editRole">
                                                <%
                                                    UserRoles[] roles = UserRoles.values();
                                                    for (UserRoles role : roles) {
                                                        boolean isSelected = (role == user.getRole());
                                                        int roleValue = role.getValue();
                                                        if (isSelected) {
                                                %>
                                                <option value="<%= roleValue%>" selected><%= role.name()%></option>
                                                <%
                                                } else {
                                                %>
                                                <option value="<%= roleValue%>"><%= role.name()%></option>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </select>
                                        </div>
                                        <div class="col-12">
                                            <button class="btn btn-primary" type="submit" >
                                                Update
                                            </button>
                                            <button type="reset" class="btn btn-primary" onclick="return window.location.reload()">Reset</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#<%= removeUserId%>">
                                Remove
                            </button>

                            <form method="post" action="${pageContext.request.contextPath}/users">
                                <div class="modal fade" id="<%= removeUserId%>" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h1 class="modal-title fs-5" id="exampleModalLabel">Remove User</h1>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body">
                                                You are currently deleting the action of user
                                                <b style="color: red; display: inline;"><%= user.getUsername()%></b>.<br>
                                                Are you sure you want to execute it?<br>
                                                Once confirmed, you will not be able to restore it.
                                                <input type="hidden" name="removeUser" value="<%= user.getUsername()%>">
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                <button type="submit" class="btn btn-primary" style="background-color: red" >Confirm</button>
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

