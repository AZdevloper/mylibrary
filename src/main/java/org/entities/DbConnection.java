package org.entities;

import java.sql.*;
import java.sql.Connection;

public class DbConnection {
    private static final String  url = "jdbc:mysql://localhost:3306/mylibrary";
    private static final String username = "root";
    private static final String password = "";

  /*  public Connection DatabaseConnection(){



        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the connection
            Connection connection = DriverManager.getConnection(url, username, password);
            if (connection != null) {
                System.out.println("Connected to the database successfully!");
               return connection;
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }*/

    public static Connection  getConnection() throws SQLException {

        Connection con = DriverManager.getConnection(url,username,password);
        return con;
    }


}
