package org.example;

import Utils.ConversionUtils;
import Utils.MenuUtils;
import Utils.MessageUtils;
import Utils.StatusUtils;
import jdk.jshell.execution.Util;

import java.sql.*;
import java.util.Scanner;

public class Book {
    private int Id;
    private int Isbn;
    private String AuthorName;
    private String Title;
    private String Status;
    private int Quantity;
    private int LostedQuantity;
    private int BorrowedQuantity;
    Scanner sc = new Scanner(System.in);

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getAuthorName() {
        return AuthorName;
    }

    public void setAuthorName(String authorName) {
        AuthorName = authorName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getIsbn() {
        return Isbn;
    }

    public void setIsbn(int isbn) {
        Isbn = isbn;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getBorrowedQuantity() {
        return BorrowedQuantity;
    }

    public void setBorrowedQuantity(int borrowedQuantity) {
        BorrowedQuantity = borrowedQuantity;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
    public  Book(int isbn){
        this.Isbn = isbn;

}
    public void getAllBooks() throws SQLException {
        try(Connection con = DbConnection.getConnection();) { // try-with-resources => is using her to automatically closing the database connection;

            Statement st = con.createStatement();
            //String qr = "SELECT * FROM books INNER JOIN author ON books.authorId = author.id";
            String qr = "SELECT * FROM books";
            ResultSet res = st.executeQuery(qr);
            while (res.next()) {
                System.out.println("_________________________________");
                System.out.println("title : " +res.getString("title") +"  |nom d'author : "+ res.getString("authorName") + "  |isbn : "+res.getInt("isbn"));
                System.out.println("_________________________________");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void add() throws SQLException{


        System.out.println("Enter book information  :");
        System.out.print("[book title] : ");
        this.Title = sc.nextLine();
        System.out.print("[book author] : ");
        this.AuthorName = sc.nextLine();

        System.out.println("[book isbn] : ");
        this.Isbn = sc.nextInt();

        System.out.println("[book Quantity] : ");
        this.Quantity = sc.nextInt();



        String query = "INSERT INTO books (isbn,authorName, title, quantity) VALUES (?, ?, ?, ?)";
        try(Connection con  = DbConnection.getConnection();PreparedStatement ps = con.prepareStatement(query);){
            ps.setInt(1,this.Isbn);
            ps.setString(2,this.Title);
            ps.setString(3,this.AuthorName);
            ps.setInt(4,this.Quantity);

            int rs =  ps.executeUpdate();
            if (rs>0) {
                MessageUtils.showMessage("le neuvau livre est crier avec succée ", "success");
                MenuUtils.showMenu();
            }

        }catch(SQLException e ){
            MessageUtils.showMessage("une error est survenu"+e.getMessage(),"error");
            System.out.println(e.getMessage());

        }

    }
    public void editeBook() throws SQLException{
        System.out.print("entrer ISBN de livre :");
        this.Isbn = sc.nextInt();
        String query = "select * from books where isbn=?";
        try(Connection con  = DbConnection.getConnection();  PreparedStatement ps = con.prepareStatement(query);) {
            ps.setInt(1,this.Isbn);
           try(ResultSet rs = ps.executeQuery()) {

               displayBook(rs);
           }catch(SQLException e ){
               System.out.println(e.getMessage());
           }

        }catch(SQLException e){
                System.out.println(e.getMessage());
            }
       MessageUtils.showMessage("------------Changer les informations de ce livre ------------","info");

        sc.nextLine();
        System.out.print("[ titre] : ");
        String title = sc.nextLine();
        if (!title.equals("") && !title.isBlank()) this.Title = title;

        System.out.print("[ auteur ] : ");
       String authorName = sc.nextLine();
        if (!authorName.equals("") && !authorName.isBlank()) this.AuthorName = authorName;

        System.out.print("[ le livre status est \"disponible\" par defaut ] : ");
       String status = sc.nextLine();
        if (StatusUtils.isValidStatus(status))
            this.Status = status;
        else
            MenuUtils.showMenu();

        System.out.print("[ Quantité ] : ");
        String Quantity = sc.nextLine();
        if (ConversionUtils.isInteger(Quantity)){
            this.Quantity = Integer.parseInt(Quantity);
        }

        System.out.print("[ la Quantité perdu] : ");
        String lostedQuantity = sc.nextLine();
        if (ConversionUtils.isInteger(lostedQuantity)){
            this.LostedQuantity = Integer.parseInt(lostedQuantity);
        }

        System.out.print("[ la Quantité empruntée ] : ");
        String borrowedQuantity = sc.nextLine();
        if (ConversionUtils.isInteger(borrowedQuantity)){
            this.BorrowedQuantity = Integer.parseInt(borrowedQuantity);
        }

        updateBook();
        MenuUtils.showMenu();
        }


    public void updateBook() throws SQLException {

        String query = "UPDATE books SET authorName = ?, title = ?, status = ?, quantity = ?, lostedQuantity = ?, borrowedQuantity = ? WHERE isbn = ?";
        try(Connection con  = DbConnection.getConnection();PreparedStatement ps = con.prepareStatement(query);){
            ps.setString(1, AuthorName);
            ps.setString(2, Title);
            ps.setString(3, Status);
            ps.setInt(4, Quantity);
            ps.setInt(5, LostedQuantity);
            ps.setInt(6, BorrowedQuantity);
            ps.setInt(7, Isbn);
            ps.executeUpdate();

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                MessageUtils.showMessage("Book with ISBN " + this.Isbn + " updated successfully.","success");
            } else {
                System.out.println("No book found with ISBN " + this.Isbn + ". No updates performed.");
            }
        }catch(SQLException e ){
            System.out.println(e.getMessage());

        }
    }
    public void deleteBook() {
        System.out.println("entrer isbn de livre :");
        this.Isbn = sc.nextInt();
        String query = "DELETE FROM books WHERE isbn = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, Isbn);

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
    public void searchBook() throws SQLException {
        System.out.println("Entrer Titre, ou Auteur de livre :");
        String param = sc.nextLine();

        String query = "SELECT * FROM books WHERE AuthorName LIKE ? OR title LIKE ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            // Use % around the search term to perform a partial match (LIKE)
            ps.setString(1,"%" + param +"%" );
            ps.setString(2, "%"+param+"%");

            ResultSet rs = ps.executeQuery();

            displayBook(rs);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void displayBook(ResultSet res) throws SQLException {
        while (res.next()){
            System.out.println("_________________________________");
            System.out.println("Titre : " +res.getString("title") +"  | Author : "+ res.getString("authorName") + "  | ISBN : "+res.getInt("isbn"));
            System.out.println("----------------------------------");
        }
    }
    public void getReport  () throws SQLException {
        try(Connection con = DbConnection.getConnection();) { // try-with-resources => is using her to automatically closing the database connection;

            Statement st = con.createStatement();
            //String qr = "SELECT * FROM books INNER JOIN author ON books.authorId = author.id";
            String qr = "SELECT * FROM books";
            ResultSet res = st.executeQuery(qr);
            while (res.next()) {
                System.out.println("_________________________________");
                System.out.println("titre : " +res.getString("title") + "  |isbn : "+res.getInt("isbn") + "   | la quantity reservée : " +res.getInt("borrowedQuantity") +"  | la quantity perdu : " + res.getInt("lostedQuantity"));
                System.out.println("_________________________________");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



}

