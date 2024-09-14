package com.everynote.api.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    private static Database database = null;
    private static Connection connection = null;

    public static Database getInstance() throws SQLException, ClassNotFoundException {
        if (database == null) {
            Class.forName("com.mysql.jdbc.Driver");
            
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/everynote_db", "root", "");
            database = new Database();
        }

        return database;
    }

    public ResultSet select(String query, Object... params) throws SQLException {
       
        PreparedStatement statement = prepareQuery(query, params);
        ResultSet result = statement.executeQuery();
        
        return result;
    }
    
    public ResultSet selectAll(String query) throws SQLException {
       
        PreparedStatement statement = prepareQuery(query);
        ResultSet result = statement.executeQuery();
        
        return result;
    }
    
    public int execute(String query, Object... params) throws SQLException {
        int rowsAffected;
        try (PreparedStatement statement = prepareQuery(query, params)) {
            rowsAffected = statement.executeUpdate();
        }
        
        return rowsAffected;
    }
    
    private PreparedStatement prepareQuery(String query, Object... params) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++)
            statement.setObject(i + 1, params[i]);
        
        return statement;
    }
}
