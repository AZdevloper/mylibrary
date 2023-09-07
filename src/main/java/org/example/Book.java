package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Book {
                private int Id;
                private String Isbn;
                private String Isbn2;
                private String AuthorName;
                private String Title;
                private String Status;
                private int Quantity;
                private int LostedQuantity;
                private int BorrowedQuantity;
                Scanner sc = new Scanner(System.in);
    public void getAllBooks() throws SQLException {
        try (Connection con = DbConnection.getConnection();) { // try-with-resources => is using her to automatically closing the database connection;

            Statement st = con.createStatement();
            //String qr = "SELECT * FROM books INNER JOIN author ON books.authorId = author.id";
            String qr = "SELECT * FROM books";
            ResultSet res = st.executeQuery(qr);
            while (res.next()) {
                System.out.println(res.getString("title") + res.getString("authorName") + res.getInt("id"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void add() throws SQLException{


        System.out.println("Enter book information  :");
        System.out.println("[book title] : ");
        this.Title = sc.nextLine();
        System.out.println("[book author] : ");
        this.AuthorName = sc.nextLine();

        System.out.println("[book isbn] : ");
        this.Isbn = sc.nextLine();

        System.out.println("[book status il'est \"disponible\" par defaut] : ");
        this.Status = sc.nextLine();

        System.out.println("[book Quantity] : ");
        this.Quantity = sc.nextInt();

        System.out.println("[book lostedQuantity] : ");
        this.LostedQuantity = sc.nextInt();

        System.out.println("[book BorrowedQuantity] : ");
        this.BorrowedQuantity = sc.nextInt();

        String query = "INSERT INTO books (isbn,authorName, title,status, quantity, lostedQuantity, borrowedQuantity) VALUES (?, ?, ?,?, ?, ?, ?)";
        try(Connection con  = DbConnection.getConnection();PreparedStatement ps = con.prepareStatement(query);){
            ps.setString(1,this.Isbn);
            ps.setString(2,this.Title);
            ps.setString(3,this.AuthorName);
            ps.setString(4,this.Status);
            ps.setInt(5,this.Quantity);
            ps.setInt(6,this.LostedQuantity);
            ps.setInt(7,this.BorrowedQuantity);
            ps.executeUpdate();

        }catch(SQLException e ){
            System.out.println(e.getMessage());

        }

    }
    public boolean removeBook(String isbn){return true;}

    public void editeBook() throws SQLException{
        System.out.println("entrer isbn de livre :");
        this.Isbn = sc.nextLine();
        String query = "select * from books where isbn=?";
        try(Connection con  = DbConnection.getConnection();  PreparedStatement ps = con.prepareStatement(query);) {
            ps.setString(1,this.Isbn);
           try(ResultSet rs = ps.executeQuery()) {

               if (rs.next()){
                   System.out.println(rs.getString("title"));
                   System.out.println(rs.getInt("isbn"));
                   System.out.println(rs.getString("authorName"));
                   System.out.println(rs.getString("status"));
                   System.out.println(rs.getInt("quantity"));
                   System.out.println(rs.getInt("lostedQuantity"));
                   System.out.println(rs.getInt("borrowedQuantity"));
               }else{
                   System.out.println("pas de livre avec ce isbn");
               }
           }catch(SQLException e ){
               System.out.println(e.getMessage());
           }

        }catch(SQLException e){
                System.out.println(e.getMessage());
            }

        System.out.println("Enter book information  :");
        System.out.println("[book title] : ");
        this.Title = sc.nextLine();
        System.out.println("[book author] : ");
        this.AuthorName = sc.nextLine();



        System.out.println("[book status il'est \"disponible\" par defaut] : ");
        this.Status = sc.nextLine();

        System.out.println("[book Quantity] : ");
        this.Quantity = sc.nextInt();

        System.out.println("[book lostedQuantity] : ");
        this.LostedQuantity = sc.nextInt();

        System.out.println("[book BorrowedQuantity] : ");
        this.BorrowedQuantity = sc.nextInt();
        updateBook(this.AuthorName,this.Title,this.Status,this.Quantity,this.LostedQuantity,this.BorrowedQuantity);
        }

    public void updateBook( String authorName,String title,String status, int quantity,int lostedQuantity,int borrowedQuantity ) throws SQLException {

        String query = "UPDATE books SET authorName = ?, title = ?, status = ?, quantity = ?, lostedQuantity = ?, borrowedQuantity = ? WHERE isbn = ?";
        try(Connection con  = DbConnection.getConnection();PreparedStatement ps = con.prepareStatement(query);){
            ps.setString(1, authorName);
            ps.setString(2, title);
            ps.setString(3, status);
            ps.setInt(4, quantity);
            ps.setInt(5, lostedQuantity);
            ps.setInt(6, borrowedQuantity);
            ps.setString(7, this.Isbn);
            ps.executeUpdate();

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Book with ISBN " + this.Isbn + " updated successfully.");
            } else {
                System.out.println("No book found with ISBN " + this.Isbn + ". No updates performed.");
            }

        }catch(SQLException e ){
            System.out.println(e.getMessage());

        }


    }

    public void deleteBook() {
        System.out.println("entrer isbn de livre :");
        this.Isbn = sc.nextLine();
        String query = "DELETE FROM books WHERE isbn = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, Isbn);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Book with ISBN " + Isbn + " deleted successfully.");
            } else {
                System.out.println("No book found with ISBN " + Isbn + ". No deletions performed.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public Book searchBook(String isbn){return new Book();}

}
