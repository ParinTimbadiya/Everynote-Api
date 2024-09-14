package com.everynote.api;

import com.everynote.api.helpers.Database;
import com.everynote.api.helpers.JsonResponse;
import com.everynote.api.models.Note;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "GetAllNote", urlPatterns = {"/GetAllNote"})
public class GetAllNoteServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            PrintWriter out = response.getWriter();
            Database database = Database.getInstance();
            
            String userId = request.getParameter("userId");
            ResultSet rs = database.select("SELECT * FROM `Notes` WHERE `UserId` = " + userId);
            
            ArrayList<Note> notes = new ArrayList<>();
            while (rs.next()) {
                Note note = new Note();
                note.setNoteNumber(rs.getString("NoteNumber"));
                note.setTitle(rs.getString("Title"));
                note.setBodyPath(rs.getString("BodyPath"));
                note.setLastModified(rs.getString("LastModified"));
                note.setIsDeleted(rs.getInt("IsDeleted"));
                
                notes.add(note);
            }

            response.setStatus(HttpServletResponse.SC_OK);
            String json = new Gson().toJson(notes);
        
            out.print(json);
        } catch (IOException | ClassNotFoundException | SQLException e) {
        }
    }
}