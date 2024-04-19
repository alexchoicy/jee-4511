<%@ page import="com.cems.Model.Users" %>

    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Account</title>
            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
                integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
                crossorigin="anonymous">
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
                crossorigin="anonymous"></script>
        </head>

        <script>
            function togglePasswordAgain() {
                var inputPassword = document.getElementById("inputPassword");
                var inputPasswordAgainContainer = document.getElementById("inputPasswordAgainContainer");
                if (inputPassword.value.length > 0) {
                    inputPasswordAgainContainer.style.display = "";
                } else {
                    inputPasswordAgainContainer.style.display = "none";
                }
            }


        </script>

        <body class="bg-light">
            <%@include file="../Components/Nav.jsp" %>
                <br>

                <div style="margin-left: 20px; margin-right: 20px;">
                    <h5>Personal information</h5>
                    <h6>Manage your name and contact information. Personal information is private and will not be
                        displayed to
                        other users. View our privacy policy</h6>

                    <br>

                    <div id="content" style="height:200px;width:450px;float:left;">
                        <% if (request.getAttribute("updateSuccess") !=null) {%>
                            <div class="alert alert-success" role="alert">
                                <%= request.getAttribute("updateSuccess")%>
                            </div>
                            <% }%>
                                <% if (request.getAttribute("noOldPassword") !=null) {%>
                                    <div class="alert alert-danger" role="alert">
                                        <%= request.getAttribute("noOldPassword")%>
                                    </div>
                                    <% }%>

                                        <% if (request.getAttribute("passwordWrong") !=null) {%>
                                            <div class="alert alert-danger" role="alert">
                                                <%= request.getAttribute("passwordWrong")%>
                                            </div>
                                            <% }%>

                                                <% if (request.getAttribute("passwordAgain") !=null) {%>
                                                    <div class="alert alert-danger" role="alert">
                                                        <%= request.getAttribute("passwordAgain")%>
                                                    </div>
                                                    <% }%>

                                                        <% if (request.getAttribute("passwordDiff") !=null) {%>
                                                            <div class="alert alert-danger" role="alert">
                                                                <%= request.getAttribute("passwordDiff")%>
                                                            </div>
                                                            <% }%>

                                                                <% if (request.getAttribute("updateFailure") !=null) {%>
                                                                    <div class="alert alert-danger" role="alert">
                                                                        <%= request.getAttribute("updateFailure")%>
                                                                    </div>
                                                                    <% }%>
                                                                        <form class="row g-3"
                                                                            onsubmit="return checkPassword(event)"
                                                                            method="post"
                                                                            action="${pageContext.request.contextPath}/update-info">
                                                                            <div class="col-12">
                                                                                <label for="inputId"
                                                                                    class="form-label">ID</label>
                                                                                <input type="number"
                                                                                    class="form-control" id="inputId"
                                                                                    value="${user.userId}" readonly>
                                                                            </div>
                                                                            <div class="col-12">
                                                                                <label for="inputUserName"
                                                                                    class="form-label">Username</label>
                                                                                <input type="text" class="form-control"
                                                                                    id="inputUsername"
                                                                                    name="inputUsername"
                                                                                    value="${user.username}" required>
                                                                            </div>
                                                                            <div class="col-md-6">
                                                                                <label for="inputFirstName"
                                                                                    class="form-label">First
                                                                                    Name</label>
                                                                                <input type="text" class="form-control"
                                                                                    id="inputFirstName"
                                                                                    name="inputFirstName"
                                                                                    value="${user.firstName}" required>
                                                                            </div>
                                                                            <div class="col-md-6">
                                                                                <label for="inputLastName"
                                                                                    class="form-label">Last Name</label>
                                                                                <input type="text" class="form-control"
                                                                                    id="inputLastName"
                                                                                    name="inputLastName"
                                                                                    value="${user.lastName}" required>
                                                                            </div>
                                                                            <div class="col-12">
                                                                                <label for="inputOldPassword"
                                                                                    class="form-label">Old
                                                                                    Password<br>(If you need to update
                                                                                    your profile please enter your old
                                                                                    password.)</label>
                                                                                <input type="password"
                                                                                    class="form-control"
                                                                                    id="inputOldPassword"
                                                                                    name="inputOldPassword">
                                                                            </div>
                                                                            <div class="col-12">
                                                                                <label for="inputPassword"
                                                                                    class="form-label">New
                                                                                    Password</label>
                                                                                <input type="password"
                                                                                    class="form-control"
                                                                                    id="inputPassword"
                                                                                    name="inputPassword"
                                                                                    oninput="togglePasswordAgain()">
                                                                            </div>
                                                                            <div class="col-12"
                                                                                id="inputPasswordAgainContainer"
                                                                                style="display: none">
                                                                                <label for="newPassword"
                                                                                    class="form-label">Please Input
                                                                                    Password Again</label>
                                                                                <input type="password"
                                                                                    class="form-control"
                                                                                    id="newPassword" name="newPassword">
                                                                            </div>
                                                                            <div class="col-md-6">
                                                                                <button type="submit"
                                                                                    class="btn btn-primary">Save
                                                                                    changes</button>
                                                                            </div>
                                                                            <div class="col-md-6">
                                                                                <button type="reset"
                                                                                    class="btn btn-primary"
                                                                                    onclick="return window.location.reload()">Reset</button>
                                                                            </div>
                                                                        </form>
                    </div>
                </div>

        </body>

        </html>