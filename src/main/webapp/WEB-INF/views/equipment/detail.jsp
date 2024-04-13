<%@ page import="com.cems.Model.Display.EquipmentDisplay" %>
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

    function removeItem(id, itemID) {
        const url = '${pageContext.request.contextPath}/equipment/' + id + '/items/' + itemID;
        fetch(url, {
            method: 'DELETE',
        }).then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text);
                });
            }
            return response.text();
        }).then(data => {
            alert("Item id " + itemID + " removed");
            window.location.href = '${pageContext.request.contextPath}/equipment/<%= equipment.getId()%>';
            console.log(data);
        }).catch((error) => {
            console.error('Error:', error);
            alert(error.message);
        });
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
                        <%--"<td> <button data-item-id='"+ id + "' onclick='removeItem("+<%= equipment.getId()%> + "," + id +")' class='btn btn-danger'>Remove Items</button></td>"--%>
                        "<td> <button data-item-id='" + id + "' class='btn btn-danger' data-bs-toggle='modal' data-bs-target='#RemoveItemModal' >Remove Items</button></td>"
                    ;
                    document.querySelector("tbody").appendChild(row);
                });
            });
    }

    document.addEventListener("DOMContentLoaded", function () {
        fetchItemDetail("");

        const removeItemModal = document.getElementById("RemoveItemModal");
        removeItemModal.addEventListener('show.bs.modal', event => {
            const button = event.relatedTarget;
            const itemID = button.getAttribute('data-item-id');

            const confirmBtn = document.getElementById("RemoveItemModalConfirmBtn");

            confirmBtn.addEventListener('click', () => {
                removeItem(<%= equipment.getId()%>, itemID);
            });

            const body = removeItemModal.querySelector(".modal-body");

            body.innerHTML = "Are you sure to remove item id : " + itemID;

            const title = removeItemModal.querySelector(".modal-title");
            title.innerHTML = "Remove Item ID : " + itemID;
        })
    });
    <% } %>

    function addToReservationCart(event) {
        event.preventDefault();


        const quantity = document.getElementById('quantity').value;

        const params = new URLSearchParams();
        params.append('quantity', quantity);


        fetch("${pageContext.request.contextPath}/equipment/<%= equipment.getId()%>/cart", {
                method: "POST",
            body: params
            }).then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text);
                });
            }
            return response.text();
        }).then(data => {
            console.log(data);
            alert("Item Added");
        }).catch((error) => {
            console.error('Error:', error);
            alert(error.message);
        });
    }

    function goToEditMode() {
        window.location.href = "${pageContext.request.contextPath}/equipment/<%= equipment.getId()%>/edit";
    }

    function removeEquipment(id) {
        const url = '${pageContext.request.contextPath}/equipment/' + id;
        fetch(url, {
            method: 'DELETE',
        }).then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text);
                });
            }
            return response.text();
        }).then(data => {
            alert("Equipment removed");
            window.location.href = '${pageContext.request.contextPath}/';
        }).catch((error) => {
            console.error('Error:', error);
            alert(error.message);
        });
    }
</script>

<body>
<%@include file="../Components/Nav.jsp" %>

<div class="col pb-2 px-3">
    <div
            class="d-flex justify-content-between align-items-center p-3 pb-2 mb-3 border-bottom"
    >
        <h2 class="h2">Equipment Detail</h2>
        <div class="btn-toolbar mb-2 mb-md-0">
            <% if (user.getRole() == UserRoles.ADMIN) { %>
            <button type="button" class="btn btn-danger mx-2" data-bs-toggle="modal" data-bs-target="#RemoveModal">
                Remove
            </button>
            <button
                    class="btn btn-primary"
                    onclick="goToEditMode()"
            >
                Edit
            </button>
            <% } %>
        </div>
    </div>
    <div class="row gx-6">
        <div class="col-lg-6">
            <div class="d-flex justify-content-center">
                <img
                        src="${pageContext.request.contextPath}/<%= equipment.getImagePath()%>"
                        class="img-thumbnail object-fit-cover"
                        width="600px"
                        height="600px"
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
            <div class="h4 pt-2">Options</div>
            <div class="row">
                <div class="col">
                    <div class="row">
                        <label for="isStaffOnly" class="col">Staff only mode</label>
                        <input
                                type="checkbox"
                                name="isStaffOnly"
                                id="isStaffOnly"
                                class="col"
                                <%= equipment.isStaffOnly() ? "checked" : ""%>
                                disabled
                        />
                    </div>
                </div>
                <div class="col">
                    <div class="row">
                        <label for="isListed" class="col">Listed</label>
                        <input
                                type="checkbox"
                                name="isListed"
                                id="isListed"
                                class="col"
                                <%= equipment.isListed() ? "checked" : ""%>
                                disabled
                        />
                    </div>
                </div>
            </div>
            <hr/>
            <div class="h4">Details</div>
            <p><%= equipment.getDescription()%>
            </p>
            <hr/>
            <div class="row mb-4 justify-content-center">
                <form onsubmit="return addToReservationCart(event)">
                    <% if (!equipment.isListed()) { %>
                    <div class="alert alert-danger" role="alert">
                        This equipment is not available for reservation
                    </div>
                    <% } else if (equipment.isStaffOnly() && user.getRole() == UserRoles.USER) {%>
                    <div class="alert alert-danger" role="alert">
                        This equipment is only available for staff
                    </div>
                    <% } else { %>
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
             `                           width="20px "
                                        height="20px"`
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
                        <button class="btn btn-primary" type="submit">
                            Add to Reservation List
                        </button>
                        <% } %>
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
        <% } %>
    </div>
</div>

<% if (user.getRole() == UserRoles.ADMIN) { %>
<div class="modal fade" id="RemoveModal" tabindex="-1" aria-labelledby="RemoveModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="RemoveModalLabel">Remove Equipment</h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                Are you sure to Remove the Equipment?
                Make sure there is no item left in the equipment
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="button" class="btn btn-danger" onclick="removeEquipment(<%= equipment.getId()%>)">
                    Confirm
                </button>
            </div>
        </div>
    </div>
</div>


<div class="modal fade" id="RemoveItemModal" tabindex="-1" aria-labelledby="RemoveItemModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="RemoveItemModalLabel"></h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body" id="modal-body">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                <button type="button" class="btn btn-danger" id="RemoveItemModalConfirmBtn">Confirm</button>
            </div>
        </div>
    </div>
</div>
<% } %>
</body>
</html>