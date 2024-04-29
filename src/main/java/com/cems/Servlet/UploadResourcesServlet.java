package com.cems.Servlet;

import com.cems.Utils.ParseUtil;
import com.cems.database.EquipmentManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.UUID;

@WebServlet(name = "UploadResourcesServlet", value = "/uploadResources")
@MultipartConfig
public class UploadResourcesServlet extends HttpServlet {
    private EquipmentManager equipmentManager;

    @Override
    public void init() throws ServletException {
        equipmentManager = new EquipmentManager();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part filePart = req.getPart("file");
        int equipmentId = ParseUtil.tryParseInt(req.getParameter("id"), 0);
        String filename = filePart.getSubmittedFileName();
        String extension = filename.substring(filename.lastIndexOf("."));
        String path = getClass().getClassLoader().getResource("images/equipments").getPath();

        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String newName = UUID.randomUUID().toString();
        String savePath = path + File.separator + newName + extension;
        try (InputStream inputStream = filePart.getInputStream()) {
            FileOutputStream outputStream = new FileOutputStream(savePath);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        equipmentManager.saveImage(equipmentId, "resources/images/equipments/" + newName + extension);
        resp.sendRedirect(req.getContextPath() + "/equipment/" + equipmentId);
    }

    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 2, token.length() - 1);
            }
        }
        return "";
    }
}
