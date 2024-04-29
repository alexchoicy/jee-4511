package com.cems.Servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@WebServlet(name = "resourcesServlet", value = "/resources/*")
public class ResourcesServlet extends HttpServlet {
    public void init() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getPathInfo();
        if (path == null || path.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String[] pathParts = path.split("/");
        // for (String part : pathParts) {
        // System.out.println(part);
        // }
        String type = pathParts[1];

        if (type.equals("images")) {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path)) {
                if (inputStream == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                if (path.endsWith(".png")) {
                    response.setContentType("image/png");
                } else if (path.endsWith(".jpg")) {
                    response.setContentType("image/jpg");
                } else if (path.endsWith(".jpeg")) {
                    response.setContentType("image/jpeg");
                } else if (path.endsWith(".gif")) {
                    response.setContentType("image/gif");
                } else if (path.endsWith(".svg")) {
                    response.setContentType("image/svg+xml");
                } else if (path.endsWith(".webp")) {
                    response.setContentType("image/webp");
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    response.getOutputStream().write(buffer, 0, bytesRead);
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
