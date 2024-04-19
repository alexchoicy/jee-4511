<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="com.cems.Model.Display.EquipmentDisplay" %>
        <%@ page import="com.cems.Model.EquipmentItem" %>
            <%@ page import="com.cems.Model.Equipment" %>
                <%@ page import="java.util.ArrayList" %>
                    <%@ page import="com.cems.Model.Request.CreateEquipmentItem" %>
                        <%@ taglib prefix="options" uri="/WEB-INF/tlds/cems_equipment_tags.tld" %>
                            <html>

                            <head>
                                <title>Title</title>
                                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                                    rel="stylesheet"
                                    integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
                                    crossorigin="anonymous">
                                <script
                                    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                                    integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
                                    crossorigin="anonymous"></script>


                            </head>
                            <% EquipmentDisplay display=(EquipmentDisplay) request.getAttribute("equipmentDisplay");
                                ArrayList<EquipmentItem> item = display.getItems();
                                Equipment equipment = display.getEquipment();
                                ArrayList<CreateEquipmentItem> errorItems = (ArrayList<CreateEquipmentItem>)
                                        request.getAttribute("errorItems");
                                        int numberOfNewItems = errorItems == null ? 0 : errorItems.size();
                                        %>
                                        <script>
                                            let numberOfNewItems = 0;

                                            function onSearchSubmit(event) {
                                                event.preventDefault();
                                                const searchText = document.getElementById("searchText").value;
                                                fetchItemDetail(searchText);
                                            }

                                            function fetchItemDetail(serach) {
                                                document.getElementById("itemTable").innerHTML = "";
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
                                                                "<td> <button data-item-id='" + id + "' class='btn btn-danger' data-bs-toggle='modal' data-bs-target='#RemoveItemModal' >Remove Items</button></td>"
                                                                ;
                                                            document.getElementById("itemTable").appendChild(row);
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
                                                        removeItem(<%= equipment.getId() %>, itemID);
                                                    });

                                                    const body = removeItemModal.querySelector(".modal-body");

                                                    body.innerHTML = "Are you sure to remove item id : " + itemID;

                                                    const title = removeItemModal.querySelector(".modal-title");
                                                    title.innerHTML = "Remove Item ID : " + itemID;
                                                })
                                            });

                                            function removeRow(id) {
                                                document.getElementById("row[" + id + "]").remove();
                                                reindexRow();
                                            }

                                            function reindexRow() {
                                                const rows = document.getElementById("newItemTableBody").querySelectorAll("tr");
                                                let numberOfNewItemsFix = 0;
                                                rows.forEach((row, index) => {
                                                    numberOfNewItemsFix++;
                                                    const inputs = row.querySelectorAll("input");
                                                    inputs.forEach((input) => {
                                                        input.name = input.name.replace(/\[\d+]/, "[" + numberOfNewItemsFix + "]");
                                                    });
                                                    const selects = row.querySelectorAll("select");
                                                    selects.forEach((select) => {
                                                        select.name = select.name.replace(/\[\d+]/, "[" + numberOfNewItemsFix + "]");
                                                    });
                                                });
                                                numberOfNewItems = numberOfNewItemsFix;
                                                document.getElementById("numberOfNewItems").value = numberOfNewItems;
                                            }

                                            function onAddnewItem(event) {
                                                event.preventDefault();
                                                let serial = document.getElementById("addNewItemSerial").value;
                                                const status = document.getElementById("addNewItemStatus").value;
                                                const location =
                                                    document.getElementById("addNewItemLocation").value;
                                                console.log(serial, status, location);
                                                if (serial === "" || status === "" || location === "") {
                                                    alert("Please fill all the fields");
                                                    return;
                                                }
                                                serial = serial.trim().toUpperCase();
                                                // if (itemsSerialNumber.includes(serial)) {
                                                //     alert("Serial number already exists");
                                                //     return;
                                                // }
                                                // itemsSerialNumber.push(serial);
                                                numberOfNewItems++;
                                                document.getElementById("numberOfNewItems").value = numberOfNewItems;
                                                const newItemTableBody =
                                                    document.getElementById("newItemTableBody");
                                                let html = "<tr id='row[" + numberOfNewItems + "]'>";
                                                html +=
                                                    "<td><input type='text' id='serialNumber[" +
                                                    numberOfNewItems +
                                                    "]' name='serialNumber[" +
                                                    numberOfNewItems +
                                                    "]' class='' value='" +
                                                    serial +
                                                    "' /></td>";

                                                html +=
                                                    "<td><select id='status[" +
                                                    numberOfNewItems +
                                                    "]' name='status[" +
                                                    numberOfNewItems +
                                                    "]'>";
                                                html += <options:itemStatusOptions hasValue="true" />
                                                html += "</select></td>";

                                                html +=
                                                    "<td><select id='location[" +
                                                    numberOfNewItems +
                                                    "]' name='locationId[" +
                                                    numberOfNewItems +
                                                    "]'>";
                                                html += <options:locationOptions hasValue="true" />
                                                html += "</select></td>";

                                                html +=
                                                    "<td><button class='btn btn-primary' onclick=removeRow(" +
                                                    numberOfNewItems +
                                                    ")>Remove</button></td>";

                                                html += "</tr>";

                                                newItemTableBody.innerHTML += html;
                                            }

                                            function goBackToNormalMode() {
                                                window.location.href = "${pageContext.request.contextPath}/equipment/<%= equipment.getId()%>";
                                            }

                                            function onUpdateSubmit(event) {
                                                event.preventDefault();
                                                const mainForm = document.getElementById("mainForm");
                                                const dataForm = document.getElementById("dataForm");
                                                const mainForm_data = new FormData(mainForm);
                                                const dataForm_data = new FormData(dataForm);

                                                const data = new FormData();
                                                for (const pair of mainForm_data.entries()) {
                                                    data.append(pair[0], pair[1]);
                                                }

                                                for (const pair of dataForm_data.entries()) {
                                                    data.append(pair[0], pair[1]);
                                                }

                                                const hiddenForm = document.getElementById("fakeForm");

                                                for (const pair of data.entries()) {
                                                    const input = document.createElement("input");
                                                    input.type = "hidden";
                                                    input.name = pair[0];
                                                    input.value = pair[1];
                                                    hiddenForm.appendChild(input);
                                                }

                                                for (const pair of data.entries()) {
                                                    console.log(pair[0] + ", " + pair[1]);
                                                }

                                                hiddenForm.submit();

                                                // const serializeFormToUrlEncoded = (form) => {
                                                //     const formData = new FormData(form);
                                                //     return new URLSearchParams(formData).toString();
                                                // };
                                                //
                                                // const mainFormData = serializeFormToUrlEncoded(mainForm);
                                                // const dataFormData = serializeFormToUrlEncoded(dataForm);
                                                //
                                                // const combinedFormData = mainFormData + "&" + dataFormData;
                                                //
                                                // fetch("", {
                                                //     method: "POST",
                                                //     body: combinedFormData,
                                                //     headers: {
                                                //         "Content-Type": "application/x-www-form-urlencoded",
                                                //     }
                                                // });
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
                                                    window.location.href = '${pageContext.request.contextPath}/equipment/' + id + '/edit';
                                                    console.log(data);
                                                }).catch((error) => {
                                                    console.error('Error:', error);
                                                    alert(error.message);
                                                });
                                            }
                                        </script>

                                        <body class="bg-light">
                                            <%@include file="../Components/Nav.jsp" %>
                                                <form style="display: none"
                                                    action="${pageContext.request.contextPath}/equipment/<%= equipment.getId()%>"
                                                    method="post" id="fakeForm"></form>
                                                <div class="col pb-2 px-3">
                                                    <div
                                                        class="d-flex justify-content-between align-items-center p-3 pb-2 mb-3 border-bottom">
                                                        <div class="col">
                                                            <h2 class="h2 col">Equipment Detail</h2>
                                                        </div>
                                                        <div class="btn-toolbar mb-2 mb-md-0">
                                                            <button type="button" class="btn btn-danger mx-2"
                                                                data-bs-toggle="modal" data-bs-target="#RemoveModal">
                                                                Remove
                                                            </button>
                                                            <button class="btn btn-primary"
                                                                onclick="goBackToNormalMode()">
                                                                Cancel
                                                            </button>
                                                        </div>
                                                    </div>
                                                    <form id="mainForm" onsubmit="return onUpdateSubmit(event)">
                                                        <input type="hidden" name="numberOfNewItems" value="0"
                                                            id="numberOfNewItems">
                                                        <div class="row gx-6">
                                                            <div class="col-lg-6">
                                                                <div class="d-flex justify-content-center">
                                                                    <img src="${pageContext.request.contextPath}/<%= equipment.getImagePath()%>"
                                                                        class="img-thumbnail object-fit-cover" alt="" />
                                                                </div>
                                                            </div>
                                                            <div class="col-lg-6">
                                                                <% if (request.getAttribute("error") !=null) { %>
                                                                    <div class="alert alert-danger" role="alert">
                                                                        <%= request.getAttribute("error")%>
                                                                    </div>
                                                                    <% } %>
                                                                        <% if (errorItems !=null) { %>
                                                                            <div class="alert alert-danger"
                                                                                role="alert">
                                                                                <h4 class="alert-heading">Failed to add
                                                                                    new items</h4>
                                                                                <ul>
                                                                                    <% for (CreateEquipmentItem
                                                                                        errorItem : errorItems) { %>
                                                                                        <li>
                                                                                            <%= errorItem.getSerialNumber()
                                                                                                + " : " +
                                                                                                errorItem.getErrorMessages()%>
                                                                                        </li>
                                                                                        <% } %>
                                                                                </ul>
                                                                            </div>
                                                                            <% } %>
                                                                                <div class="row">
                                                                                    <div class="h4 col-lg-6">ID :</div>
                                                                                    <div class="col-lg-6">
                                                                                        <%= equipment.getId()%>
                                                                                    </div>
                                                                                </div>
                                                                                <label for="itemName"
                                                                                    class="h4 pt-2">Name :</label>
                                                                                <input type="text" class="form-control"
                                                                                    value="<%= equipment.getName()%>"
                                                                                    name="itemName" id="itemName" />
                                                                                <div class="h4 pt-2">Options</div>
                                                                                <div class="row">
                                                                                    <div class="col">
                                                                                        <div class="row">
                                                                                            <label for="isStaffOnly"
                                                                                                class="col">Staff only
                                                                                                mode</label>
                                                                                            <input type="checkbox"
                                                                                                name="isStaffOnly"
                                                                                                id="isStaffOnly"
                                                                                                class="col"
                                                                                                <%=equipment.isStaffOnly()
                                                                                                ? "checked" : "" %>
                                                                                            />
                                                                                        </div>
                                                                                    </div>
                                                                                    <div class="col">
                                                                                        <div class="row">
                                                                                            <label for="isListed"
                                                                                                class="col">Listed</label>
                                                                                            <input type="checkbox"
                                                                                                name="isListed"
                                                                                                id="isListed"
                                                                                                class="col"
                                                                                                <%=equipment.isListed()
                                                                                                ? "checked" : "" %>
                                                                                            />
                                                                                        </div>
                                                                                    </div>
                                                                                </div>
                                                                                <label for="itemDescription"
                                                                                    class="h4 pt-2">Description</label>
                                                                                <textarea name="itemDescription"
                                                                                    id="itemDescription" cols="30"
                                                                                    rows="10"
                                                                                    class="form-control"><%= equipment.getDescription()%></textarea>

                                                                                <hr />
                                                                                <div
                                                                                    class="row mb-4 justify-content-center">
                                                                                    <button class="btn btn-primary">
                                                                                        Update Details
                                                                                    </button>
                                                                                </div>
                                                            </div>
                                                        </div>
                                                    </form>
                                                    <div class="p-3">
                                                        <h2 class="h2">Add New Items</h2>
                                                    </div>
                                                    <div class="container-fluid">
                                                        <form method="post" onsubmit="return onAddnewItem(event)">
                                                            <div class="row justify-content-center">
                                                                <div class="col">
                                                                    <label for="addNewItemSerial">Serial Number</label>
                                                                    <input type="text" name="addNewItemSerial"
                                                                        id="addNewItemSerial" class="" />
                                                                </div>
                                                                <div class="col">
                                                                    <label for="addNewItemStatus">Status</label>
                                                                    <select name="addNewItemStatus"
                                                                        id="addNewItemStatus" required>
                                                                        <options:itemStatusOptions hasValue="false" />
                                                                    </select>
                                                                </div>
                                                                <div class="col">
                                                                    <label for="addNewItemLocation">Location</label>
                                                                    <select name="addNewItemLocation"
                                                                        id="addNewItemLocation" required>
                                                                        <options:locationOptions hasValue="false" />
                                                                    </select>
                                                                </div>
                                                                <div class="col-2">
                                                                    <button class="btn btn-primary">Add</button>
                                                                </div>
                                                            </div>
                                                        </form>
                                                    </div>
                                                    <div class="p-3">
                                                        <form id="dataForm">
                                                            <table class="table">
                                                                <thead>
                                                                    <tr>
                                                                        <th>serial Number</th>
                                                                        <th>Status</th>
                                                                        <th>Location</th>
                                                                        <th></th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody id="newItemTableBody"></tbody>
                                                            </table>
                                                        </form>
                                                    </div>
                                                    <hr />

                                                    <div class="p-3">
                                                        <h2 class="h2">Current Items</h2>
                                                    </div>
                                                    <form class="d-flex p-3" role="search"
                                                        onsubmit="return onSearchSubmit(event)">
                                                        <input class="form-control me-2" type="search"
                                                            placeholder="Search" aria-label="Search" id="searchText" />
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
                                                </div>

                                                <div class="modal fade" id="RemoveModal" tabindex="-1"
                                                    aria-labelledby="RemoveModalLabel" aria-hidden="true">
                                                    <div class="modal-dialog">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h1 class="modal-title fs-5" id="RemoveModalLabel">
                                                                    Remove Equipment</h1>
                                                                <button type="button" class="btn-close"
                                                                    data-bs-dismiss="modal" aria-label="Close"></button>
                                                            </div>
                                                            <div class="modal-body">
                                                                Are you sure to Remove the Equipment?
                                                                Make sure there is no item left in the equipment
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-secondary"
                                                                    data-bs-dismiss="modal">Close</button>
                                                                <button type="button" class="btn btn-danger"
                                                                    onclick="removeEquipment(<%= equipment.getId()%>)">Confirm</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="modal fade" id="RemoveItemModal" tabindex="-1"
                                                    aria-labelledby="RemoveItemModalLabel" aria-hidden="true">
                                                    <div class="modal-dialog">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h1 class="modal-title fs-5" id="RemoveItemModalLabel">
                                                                </h1>
                                                                <button type="button" class="btn-close"
                                                                    data-bs-dismiss="modal" aria-label="Close"></button>
                                                            </div>
                                                            <div class="modal-body" id="modal-body">
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-secondary"
                                                                    data-bs-dismiss="modal">Close</button>
                                                                <button type="button" class="btn btn-danger"
                                                                    id="RemoveItemModalConfirmBtn">Confirm</button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                        </body>

                            </html>