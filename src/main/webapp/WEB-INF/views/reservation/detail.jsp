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
<div class="d-flex justify-content-end m-2">
  <button class="btn btn-success m-2">Approve</button>
  <button class="btn btn-danger m-2">Deliced</button>
  <button class="btn btn-primary m-2">Check out</button>
  <button class="btn btn-primary m-2">Return</button>
</div>

<div class="container-fluid m-5">
  <div class="row">
    <div class="col-11 rounded-1 bg-white my-3 p-1">
      <div class="row p-3">
        <div class="col-lg-8">
          <div class="row">
            <div class="col">Reservation User Name :</div>
            <div class="col">
              <div>USER NAME</div>
            </div>
          </div>
          <div class="row">
            <div class="col">Location</div>
            <div class="col">
              <div>LOCATION</div>
            </div>
          </div>
          <div class="row">
            <div class="col">Start Time</div>
            <div class="col">
              <div>DATE TIME</div>
            </div>
          </div>
          <div class="row">
            <div class="col">End Time</div>
            <div class="col">
              <div>DATE TIME</div>
            </div>
          </div>
          <div class="row">
            <div class="col">Check out Time</div>
            <div class="col">
              <div>DATE TIME</div>
            </div>
          </div>
          <div class="row">
            <div class="col">Return Time</div>
            <div class="col">
              <div>DATE TIME</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col-11 rounded-1 bg-white my-3 p-1">
        <div class="p-3">
          <table class="table text-center">
            <thead>
            <tr>
              <th scope="col">Item ID</th>
              <th scope="col">Item Name</th>
              <th scope="col">Request Quantity</th>
            </tr>
            </thead>
          </table>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
