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
<body class="bg-light">
<%@include file="../Components/Nav.jsp"%>
<br>
<div class="container-fluid">
  <div class="mt-5">
    <div class="card mt-2">
      <div class="card-header bg-primary text-white">
        Waiting to Approve
      </div>
      <div class="card-body">
        <table
                class="table table-striped table-hover text-center"
        >
          <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Start Date</th>
            <th scope="col">End Date</th>
            <th scope="col">Created Date</th>
          </tr>
          </thead>
          <tbody id="waitingToApproveBody"></tbody>
        </table>
      </div>
    </div>

    <div class="card mt-2">
      <div class="card-header bg-success text-white">Active</div>
      <div class="card-body">
        <table
                class="table table-striped table-hover text-center"
        >
          <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Start Date</th>
            <th scope="col">End Date</th>
            <th scope="col">Created Date</th>
          </tr>
          </thead>
          <tbody id="activeBody"></tbody>
        </table>
      </div>
    </div>

    <div class="card mt-2">
      <div class="card-header bg-secondary text-white">
        History
      </div>
      <div class="card-body">
        <table
                class="table table-striped table-hover text-center"
        >
          <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Start Date</th>
            <th scope="col">End Date</th>
            <th scope="col">Created Date</th>
          </tr>
          </thead>
          <tbody id="historyBody"></tbody>
        </table>
      </div>
    </div>
  </div>
</div>
</body>
</html>
