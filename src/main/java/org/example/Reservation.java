package org.example;

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
        System.out.println("---------Entrée les information pour crée une réservation -------");

        System.out.print("ISBN de : ");
        int isbn = Integer.parseInt(sc.nextLine()) ;

        System.out.print(" la quantitée à reserée : ");
        int quantity = Integer.parseInt(sc.nextLine()) ;

        System.out.print("cin de d'emprunteur : ");
        String cin = sc.nextLine();

        Book reservedBook = findBookByIsbn(isbn);
        if (reservedBook == null ){
            System.out.println(" ==> pas de livre avec cette ISBN !");
        }else if( !reservedBook.getStatus().equals("available") ){
            System.out.println("==> ce  livre est pas disponible mantenent !");
        }else if (reservedBook.getQuantity() - reservedBook.getBorrowedQuantity() < quantity || quantity == 0){
            System.out.println(" ==> la quantity demanndée est pas disponible !");

        }else saveReservation(reservedBook,cin,quantity);

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
                System.out.println("reservation a eté crièr avec succée ");
            }else {
                System.out.println("une error est suvenu ");
            }
        }catch (SQLException e ){
            System.out.println(e.getMessage());
        }
    }

    public void returnBook() throws SQLException {
        System.out.println("--------- Return a Book ---------");

        System.out.print("Enter ISBN of the book to return: ");
        int isbn = Integer.parseInt(sc.nextLine());
        System.out.print("entrer le CIN d'emprunteur : ");
        String CIN = sc.nextLine();

        Reservation holdReservation = findHoldReservation(isbn,CIN);

        if (holdReservation == null) {
            System.out.println("le livre n'est été pas reservée dija !");
        } else {
                removeHoldReservation(holdReservation);

        }
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
                System.out.println("reservation est supprimer avec succee !");
            }else{
                System.out.println("une error est survenu lors de la supprition !");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    //ajouter trigger pour changer status;
}
