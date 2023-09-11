package org.entities;

import Utils.ConversionUtils;
import Utils.MenuUtils;
import Utils.MessageUtils;
import Utils.StatusUtils;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;
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
        boolean valid = true;
    do {
        MessageUtils.showMessage("------- entrer les informations de livre ------- ", "info");

        System.out.print("[book title] : ");
        String title = sc.nextLine();

        if (title.isBlank()){
            MessageUtils.showMessage("le titre ne peu pas etre null", "error");
            continue;

        }if (ConversionUtils.isInteger(title)){
            MessageUtils.showMessage("le titre ne peu pas etre un nombre", "error");
            continue;
        }else{
            this.Title = title;
        }

        System.out.print("[auteur] : ");
        String auteur = sc.nextLine();

        if (auteur.isBlank()){
            MessageUtils.showMessage("le auteur ne peu pas etre null", "error");
            continue;
        }if (ConversionUtils.isInteger(auteur)){
            MessageUtils.showMessage("le auteur ne peu pas etre un nombre", "error");
            continue;
        }else{
            this.AuthorName = auteur;
        }

        System.out.print("[ ISBN ] : ");
        String isbn = sc.nextLine();

        if (isbn.isBlank() || !ConversionUtils.isInteger(isbn)){
            MessageUtils.showMessage("le isbn doit etre un nombre", "error");
            continue;
        }else{
            this.Isbn = Integer.parseInt(isbn);
        }

        System.out.print("[ Quantité ] : ");
        String quantity = sc.nextLine();

        if (quantity.isBlank() || !ConversionUtils.isInteger(quantity)){
            MessageUtils.showMessage("l quantité doit etre un nombre", "error");
        }if (Integer.parseInt(quantity) <= 0){
            MessageUtils.showMessage("l quantité ne peu pas etre zéro", "error");
        }

        else{
            this.Quantity = Integer.parseInt(quantity);
            valid = false;
        }


}while(valid);

        MessageUtils.showMessage("en coure ...", "info");

        String query = "INSERT INTO books (isbn,authorName, title, quantity) VALUES (?, ?, ?, ?)";
        try(Connection con  = DbConnection.getConnection();PreparedStatement ps = con.prepareStatement(query);){
            ps.setInt(1,this.Isbn);
            ps.setString(2,this.Title);
            ps.setString(3,this.AuthorName);
            ps.setInt(4,this.Quantity);



            int rs =  ps.executeUpdate();

            if (rs>0) {
                MessageUtils.showMessage("le neuvau livre est ajouter avec succée ", "success");
                MenuUtils.showMenu();
            }

        }catch(SQLException e ){
            MessageUtils.showMessage("une error est survenu : "+e.getMessage(),"error");

        }
//        catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
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
           int rs = ps.executeUpdate();
            while(rs == 0){
                MessageUtils.showMessage(".", "error");
            }

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
                int availableQuantity = res.getInt("quantity") - res.getInt("borrowedQuantity");
                MessageUtils.showMessage("____________________________________________________________________________________________________","info");
                System.out.println("\u001B[31m" + "|" +"\u001B[0m" +"titre : " +res.getString("title") + "\u001B[31m" + "  |" +"\u001B[0m" +"  la quantité disponible : "+ availableQuantity + "\u001B[31m" + "  |" +"\u001B[0m" + "    la quantity reservée : " +res.getInt("borrowedQuantity") +"\u001B[31m" + "  |" +"\u001B[0m" +"   la quantity perdu : " + res.getInt("lostedQuantity")+"\u001B[31m" + "    |" +"\u001B[0m");
                MessageUtils.showMessage("----------------------------------------------------------------------------------------------------","info");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public  void checkForLostBooks(){
        MessageUtils.showMessage("en coure ...","info");

        String query = "SELECT * FROM reservation";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();
            Calendar calendar = Calendar.getInstance();

            while (rs.next()){

                Timestamp reservationDate = rs.getTimestamp("reservationDate");
                calendar.setTime(reservationDate);
                calendar.add(Calendar.DATE, 10);

                // Get the updated Timestamp
                Timestamp takenDate = new Timestamp(calendar.getTimeInMillis());

                // Get the current date and time
                Timestamp todaysDate = new Timestamp(System.currentTimeMillis());

                if (takenDate.before(todaysDate)) { // Check if takenDate is before todaysDate
                    addToLostQuantity(rs.getInt("quantity"), rs.getInt("bookIsbn"));
                }
            }

            displayBook(rs);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addToLostQuantity(int lostquantity, int isbn) {
        String query = "UPDATE books SET lostedQuantity = lostedQuantity + ? WHERE isbn = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, lostquantity);
            ps.setInt(2, isbn);

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Lost quantity updated successfully for ISBN " + isbn);
            } else {
                System.out.println("No book found with ISBN " + isbn + ". Lost quantity not updated.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating lost quantity: " + e.getMessage());
        }
    }




}

