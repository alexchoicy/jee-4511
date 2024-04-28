<%@ page import="com.cems.Enums.UserRoles" %>
<%@ page import="com.cems.Model.User" %>
<%
    User userBean = (User) session.getAttribute("user");
%>
<nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/">Reservation System</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/wishList">Wish List</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/reservations">Reservations</a>
                </li>
                <% if (userBean.getRole() == UserRoles.ADMIN) { %>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/users">users</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/damages">Damaged Report</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/analytics">Analytics</a>
                </li>
                <% } %>
                <% if (userBean.getRole() == UserRoles.ADMIN || userBean.getRole() == UserRoles.COURIER) { %>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/deliveryList">Delivery List</a>
                </li>
                <% } %>
                <div class="btn-group dropstart position-absolute top-10 end-0" style="margin-right: 20px;">
                    <button
                            class="btn btn-white border border-secondary px-3"
                            onclick="return window.location.href='${pageContext.request.contextPath}/cart'"
                    >
                        <img src="${pageContext.request.contextPath}/resources/images/buttons/cart-shopping-solid.svg"
                             alt=""
                             width="20px "
                             height="20px">
                    </button>
                    <button type="button" class="btn btn-secondary dropdown-toggle" data-bs-toggle="dropdown"
                            aria-expanded="false">
                        <%= String.format("%s %s", userBean.getFirstName(), userBean.getLastName())%>
                    </button>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/update-info">Account
                            information</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Logout</a></li>
                    </ul>
                </div>
            </ul>
        </div>
    </div>
</nav>