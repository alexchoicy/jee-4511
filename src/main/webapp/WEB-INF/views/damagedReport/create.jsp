<%@ page import="com.cems.Model.Display.DamagesReportDisplay" %>
<%@ page import="com.cems.Model.User" %>
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
<%
    DamagesReportDisplay detail = (DamagesReportDisplay) request.getAttribute("damageData");
    User user = (User) request.getSession().getAttribute("user");
%>
<body class="bg-light">
<jsp:include page="../Components/Nav.jsp"/>
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
                                <%= String.format("%s %s (%s)", user.getFirstName(), user.getLastName(), user.getUserId())%>
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
                    </div>
                </div>
                <div class="row p-3">
                    <div class="border"></div>
                </div>
                <form action="${pageContext.request.contextPath}/damages/create" method="post">
                    <input  type="hidden" name="itemID" value="<%= detail.getItemID()%>">
                    <input  type="hidden" name="recordID" value="<%= detail.getRecordID()%>">
                    <div class="m-5">
                        <div class="form-floating">
                                <textarea
                                        class="form-control"
                                        placeholder="Leave a comment here"
                                        id="floatingTextarea2"
                                        name="damageDetail"
                                        style="height: 100px"
                                ></textarea>
                            <label for="floatingTextarea2"
                            >Damages Details</label
                            >
                        </div>
                    </div>
                    <div class="row">
                        <div class="col d-flex justify-content-end">
                            <button
                                    type="submit"
                                    class="btn btn-primary mx-3 mb-2"
                            >
                                Submit
                            </button>
                        </div>
                    </div>
                </form>

            </form>
        </div>
    </div>
</div>
</body>
</html>
