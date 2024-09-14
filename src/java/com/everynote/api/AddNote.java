package com.everynote.api;

import com.everynote.api.helpers.Database;
import com.everynote.api.helpers.JsonResponse;
import com.everynote.api.helpers.Utils;
import com.everynote.api.models.Note;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AddNote", urlPatterns = {"/AddNote"})
public class AddNote extends HttpServlet {
    
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            String json = Utils.getBody(request);
            Note note = new Gson().fromJson(json, Note.class);
            
            PrintWriter out = response.getWriter();

            String NoteNumber = note.getNoteNumber();
            String Title = note.getTitle();
            String BodyPath = note.getBodyPath();
            String LastModified = note.getLastModified();
            String UserId = note.getUserId();
            

            if(NoteNumber == null || Title == null || BodyPath == null || UserId == null)
            {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(new JsonResponse("Incomplate data."));
                return;
            }
            
            Database database = Database.getInstance();
  
            int rs = database.execute("INSERT INTO `Notes` (`NoteNumber`, `Title`, `BodyPath`, `UserId`, `LastModified`) VALUES (?,?,?,?,?)", new Object[]{NoteNumber,Title,BodyPath, UserId, LastModified});
            if (rs == 0) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(new JsonResponse("Could not add note."));
                
                return;
            }
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            
        } catch (JsonSyntaxException | IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AuthServlet.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
}