<%@ page import="com.cems.Model.Display.DamagesReportDisplay" %>
<%@ page import="com.cems.Model.User" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.cems.Enums.UserRoles" %>
<%@ page import="com.cems.Enums.DamagedStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html><%
    DamagesReportDisplay detail = (DamagesReportDisplay) request.getAttribute("damageData");
    User user = (User) request.getSession().getAttribute("user");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>
<head>
    <title>Title</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            document.querySelectorAll('.btn').forEach(button => {
                button.addEventListener('click', function () {
                    const action = this.getAttribute('data-action');

                    fetch('${pageContext.request.contextPath}/damages/detail?recordID=' + <%= detail.getRecordID()%>, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: 'action=' + action,
                    })
                        .then(response => {
                            if (response.ok) {
                                location.reload();
                            }
                            return response.text().then(text => {
                                throw new Error(text);
                            });
                        })
                        .catch(error => {
                            console.log(error);
                        });

                });
            });
        });
    </script>

</head>

<body class="bg-light">
<jsp:include page="../Components/Nav.jsp"/>

<% if (user.getRole()
        == UserRoles.ADMIN) { %>
<div class="d-flex justify-content-end m-2">
    <% if (detail.getDamagedStatus() == DamagedStatus.REPORTED) { %>
    <button class="btn btn-success m-2" data-action="<%=DamagedStatus.CONFIRMED.getValue()%>">Confirm</button>
    <button class="btn btn-danger m-2" data-action="<%=DamagedStatus.DISCARDED.getValue()%>">Discard</button>
    <% } else if (detail.getDamagedStatus() == DamagedStatus.CONFIRMED) { %>
    <button class="btn btn-primary m-2" data-action="<%=DamagedStatus.REPAIRED.getValue()%>"> Repaired
    </button>
    <% }
    } %>
</div>


<div class="container-fluid m-5">
    <div class="row">
        <div class="col-11 rounded-1 bg-white my-3 p-1">
            <form
                    method="post"
                    action="${pageContext.request.contextPath}/damages/create"
            >
                <div class="row p-3">
                    <div class="col-lg-8">
                        <div class="row">
                            <div class="col">Logged by</div>
                            <div class="col">
                                <%= detail.getLoggerName() %> (<%= detail.getLoggerID()%>)
                            </div>
                        </div>
                        <div class="row">
                            <div class="col">Related RecordID</div>
                            <div class="col">
                                <%= detail.getRecordID()%>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col">Borrower Name</div>
                            <div class="col">
                                <%= detail.getBorrowerName()%> (<%= detail.getBorrowerID()%>)
                            </div>
                        </div>
                        <div class="row">
                            <div class="col">Related itemID</div>
                            <div class="col">
                                <%= detail.getItemID()%>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col">Item name</div>
                            <div class="col">
                                <%= detail.getItemName()%>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col">Reported Date</div>
                            <div class="col"><%= sdf.format(detail.getLogDatetime()) %>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col">Status</div>
                            <div class="col"><%= detail.getDamagedStatus().getName()%>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row p-3">
                    <div class="border"></div>
                </div>
                <div class="m-5">
                    <div class="form-floating">
                                <textarea
                                        class="form-control"
                                        placeholder="Leave a comment here"
                                        id="floatingTextarea2"
                                        name="damageDetail"
                                        style="height: 100px"
                                        disabled
                                >
                                    <%= detail.getDamageDetail()%>
                                </textarea>
                        <label for="floatingTextarea2"
                        >Damages Detail</label
                        >
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
