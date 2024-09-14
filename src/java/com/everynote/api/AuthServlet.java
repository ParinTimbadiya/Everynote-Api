package com.everynote.api;

import com.everynote.api.helpers.Utils;
import com.everynote.api.helpers.Database;
import com.everynote.api.helpers.JsonResponse;
import com.everynote.api.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "AuthServlet", urlPatterns = {"/auth"})
public class AuthServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            
            String json = Utils.getBody(request);
            User user = new Gson().fromJson(json, User.class);

            PrintWriter out = response.getWriter();
            
            String email = user.getEmail();
            String password = user.getPassword();

            Database database = Database.getInstance();
            ResultSet rs = database.select("SELECT * FROM `Users` WHERE `Email` = ? AND `Password` = ?", new Object[]{email, password});
            
            if (!rs.first()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print(new JsonResponse("Wrong email or password"));
                return;
            }

            String userId = String.valueOf(rs.getString(1));
            out.print("{\"id\":\""+userId+"\"}");
            
        } catch (JsonSyntaxException | IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AuthServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
