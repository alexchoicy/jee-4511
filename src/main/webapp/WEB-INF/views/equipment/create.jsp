<%@ taglib prefix="options" uri="/WEB-INF/tlds/cems_equipment_tags.tld" %>
<%@ page import="com.cems.Model.Request.CreateEquipmentItem" %>
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>

    <script>
        let numberOfNewItems = 0;
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
            html += <options:itemStatusOptions hasValue="true"/>
                html += "</select></td>";

            html +=
                "<td><select id='location[" +
                numberOfNewItems +
                "]' name='locationId[" +
                numberOfNewItems +
                "]'>";
            html += <options:locationOptions hasValue="true"/>
                html += "</select></td>";

            html +=
                "<td><button class='btn btn-primary' onclick=removeRow(" +
                numberOfNewItems +
                ")>Remove</button></td>";

            html += "</tr>";

            newItemTableBody.innerHTML += html;
        }

        function onCreateSubmit(event) {
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
        }
    </script>
</head>
<%
    ArrayList<CreateEquipmentItem> errorItems = (ArrayList<CreateEquipmentItem>) request.getAttribute("errorItems");
%>
<body>
<%@include file="../Components/Nav.jsp" %>
<form style="display: none" action="${pageContext.request.contextPath}/equipment/create" method="post"
      id="fakeForm"></form>
<div class="col pb-2 px-3">
    <div
            class="d-flex justify-content-between align-items-center p-3 pb-2 mb-3 border-bottom"
    >
        <div class="col">
            <h2 class="h2 col">Equipment Detail</h2>
        </div>
    </div>
    <form id="mainForm" onsubmit="return onCreateSubmit(event)">
        <input type="hidden" name="numberOfNewItems" value="0" id="numberOfNewItems">
        <div class="row gx-6">
            <div class="col-lg-6">
                <div class="d-flex justify-content-center">
                    <img
                            src="${pageContext.request.contextPath}/resources/images/equipments/awaitUpload.png"
                            class="img-thumbnail object-fit-cover"
                            alt=""
                    />
                </div>
            </div>
            <div class="col-lg-6">
                <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-danger" role="alert">
                    <%= request.getAttribute("error")%>
                </div>
                <% } %>
                <% if (errorItems != null) { %>
                <div class="alert alert-danger" role="alert">
                    <h4 class="alert-heading">Failed to add new items</h4>
                    <ul>
                        <% for (CreateEquipmentItem errorItem : errorItems) { %>
                        <li><%= errorItem.getSerialNumber() + " : " + errorItem.getErrorMessages()%>
                        </li>
                        <% } %>
                    </ul>
                </div>
                <% } %>
                <label for="itemName" class="h4 pt-2">Name :</label>
                <input
                        type="text"
                        class="form-control"
                        name="itemName"
                        id="itemName"
                />
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
                            />
                        </div>
                    </div>
                </div>
                <label for="itemDescription" class="h4 pt-2">Description</label>
                <textarea
                        name="itemDescription"
                        id="itemDescription"
                        cols="30"
                        rows="10"
                        class="form-control"
                ></textarea>
                <hr/>
                <div class="row mb-4 justify-content-center">
                    <button class="btn btn-primary">
                        Create Records
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
                    <input
                            type="text"
                            name="addNewItemSerial"
                            id="addNewItemSerial"
                            class=""
                    />
                </div>
                <div class="col">
                    <label for="addNewItemStatus">Status</label>
                    <select name="addNewItemStatus" id="addNewItemStatus" required>
                        <options:itemStatusOptions hasValue="false"/>
                    </select>
                </div>
                <div class="col">
                    <label for="addNewItemLocation">Location</label>
                    <select
                            name="addNewItemLocation"
                            id="addNewItemLocation"
                            required
                    >
                        <options:locationOptions hasValue="false"/>
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
    <hr/>
</div>



</body>
</html>
