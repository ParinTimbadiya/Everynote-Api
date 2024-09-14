package com.everynote.api;

import com.everynote.api.helpers.JsonResponse;
import com.everynote.api.helpers.Utils;
import com.everynote.api.models.Note;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DeleteNoteFile", urlPatterns = {"/DeleteNoteFile"})
public class DeleteNoteFileServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        
        
        try {
            String json = Utils.getBody(request);
            Note note = new Gson().fromJson(json, Note.class);
            
            PrintWriter out = response.getWriter();
            String Body = note.getBodyPath();
            
            if(Body == null)
            {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(new JsonResponse("Incomplate data."));
                return;
            }
        
//
//        Part filePart = request.getPart("file");
//        String fileName = filePart.getSubmittedFileName();
        File file = new File("D:\\Everynote\\upload\\" + Body);

        if (file.delete()) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.print(new JsonResponse("File deleted successfully"));
                
        } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(new JsonResponse("Failed to delete the file"));
        }

        }
        catch(Exception ex){
            
        }
    }

}
