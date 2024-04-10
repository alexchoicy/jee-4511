<%@ page import="com.cems.Model.Users" %>
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
<script>
    let response_data;
    const fetchData = async (searchText = "") => {
        const url = '${pageContext.request.contextPath}/equipment?searchText=' + searchText;
        const response = await fetch(url);
        const data = await response.json();
        console.log(data);
        response_data = data;
        setBody(data);
    }

    function onSearchSubmit(e) {
        e.preventDefault();
        const searchText = document.getElementById("searchText").value;
        console.log(searchText);
        fetchData(searchText);
    }

    const createCheckbox = (value) => {
        const checkbox = document.createElement("input");
        checkbox.type = "checkbox";
        if (value) {
            checkbox.setAttribute("checked", "");
        }
        checkbox.disabled = true;
        return checkbox.outerHTML;
    }
    const setBody = (data) => {
        const tbody = document.getElementById("body");
        tbody.innerHTML = "";
        data.data.forEach((item) => {
            const {equipment_id, equipment_name, description, isStaffOnly, isWishlisted, availableQuantity} = item;
            const tr = document.createElement("tr");
            tr.classList.add("text-center")
            let row = "";
            row += "<td>" + createCheckbox(isStaffOnly) + "</td>";
            row += "<td>" + createCheckbox(isWishlisted) + "</td>";
            row += "<td>" + equipment_id + "</td>";
            row += "<td>" + equipment_name + "</td>";
            row += "<td>" + availableQuantity + "</td>";
            row += "<td><a href=${pageContext.request.contextPath}/equipment/" + equipment_id + ">Detail</a></td>";
            tr.innerHTML = row;
            tbody.appendChild(tr);
        });
    }

    document.addEventListener("DOMContentLoaded", () => {
        fetchData();
    });


    function removeEquipment(id) {
        const url = '${pageContext.request.contextPath}/equipment/' + id;
        fetch(url, {
            method: 'DELETE',
        }).then(response => {
            return response.text();
        }).then(data => {
            console.log(data);
        });
    }

    function removeItem(id, itemID) {
        const url = '${pageContext.request.contextPath}/equipment/' + id + '/items/' + itemID;
        fetch(url, {
            method: 'DELETE',
        }).then(response => {
            return response.text();
        }).then(data => {
            console.log(data);
        });
    }

</script>
<body>
<%@include file="Components/Nav.jsp" %>


<div class="d-flex justify-content-end">
    <form
            class="d-flex"
            role="search"
            style="margin-right: 20px; margin-top: 10px"
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
</div>
<div class="mx-2">
    <table class="table table-striped table-hover text-center" style="table-layout: fixed; width: 100%">
        <thead>
        <th scope="col">Staff Only</th>
        <th scope="col">Wish Listed</th>
        <th scope="col">ID</th>
        <th scope="col">Item Name</th>
        <th scope="col">Available Quantity</th>
        <th scope="col">Detail</th>
        </thead>
        <tbody id="body">
        </tbody>
    </table>
</div>

<%
    Users user = (Users) session.getAttribute("user");
    if (user != null) {
%>
<h2>User Details</h2>
<p>User ID: <%= user.getUserId() %>
</p>
<p>Username: <%= user.getUsername() %>
</p>
<p>Phone Number: <%= user.getPhoneNumber() %>
</p>
<p>First Name: <%= user.getFirstName() %>
</p>
<p>Last Name: <%= user.getLastName() %>
</p>
<p>Role: <%= user.getRole() %>
</p>
<%
    }
%>

<button class="btn btn-danger mx-2" onclick="removeEquipment(2)">Remove</button>

<button class="btn btn-danger mx-2" onclick="removeItem(1,1)">Remove</button>
</body>
</html>
