<%@ page import="com.cems.Model.Users" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Account</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
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

    <body>
        <nav class="navbar navbar-expand-lg bg-body-tertiary">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">Rentai List</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                        aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="index.html">Home</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="records.html">Recods</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="wishList.html">Wish List</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="booking.html">Booking</a>
                        </li>
                        <div class="btn-group dropstart position-absolute top-10 end-0" style="margin-right: 20px;">
                            <button type="button" class="btn btn-secondary dropdown-toggle" data-bs-toggle="dropdown"
                                    aria-expanded="false">
                                Username
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="information.html">Change information</a></li>
                                <li><a class="dropdown-item" href="#">Logout</a></li>
                            </ul>
                        </div>
                    </ul>
                </div>
            </div>
        </nav>

        <br>

        <div style="margin-left: 20px; margin-right: 20px;">
            <h5>Personal information</h2>
                <h6>Manage your name and contact information. Personal information is private and will not be displayed to
                    other users. View our privacy policy</h3>

                    <br>

                    <div id="content" style="height:200px;width:450px;float:left;">
                        <% if (request.getAttribute("noOldPassword") != null) {%>
                        <div class="alert alert-danger" role="alert">
                            <%= request.getAttribute("noOldPassword")%>
                        </div>
                        <% }%>

                        <% if (request.getAttribute("passwordWrong") != null) {%>
                        <div class="alert alert-danger" role="alert">
                            <%= request.getAttribute("passwordWrong")%>
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
                        
                        <% if (request.getAttribute("updateFailure") != null) {%>
                        <div class="alert alert-danger" role="alert">
                            <%= request.getAttribute("updateFailure")%>
                        </div>
                        <% }%>
                        <form class="row g-3" onsubmit="return checkPassword(event)" method="post" action="${pageContext.request.contextPath}/information">
                            <div class="col-12">
                                <label for="inputId" class="form-label">ID</label>
                                <input type="number" class="form-control" id="inputId" value="${user.userId}" readonly>
                            </div>
                            <div class="col-12">
                                <label for="inputUserName" class="form-label">Username</label>
                                <input type="text" class="form-control" id="inputUsername" name="inputUsername" value="${user.username}" required>
                            </div>
                            <div class="col-md-6">
                                <label for="inputFirstName" class="form-label">First Name</label>
                                <input type="text" class="form-control" id="inputFirstName" name="inputFirstName" value="${user.firstName}" required>
                            </div>
                            <div class="col-md-6">
                                <label for="inputLastName" class="form-label">Last Name</label>
                                <input type="text" class="form-control" id="inputLastName" name="inputLastName" value="${user.lastName}" required>
                            </div>
                            <div class="col-12">
                                <label for="inputOldPassword" class="form-label">Old Password<br>(If you need to update your profile please enter your old password.)</label>
                                <input type="password" class="form-control" id="inputOldPassword" name="inputOldPassword">
                            </div>
                            <div class="col-12">
                                <label for="inputPassword" class="form-label">New Password</label>
                                <input type="password" class="form-control" id="inputPassword" name="inputPassword" oninput="togglePasswordAgain()">
                            </div>
                            <div class="col-12" id="inputPasswordAgainContainer" style="display: none">
                                <label for="inputPasswordAgain" class="form-label">Please Input Password Again</label>
                                <input type="password" class="form-control" id="newPassword" name="newPassword">
                            </div>
                            <div class="col-md-6">
                                <button type="submit" class="btn btn-primary">Save changes</button>
                            </div>
                            <div class="col-md-6">
                                <button type="reset" class="btn btn-primary">Reset</button>
                            </div>
                        </form>
                    </div>
                    </div>

                    </body>

                    </html>

