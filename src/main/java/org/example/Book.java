package org.example;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Book {
                private int id;
                private String isbn;
                private Author author;
                private String title;
                private Boolean status;
                private int quantity;
                private Boolean lostedQuantity;
                private Boolean borrowedQuantity;
    public List<Book> getAllBooks() throws SQLException {
        try (Connection con = DbConnection.getConnection();) { // try-with-resources => is using her to automatically closing the database connection;

            Statement st = con.createStatement();
            String qr = "SELECT * FROM books INNER JOIN author ON books.authorId = author.id";
            ResultSet res = st.executeQuery(qr);
            while (res.next()) {
                System.out.println(res.getString("title") + res.getString("name") + res.getInt("id"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Book addBook(int id, String isbn, Author authorId,String title,Boolean status, int quantity,Boolean lostedQuantity,Boolean borrowedQuantity  ) throws SQLException{
        try(Connection con  = DbConnection.getConnection();){

        }

        return new Book();}
    public boolean removeBook(String isbn){return true;}
    public Book editeBook(String isbn){return new Book();}
    public Book updateBook(int id, String isbn, Author authorId,String title,Boolean status, int quantity,Boolean lostedQuantity,Boolean borrowedQuantity ){return new Book();}
    public Book searchBook(String isbn){return new Book();}

}
