<%@ page import="com.cems.Model.EquipmentDisplay" %>
<%@ page import="com.cems.Model.EquipmentItem" %>
<%@ page import="com.cems.Model.Equipment" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.cems.Model.Users" %>
<%@ page import="com.cems.Enums.UserRoles" %>
<html>
<head>
    <title>Title</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>


</head>
<%
    EquipmentDisplay display = (EquipmentDisplay) request.getAttribute("equipmentDisplay");
    ArrayList<EquipmentItem> item = display.getItems();
    Equipment equipment = display.getEquipment();
    Users user = (Users) request.getSession().getAttribute("user");
%>
<script>
    const maxQuantity = <%= equipment.getAvailableQuantity()%>;

    function addQuantity(number) {
        const quantity = document.getElementById("quantity");
        if (quantity.value === "" || quantity.value === null) {
            quantity.value = 0;
        }
        quantity.value = parseInt(quantity.value) + number;
        if (parseInt(quantity.value) < 0) {
            quantity.value = 0;
        }
        if (parseInt(quantity.value) > maxQuantity) {
            quantity.value = maxQuantity;
        }
    }

    <% if (user.getRole() == UserRoles.ADMIN) { %>
    function onSearchSubmit(event) {
        event.preventDefault();
        const searchText = document.getElementById("searchText").value;
        fetchItemDetail(searchText);
    }

    function fetchItemDetail(serach) {
        document.querySelector("tbody").innerHTML = "";
        fetch("${pageContext.request.contextPath}/equipment/<%= equipment.getId()%>?search=" + serach)
            .then((response) => response.json())
            .then((data) => {
                data.forEach((element) => {
                    const {
                        id,
                        serialNumber,
                        status,
                        borrowedTimes,
                        location,
                    } = element;
                    const row = document.createElement("tr");
                    row.innerHTML = "<td>" + id + "</td>" +
                        "<td>" + serialNumber + "</td>" +
                        "<td>" + status + "</td>" +
                        "<td>" + borrowedTimes + "</td>" +
                        "<td>" + location.name + "</td>" +
                        "<td></td>"
                    ;
                    document.querySelector("tbody").appendChild(row);
                });
            });
    }

    document.addEventListener("DOMContentLoaded", function () {
        fetchItemDetail("");
    });
<% } %>
    function addToReservationCart(event) {
        event.preventDefault();
    }
</script>

<body>
<%@include file="../Components/Nav.jsp" %>

<div class="col pb-2 px-3">
    <div
            class="d-flex justify-content-between align-items-center p-3 pb-2 mb-3 border-bottom"
    >
        <h2 class="h2">Equipment Detail</h2>
        <div class="btn-toolbar mb-2 mb-md-0"></div>
    </div>
    <div class="row gx-6">
        <div class="col-lg-6">
            <div class="d-flex justify-content-center">
                <img
                        src="${pageContext.request.contextPath}/<%= equipment.getImagePath()%>"
                        class="img-thumbnail object-fit-cover"
                        alt=""
                />
            </div>
        </div>
        <div class="col-lg-6">
            <h4 class="h4 text-dark"><%=equipment.getName()%>
            </h4>
            <hr/>
            <div class="row my-3">
                <div class="h3 col-lg-6">Available :</div>
                <div class="col-lg-6"><%= equipment.getAvailableQuantity()%>
                </div>
            </div>
            <hr/>
            <div class="row my-3">
                <div class="h4 col-lg-6">ID :</div>
                <div class="col-lg-6"><%= equipment.getId()%>
                </div>
            </div>
            <hr/>
            <div class="h4">Details</div>
            <p><%= equipment.getDescription()%>
            </p>
            <hr/>
            <div class="row mb-4 justify-content-center">
                <form onsubmit="return addToReservationCart(event)">

                    <div class="col">
                        <label rel="quantity" class="mb-2 d-block">Quantity</label>
                        <div class="input-group mb-3" style="width: 170px">
                            <button
                                    class="btn btn-white border border-secondary px-3"
                                    type="button"
                                    id="button-addon1"
                                    data-mdb-ripple-color="dark"
                                    onclick="addQuantity(-1)"
                            >
                                <img
                                        src="${pageContext.request.contextPath}/resources/images/buttons/minus-solid.svg"
                                        alt=""
                                        width="20px "
                                        height="20px"
                                />
                            </button>
                            <input
                                    type="text"
                                    class="form-control text-center border border-secondary"
                                    placeholder="<%= equipment.getAvailableQuantity()%>"
                                    max="<%= equipment.getAvailableQuantity()%>"
                                    min="0"
                                    id="quantity"
                                    name="quantity"
                                    required
                            />

                            <button
                                    class="btn btn-white border border-secondary px-3 rounded-end"
                                    type="button"
                                    id="button-addon2"
                                    data-mdb-ripple-color="dark"
                                    onclick="addQuantity(1)"
                            >
                                <img
                                        width="20px"
                                        height="20px"
                                        src="${pageContext.request.contextPath}/resources/images/buttons/plus-solid.svg"
                                        alt=""
                                />
                            </button>
                            <div class="invalid-feedback">
                                The Max avaiable is 2
                            </div>
                        </div>
                        <button class="btn btn-primary">
                            Add to Reservation List
                        </button>
                    </div>
                </form>
            </div>

        </div>
        <% if (user.getRole() == UserRoles.ADMIN) { %>
        <form
                class="d-flex p-3"
                role="search"
                onsubmit="return onSearchSubmit(event)"
        >
            <input
                    class="form-control me-2"
                    type="search"
                    placeholder="Search"
                    aria-label="Search"
                    id="searchText"
            />
            <button class="btn btn-outline-success" type="submit">
                Search
            </button>
        </form>
        <div class="p-3">
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Serial Number</th>
                    <th scope="col">Status</th>
                    <th scope="col">Borrowed Times</th>
                    <th scope="col">Location</th>
                    <th scope="col">Action</th>
                </tr>
                </thead>
                <tbody id="itemTable">
                </tbody>
            </table>
        </div>
          <%  } %>
    </div>
</div>

</body>
</html>
