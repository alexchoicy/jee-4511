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
    <% if (request.getAttribute("error") !=null) { %>
      <div class="alert alert-danger" role="alert">
        <%= request.getAttribute("error")%>
      </div>
      <% } %>
  </body>

  </html>