package com.everynote.api;

import com.everynote.api.helpers.Database;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DownloadNoteServlet", urlPatterns = {"/DownloadNote"})
public class DownloadNoteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String userId = request.getParameter("userId");
            String noteNumber = request.getParameter("noteNumber");

            Database database = Database.getInstance();
            ResultSet r = database.select("SELECT * FROM `Notes` WHERE `NoteNumber`  = ? AND `UserId` = ?", new Object[]{noteNumber, userId});

            if (!r.first()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            String fileName = r.getString("BodyPath");
            File file = new File("D:\\Everynote\\upload\\" + fileName);
            
            ServletContext ctx = getServletContext();
            InputStream fis = new FileInputStream(file);

            String mimeType = ctx.getMimeType(file.getAbsolutePath());
            response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            ServletOutputStream os = response.getOutputStream();

            byte[] bufferData = new byte[1024];
            int read = 0;
            while ((read = fis.read(bufferData)) != -1) {
                os.write(bufferData, 0, read);
            }

            os.flush();
            os.close();
            fis.close();

        } catch (Exception ex) {
            Logger.getLogger(AuthServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
