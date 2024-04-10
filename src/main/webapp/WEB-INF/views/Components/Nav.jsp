<%@ page import="com.cems.Enums.UserRoles" %>
<%
  Users userBean = (Users) session.getAttribute("user");
%>
<nav class="navbar navbar-expand-lg bg-body-tertiary">
  <div class="container-fluid">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/">Rentai List</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav">
        <li class="nav-item">
          <a class="nav-link" aria-current="page" href="${pageContext.request.contextPath}/">Home</a>
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
        <% if (userBean.getRole() == UserRoles.ADMIN) { %>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/users">users</a>
        </li>
        <% } %>
        <div class="btn-group dropstart position-absolute top-10 end-0" style="margin-right: 20px;">
          <button type="button" class="btn btn-secondary dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
            Username
          </button>
          <ul class="dropdown-menu">
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/update-info">Account information</a></li>
            <li><a class="dropdown-item" href="#">Logout</a></li>
          </ul>
        </div>
      </ul>
    </div>
  </div>
</nav>