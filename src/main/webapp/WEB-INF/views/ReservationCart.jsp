<%@ page import="java.util.ArrayList" %>
<%@ page import="com.cems.Model.Request.ReservationCart" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<!DOCTYPE html>
<html>

<head>
    <title>Reservation Cart</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <script>
        function removeItemFromCart(url) {
            fetch(url, {
                method: "DELETE",

            }).then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(text);
                    });
                }
                return response.text();
            }).then(data => {
                console.log(data);
                alert("Removed");
                location.reload();
            }).catch((error) => {
                console.error('Error:', error);
                alert(error.message);
            });
        }
    </script>
</head>
<% ArrayList<ReservationCart> cart = (ArrayList<ReservationCart>) request.getAttribute("cartItems"); %>

<body class="bg-light">
<%@include file="Components/Nav.jsp" %>
<div class="p-3">
    <table class="table text-center">
        <thead>
        <tr>
            <th scope="col">
                Item ID
            </th>
            <th scope="col">
                Item Name
            </th>
            <th scope="col">
                Request Quantity
            </th>
            <th scope="col">
                Remove
            </th>
        </tr>
        </thead>
        <tbody>
        <% if (cart != null) {
            for (ReservationCart cartItem : cart) {%>
        <tr>
            <td>
                <%= cartItem.getEquipmentID()%>
            </td>
            <td>
                <%= cartItem.getEquipmentName()%>
            </td>
            <td>
                <%= cartItem.getQuantity()%>
            </td>
            <td>
                <button
                        onclick="removeItemFromCart('${pageContext.request.contextPath}/equipment/<%= cartItem.getEquipmentID()%>/cart')"
                        class="btn btn-danger">
                    Remove
                </button>
            </td>
        </tr>
        <% }
        }%>
        </tbody>
    </table>

    <div class="d-flex justify-content-end">
        <button class="btn btn-primary"
                onclick="window.location = '${pageContext.request.contextPath}/reservation/create'">
            Create Reservation
        </button>
    </div>

</div>
</body>

</html>