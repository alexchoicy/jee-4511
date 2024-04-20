<%@ page import="com.cems.Model.Users" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Create User</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
    </head>
    <body class="bg-light">
        <%@include file="../Components/Nav.jsp"%>
        <br>

        <div style="margin-left: 20px; margin-right: 20px;">
            <h4>Create User</h4>
            <br>
            <div id="content" style="height:200px;width:450px;float:left;">
                <% if (request.getAttribute("createSuccess") != null) {%>
                <div class="alert alert-success" role="alert">
                    <%= request.getAttribute("createSuccess")%>
                </div>
                <% }%>

                <% if (request.getAttribute("passwordAgain") != null) {%>
                <div class="alert alert-danger" role="alert">
                    <%= request.getAttribute("passwordAgain")%>
                </div>
                <% }%>

                <% if (request.getAttribute("passwordDiff") != null) {%>
                <div class="alert alert-danger" role="alert">
                    <%= request.getAttribute("passwordDiff")%>
                </div>
                <% }%>
                
                <% if (request.getAttribute("noUsername") != null) {%>
                <div class="alert alert-danger" role="alert">
                    <%= request.getAttribute("noUsername")%>
                </div>
                <% }%>

                <% if (request.getAttribute("createFailure") != null) {%>
                <div class="alert alert-danger" role="alert">
                    <%= request.getAttribute("createFailure")%>
                </div>
                <% }%>
                
                <form class="row g-3" method="post" action="${pageContext.request.contextPath}/createUser">

                    <div class="col-12">
                        <label for="Username" class="form-label">Username</label>
                        <input type="text" class="form-control" id="Username" name="Username" required>
                    </div>
                    <div class="col-md-6">
                        <label for="firstName" class="form-label">First Name</label>
                        <input type="text" class="form-control" id="firstName" name="firstName" required>
                    </div>
                    <div class="col-md-6">
                        <label for="lastName" class="form-label">Last Name</label>
                        <input type="text" class="form-control" id="lastName" name="lastName" required>
                    </div>
                    <div class="col-12">
                        <label for="Password" class="form-label">Password</label>
                        <input type="password" class="form-control" id="Password" name="Password" required>
                    </div>
                    <div class="col-12">
                        <label for="passwordConfirm" class="form-label">Please Input Password Again</label>
                        <input type="password" class="form-control" id="passwordConfirm" name="passwordConfirm" required>
                    </div>
                    <div class="col-12">
                        <label for="Role" class="form-label">Role :</label>
                        <select id="Role" name="Role" requied>
                            <option value="1">Admin</option>
                            <option value="2">Technician</option>
                            <option value="3">Staff</option>
                            <option value="4" >Courier</option>
                            <option value="5" selected>User</option>
                        </select>
                    </div>
                    <div class="col-md-6">
                        <button type="submit" class="btn btn-primary">Create</button>
                    </div>
                    <div class="col-md-6">
                        <button type="reset" class="btn btn-primary" onclick="return window.location.reload()">Reset</button>
                    </div>
                </form>
            </div>
        </div>

    </body>

</html>

