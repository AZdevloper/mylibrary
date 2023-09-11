package org.entities;

import Utils.ConversionUtils;
import Utils.MenuUtils;
import Utils.MessageUtils;

import java.sql.*;
import java.util.Date;
import java.util.Scanner;

public class Reservation {
    private int id;
    private Book  book;
    private  Borrower borrower;
    private Date reservedDate;
    private Date returnedDate;
    private int Quantity;

    Scanner sc = new Scanner(System.in);

    public Book getBookId() {
        return book;
    }

    public void setBookId(Book bookId) {
        this.book = bookId;
    }

    public int getId(){
        return id;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public void setId(int id){
        this.id = id;
    }
    public Book getBook(){
        return this.book;
    }

    public void setBook(Book book){
        this.book = book;

    }

    public Borrower getBorrower() {
        return borrower;
    }

    public void setBorrower(Borrower borrower) {
        this.borrower = borrower;
    }

    public void createReservation() throws SQLException {
        boolean valid = true;
        int isbn;
        int quantity;
        String cin;
        do {
            MessageUtils.showMessage("---------Entrée les information pour crée une réservation -------", "info");

            System.out.print("ISBN de : ");
            String Isbn = sc.nextLine();
            if (Isbn.isBlank() || !ConversionUtils.isInteger(Isbn)){
                MessageUtils.showMessage("le isbn doit etre un nombre", "error");
                continue;
            }else{
                isbn = Integer.parseInt(Isbn);
            }

            System.out.print("la quantitée à reservée : ");
             String Quantity = sc.nextLine();
            if (Quantity.isBlank() || !ConversionUtils.isInteger(Quantity)){
                MessageUtils.showMessage("l quantité doit etre un nombre", "error");
                continue;
            }if (Integer.parseInt(Quantity) <= 0){
                MessageUtils.showMessage("l quantité ne peu pas etre zéro", "error");
                continue;
            }else {
                quantity = Integer.parseInt(Quantity);

            }

            System.out.print("cin de d'emprunteur : ");
              cin = sc.nextLine();

            if (cin.isBlank() || ConversionUtils.isInteger(cin)){
                MessageUtils.showMessage("le CIN doit etre une chaine des caractère ", "error");

            }else{
                MessageUtils.showMessage("en coure ...", "info");

                Book reservedBook = findBookByIsbn(isbn);
                if (reservedBook == null ){
                    MessageUtils.showMessage(" pas de livre avec cette ISBN !","error");
                }else if( !reservedBook.getStatus().equals("available") ){
                    MessageUtils.showMessage(" ce  livre est pas disponible mantenent !","error");
                }else if (reservedBook.getQuantity() - reservedBook.getBorrowedQuantity() < quantity || quantity == 0){
                    MessageUtils.showMessage("la quantity demanndée est pas disponible !","error");

                }else saveReservation(reservedBook,cin,quantity);
                valid = false;
            }
        }while (valid);

    }
    public Book findBookByIsbn(int isbn){
        String qr = "SELECT * FROM books WHERE isbn = ?";
        try(Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(qr)){
            ps.setInt(1,isbn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                Book book = new Book(0);
                book.setIsbn(isbn);
                book.setBorrowedQuantity(rs.getInt("borrowedQuantity"));
                book.setQuantity(rs.getInt("quantity"));
                book.setStatus(rs.getString("status"));
                return book;
            }else{
                return null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }
    private void saveReservation(Book reservedBook, String cin, int quantity) throws SQLException {
        try (Connection con = DbConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO reservation (bookIsbn, borrowerCin, quantity,reservationDate) VALUES (?, ?,?, ?)")) {

            Timestamp date = new Timestamp(new Date().getTime());
            ps.setInt(1, reservedBook.getIsbn());
            ps.setString(2, cin);
            ps.setInt(3, quantity);
           // ps.setString(4, new Date(new Date().getTime()));
            ps.setTimestamp(4, date);

            int rowAffected  =  ps.executeUpdate();

            if (rowAffected > 0){
                MessageUtils.showMessage("reservation a eté crièr avec succée ","success");

            }else {
                MessageUtils.showMessage("une error est suvenu ","error");
            }
        }catch (SQLException e ){
            MessageUtils.showMessage(e.getMessage(),"error");
        }
    }

    public void returnBook() throws SQLException {
        boolean valid = true;
        do {

            MessageUtils.showMessage("--------- Retourner de livre  ---------","info");

            System.out.print("Enter ISBN of the book to return: ");
            String isbn =sc.nextLine();
            int Isbn;

            if (isbn.isBlank() || !ConversionUtils.isInteger(isbn)){
                MessageUtils.showMessage("le isbn doit etre un nombre", "error");
                continue;
            }else{
                Isbn = Integer.parseInt(isbn);
            }

            System.out.print("entrer le CIN d'emprunteur : ");
            String cin = sc.nextLine();

            if (cin.isBlank() || ConversionUtils.isInteger(cin)){
                MessageUtils.showMessage("le CIN doit etre une chaine des caractère ", "error");
                continue;
            }

            Reservation holdReservation = findHoldReservation(Isbn, cin);

            if (holdReservation == null) {
                MessageUtils.showMessage("le livre n'est été pas reservée dija !","error");
            } else {
                removeHoldReservation(holdReservation);
                valid = false;
            }
        }while(valid);

    }

    public Reservation findHoldReservation(int isbn,String CIN){

        String qr = "SELECT * FROM reservation WHERE bookIsbn = ? and borrowerCin = ? ";
        try(Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(qr)){
            ps.setInt(1,isbn);
            ps.setString(2,CIN);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                Reservation reservation = new Reservation();
                reservation.book = new Book(isbn);
                reservation.borrower =  new Borrower(CIN);
                return reservation;
            }else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void removeHoldReservation(Reservation holdReservation){
        String qr = "DELETE FROM reservation WHERE bookIsbn = ? and borrowerCin = ? ";
        try(Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(qr)){
            ps.setInt(1,holdReservation.book.getIsbn());
            ps.setString(2,holdReservation.borrower.getCin());
            int rs = ps.executeUpdate();

            if (rs >0 ){
                MessageUtils.showMessage("reservation est supprimer avec succee !","success");
                MenuUtils.showMenu();
            }else{
                MessageUtils.showMessage("une error est survenu lors de la supprition !","error");
                MenuUtils.showMenu();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    //ajouter trigger pour changer status;
}
//perdu
//disponible
//emprnted