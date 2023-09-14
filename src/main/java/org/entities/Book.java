package org.entities;

import Utils.ConversionUtils;
import Utils.MenuUtils;
import Utils.MessageUtils;
import Utils.StatusUtils;

import java.sql.*;
import java.util.Calendar;
import java.util.Scanner;

public class Book {
    private int id;
    private int isbn;
    private String authorName;
    private String title;
    private String status;
    private int quantity;
    private int lostedQuantity;
    private int borrowedQuantity;
    Scanner sc = new Scanner(System.in);

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getBorrowedQuantity() {
        return borrowedQuantity;
    }

    public void setBorrowedQuantity(int borrowedQuantity) {
        this.borrowedQuantity = borrowedQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public  Book(int isbn){
        this.isbn = isbn;

}
    public void getAllBooks() throws SQLException {
        try(Connection con = DbConnection.getConnection();) { // try-with-resources => is using her to automatically closing the database connection;

            Statement st = con.createStatement();
            //String qr = "SELECT * FROM books INNER JOIN author ON books.authorId = author.id";
            String qr = "SELECT * FROM books";
            ResultSet res = st.executeQuery(qr);
            while (res.next()) {
                int availableQuantity = res.getInt("quantity") - res.getInt("borrowedQuantity");

                MessageUtils.showMessage("____________________________________________________________________________________________________","info");
                System.out.println("\u001B[31m" + "|" +"\u001B[0m" + "titre : " +res.getString("title") + "\u001B[31m" + "  |" +"\u001B[0m" + " isbn : "+ res.getInt("isbn") + "\u001B[31m" + "  |" +"\u001B[0m" + "  la quantité disponible : "+ availableQuantity + "\u001B[31m" + "  |" +"\u001B[0m" + "    la quantity reservée : " +res.getInt("borrowedQuantity")+ "\u001B[31m" + "  |" +"\u001B[0m" + "  Status : " +res.getString("status")  );
                MessageUtils.showMessage("----------------------------------------------------------------------------------------------------","info");
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
            this.title = title;
        }

        System.out.print("[auteur] : ");
        String auteur = sc.nextLine();

        if (auteur.isBlank()){
            MessageUtils.showMessage("l'auteur ne peu pas etre null", "error");
            continue;
        }if (ConversionUtils.isInteger(auteur)){
            MessageUtils.showMessage("l'auteur ne peu pas etre un nombre", "error");
            continue;
        }else{
            this.authorName = auteur;
        }

        System.out.print("[ ISBN ] : ");
        String isbn = sc.nextLine();

        if (isbn.isBlank() || !ConversionUtils.isInteger(isbn)){
            MessageUtils.showMessage("ISBN doit etre un nombre", "error");
            continue;
        }else{
            this.isbn = Integer.parseInt(isbn);
        }

        System.out.print("[ Quantité ] : ");
        String quantity = sc.nextLine();

        if (quantity.isBlank() || !ConversionUtils.isInteger(quantity)){
            MessageUtils.showMessage("l quantité doit etre un nombre", "error");
        }if (Integer.parseInt(quantity) <= 0){
            MessageUtils.showMessage("l quantité ne peu pas etre zéro", "error");
        }

        else{
            this.quantity = Integer.parseInt(quantity);
            valid = false;
        }


}while(valid);

        MessageUtils.showMessage("en coure ...", "info");

        String query = "INSERT INTO books (isbn,authorName, title, quantity) VALUES (?, ?, ?, ?)";
        try(Connection con  = DbConnection.getConnection();PreparedStatement ps = con.prepareStatement(query);){
            ps.setInt(1,this.isbn);
            ps.setString(2,this.title);
            ps.setString(3,this.authorName);
            ps.setInt(4,this.quantity);

            int rs =  ps.executeUpdate();

            if (rs>0) {
                MessageUtils.showMessage("le neuvau livre est ajouter avec succée ", "success");
                MenuUtils.showMenu();
            }
        }catch(SQLException e ){
            MessageUtils.showMessage("une error est survenu : "+e.getMessage(),"error");
            MenuUtils.showMenu();
        }

    }
    public void editeBook() throws SQLException {

        boolean valid  = true;

        do {
            System.out.print("entrer ISBN de livre à modifier : ");
            String Isbn = sc.nextLine();
            if (Isbn.isBlank() || !ConversionUtils.isInteger(Isbn)){
                MessageUtils.showMessage("ISBN doit etre un nombre", "error");
            }else{
                this.isbn = Integer.parseInt(Isbn);
                valid = false;
            }
        }while(valid);

        String query = "select * from books where isbn=?";
        try(Connection con  = DbConnection.getConnection();  PreparedStatement ps = con.prepareStatement(query);) {
            ps.setInt(1,isbn);
           try(ResultSet rs = ps.executeQuery()) {

               displayBook(rs);
           }catch(SQLException e ){
               System.out.println(e.getMessage());
           }
        }catch(SQLException e){
                System.out.println(e.getMessage());
            }

        valid = true;
        do {
        MessageUtils.showMessage("------------Changer les informations de ce livre ------------", "info");

        System.out.print("[ titre] : ");
        String title = sc.nextLine();
        if (ConversionUtils.isInteger(title)){
            MessageUtils.showMessage("le titre ne peu pas etre un nombre", "error");
            continue;
        }else{
            this.title = title;
        }
//
        System.out.print("[auteur] : ");
        String auteur = sc.nextLine();
        if  (ConversionUtils.isInteger(auteur)){
            MessageUtils.showMessage("l'auteur ne peu pas etre un nombre", "error");
            continue;
        }else{
            this.authorName = auteur;
        }
//
        System.out.print("[ le livre status ['disponible' ou 'indisponible' ] il'est \"disponible\" par defaut ] : ");
        String status = sc.nextLine();
        if (StatusUtils.isValidStatus(status)){
            this.status = status;

        }else{
            MessageUtils.showMessage("status pas validée. le livre status ['disponible' ou 'indisponible' ]","error");
            continue;
        }
//
        System.out.print("[ Quantité ] : ");
        String quantity = sc.nextLine();
        if (!ConversionUtils.isInteger(quantity)){
            MessageUtils.showMessage("l quantité doit etre un nombre", "error");
            continue;
        }if (Integer.parseInt(quantity) <= 0){
            MessageUtils.showMessage("l quantité ne peu pas etre zéro", "error");
            continue;
        }else this.quantity =Integer.parseInt(quantity);
//
        System.out.print("[ la Quantité perdu] : ");
        String lostQuantity = sc.nextLine();
        if (!lostQuantity.isBlank() && ConversionUtils.isInteger(lostQuantity)){
            this.lostedQuantity = Integer.parseInt(lostQuantity);

        }else if(!lostQuantity.isBlank() && !ConversionUtils.isInteger(lostQuantity)){
            MessageUtils.showMessage("l quantité doit etre un nombre", "error");
             continue;
        }else if (lostQuantity.isBlank()){
            this.lostedQuantity = Integer.parseInt(lostQuantity);
        }

        valid = false;


}while(valid);
        updateBook();
        MenuUtils.showMenu();
        }
    public void updateBook() throws SQLException {

        String query = "UPDATE books SET authorName = ?, title = ?, status = ?, quantity = ?, lostedQuantity = ?, borrowedQuantity = ? WHERE isbn = ?";
        try(Connection con  = DbConnection.getConnection();PreparedStatement ps = con.prepareStatement(query);){
            ps.setString(1, authorName);
            ps.setString(2, title);
            ps.setString(3, status);
            ps.setInt(4, quantity);
            ps.setInt(5, lostedQuantity);
            ps.setInt(6, borrowedQuantity);
            ps.setInt(7, isbn);

           int rs = ps.executeUpdate();

           while(rs == 0){
                MessageUtils.showMessage(".", "error");
            }
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                MessageUtils.showMessage(" le livre avec cette ISBN " + this.isbn + " est modifier avec succée.","success");
            } else {
                MessageUtils.showMessage("No book found with ISBN " + this.isbn + ". No updates performed.","error");
            }
        }catch(SQLException e ){
            System.out.println(e.getMessage());
        }
    }
    public void deleteBook() {
        System.out.print("entrer isbn de livre :");
        this.isbn = sc.nextInt();
        String query = "DELETE FROM books WHERE isbn = ?";

        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, isbn);

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                MessageUtils.showMessage(" le livre avec ce ISBN " + isbn + " est supprimer avec succée.","success");
            } else {
                MessageUtils.showMessage(" pas de livre avec cette ISBN " + isbn, "error");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void searchBook() throws SQLException {
        System.out.print("Entrer Titre, ou Auteur de livre : ");
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
            /*System.out.println("_________________________________");
            System.out.println("Titre : " +res.getString("title") +"  | Author : "+ res.getString("authorName") + "  | ISBN : "+res.getInt("isbn"));
            System.out.println("----------------------------------");*/
String redLine = "\u001B[31m" + " | " +"\u001B[0m";
            MessageUtils.showMessage("____________________________________________________________________________________________________","info");
            System.out.println(redLine +"titre : " +res.getString("title") + redLine +  " ISBN : "+res.getInt("isbn") +redLine+" Auteur : "+res.getString("authorName")+redLine+" status : "+res.getString("status")+ redLine +" quantitée : "+ res.getInt("quantity")+redLine+"   la quantité perdu : " + res.getInt("lostedQuantity"));
            MessageUtils.showMessage("----------------------------------------------------------------------------------------------------","info");
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
                MessageUtils.showMessage(" la quantity perdu est modifier avec succée pour ce ISBN  : " + isbn, "success");
            } else {
                MessageUtils.showMessage(" pas de livre avec cette ISBN : " + isbn ,"error");
            }
        } catch (SQLException e) {
            MessageUtils.showMessage("une error est survenu : " + e.getMessage(),"error");
        }
    }
}

