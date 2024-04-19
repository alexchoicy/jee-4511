<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>

    <head>
        <title>Login</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    </head>
    <script>
        let hasError = false;
        let username = "";
        let password = "";

        function onFormSubmit(e) {
            let isValid = true;
            const usernameDom = document.getElementById("username");
            const passwordDom = document.getElementById("password");

            if (usernameDom.value === "") {
                isValid = false;
                usernameDom.classList.add("is-invalid");
            } else {
                usernameDom.classList.remove("is-invalid");
            }

            if (passwordDom.value === "") {
                isValid = false;
                passwordDom.classList.add("is-invalid");
            } else {
                passwordDom.classList.remove("is-invalid");
            }

            if (!isValid) {
                e.preventDefault();
            }
        }

    // display when there are error
    // I should use fetch Um... it looks dum...
    <% if (request.getAttribute("error") != null) {%>
            hasError = true;
            username = '<%= (request.getAttribute("username") != null) ? request.getAttribute("username") : "weird" %>';
            password = '<%= (request.getAttribute("password") != null) ? request.getAttribute("password") : "weird" %>';
    <% } %>

            document.addEventListener("DOMContentLoaded", function () {
                if (hasError) {
                    const usernameDom = document.getElementById("username");
                    const passwordDom = document.getElementById("password");
                    usernameDom.value = username;
                    passwordDom.value = password;
                    usernameDom.classList.add("is-invalid");
                    passwordDom.classList.add("is-invalid");
                }
            });

    </script>

    <body class="bg-light">
        <div class="min-vh-100 d-flex flex-column align-items-center justify-content-center">
            <div>
                <div class="pb-5 text-center fw-bold  fs-1">Centralized equipment system</div>
                <% if (request.getAttribute("error") !=null) { %>
                    <div class="alert alert-danger" role="alert">
                        <%= request.getAttribute("error") %>
                    </div>
                    <% } %>
                        <form onsubmit="return onFormSubmit(event);" action="${pageContext.request.contextPath}/login"
                            method="post" class="p-3 need-validation" novalidate>
                            <div class="row mb-3 align-items-center">
                                <label for="username" class="col-form-label">Username</label>
                                <div class="">
                                    <input type="text" class="form-control" id="username" name="username" required>
                                    <div class="invalid-feedback">Please enter a valid username.</div>
                                </div>
                            </div>
                            <div class="row mb-3 align-items-center">
                                <label for="password" class="col-form-label">Password</label>
                                <div class="">
                                    <input type="password" class="form-control" id="password" name="password" required>
                                    <div class="invalid-feedback">Please enter a valid password.</div>
                                </div>
                            </div>
                            <div class="d-flex justify-content-between">
                                <div class="d-flex justify-content-end">
                                    <button type="submit" class="btn btn-primary">Login</button>
                                </div>
                            </div>
                        </form>
            </div>
        </div>
    </body>

    </html>