package com.everynote.api;

import com.everynote.api.helpers.Utils;
import com.everynote.api.helpers.Database;
import com.everynote.api.helpers.JsonResponse;
import com.everynote.api.models.User;
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

@WebServlet(name = "Register", urlPatterns = {"/Register"})
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            String json = Utils.getBody(request);
            User user = new Gson().fromJson(json, User.class);
            
            PrintWriter out = response.getWriter();

            String email = user.getEmail();
            String password = user.getPassword();
            
            if(email == null || password == null)
            {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(new JsonResponse("Incomplate data."));
                return;
            }

            Database database = Database.getInstance();
  
            
            ResultSet res = database.select("SELECT * FROM `Users` WHERE `Email` = ? AND `Password` = ?", new Object[]{email, password});
            
            if (res.first()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                out.print(new JsonResponse("Wrong email or password"));
                return;
            }
            int rs = database.execute("INSERT INTO `Users` (`Email`,`Password`) VALUES (?,?)", new Object[]{email, password});
            if (rs == 0) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.print(new JsonResponse("Could not create an account."));
                
                return;
            }
            
            response.setStatus(HttpServletResponse.SC_CREATED);
            
        } catch (Exception ex) {
            Logger.getLogger(AuthServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
