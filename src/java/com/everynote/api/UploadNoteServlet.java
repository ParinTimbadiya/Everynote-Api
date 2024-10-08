package com.everynote.api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(name = "UploadNote", urlPatterns = {"/UploadNote"})

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)

public class UploadNoteServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try
        {
            /* Receive file uploaded to the Servlet from the HTML5 form */
            Part filePart = request.getPart("file");
            String fileName = filePart.getSubmittedFileName();

            for (Part part : request.getParts()) {
                part.write("D:\\Everynote\\upload\\" + fileName);
            }

            response.getWriter().print("The file uploaded sucessfully.");
            response.setStatus(HttpServletResponse.SC_CREATED);
        }
        catch (IOException | ServletException ex)
        {
            ex.printStackTrace();
        }
    }
}
