package com.everynote.api;

import com.everynote.api.helpers.Database;
import com.everynote.api.helpers.JsonResponse;
import com.everynote.api.helpers.Utils;
import com.everynote.api.models.Note;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "UpdateNote", urlPatterns = {"/UpdateNote"})
public class UpdateNoteServlet extends HttpServlet {
    
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            String json = Utils.getBody(request);
            Note note = new Gson().fromJson(json, Note.class);
            
            PrintWriter out = response.getWriter();
            String NoteNumber = note.getNoteNumber();
            String Title = note.getTitle();
//            String BodyPath = note.getBodyPath();
            String UserId = note.getUserId();
            String LastModified = note.getLastModified();
            
            if(NoteNumber == null || Title == null || UserId == null)
            {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(new JsonResponse("Incomplate data."));
                return;
            }
            Database database = Database.getInstance();
            
//            ResultSet rs = database.select("SELECT * FROM `users` WHERE `Email` = ? AND `Password` = ?", new Object[]{email, password});

            ResultSet r = database.select("SELECT * FROM `Notes` WHERE `NoteNumber`  = ? AND `UserId` = ?", new Object[]{NoteNumber,UserId});
            
            if (!r.first()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print(new JsonResponse("Note are note added in database"));
                return;
            }
            
            String Id = String.valueOf(r.getString(1));
            
            int rs = database.execute("UPDATE `Notes` SET `NoteNumber` = ?, `Title` = ?, `LastModified` = ?, `UserId` = ? WHERE `Id`  = ?", new Object[]{NoteNumber,Title,LastModified,UserId,Id});
            if (rs == 0) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(new JsonResponse("Could not update note."));
                
                return;
            }
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            
        } catch (Exception ex) {
            Logger.getLogger(AuthServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}