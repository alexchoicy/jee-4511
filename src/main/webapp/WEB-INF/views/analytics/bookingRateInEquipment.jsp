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
    function search(event) {
      event.preventDefault();
      let start_date = document.getElementById('start_date').value;
      let end_date = document.getElementById('end_date').value;
      let id = document.getElementById('id').value;
      console.log(start_date, end_date, id);
      const url = '${pageContext.request.contextPath}/analytics/viewEquipment?id=' + id + '&start_date=' + start_date + '&end_date=' + end_date;
      console.log(url);
    }
  </script>
</head>
<%@taglib prefix="options" uri="/WEB-INF/tlds/cems_equipment_tags.tld" %>
<body class="bg-light">
<jsp:include page="../Components/Nav.jsp"/>
  <div>
    <h1> booking rate of selected equipment</h1>
    <form action="" onsubmit="return search(event)">
      <label for="start_date"> Start Date</label>
      <input type="date" id="start_date" name="start_date" required> Start Date</input>
      <label for="end_date"> End Date</label>
      <input type="date" id="end_date" name="end_date" required/>
      <label for="id">Select a Equipment</label>
      <select name="id" id="id" required>
        <options:ItemsOptions/>
      </select>



      <button class="btn btn-primary">Search</button>



    </form>
  </div>
</body>
</html>
