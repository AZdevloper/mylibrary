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
    public void reservation() throws SQLException {
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

        }else creatReservation(reservedBook,cin,quantity);

    }
    public Book findBookByIsbn(int isbn){
        String qr = "SELECT * FROM books WHERE isbn = ?";
        try(Connection con = DbConnection.getConnection(); PreparedStatement ps = con.prepareStatement(qr)){
            ps.setInt(1,isbn);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                Book book = new Book();
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
    private void creatReservation(Book reservedBook, String cin, int quantity) throws SQLException {
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

        System.out.print("Enter the quantity to return: ");
        int quantityToReturn = Integer.parseInt(sc.nextLine());

        // Find the reserved book by ISBN
        Book reservedBook = findBookByIsbn(isbn);

        if (reservedBook == null) {
            System.out.println("No book with this ISBN found.");
        } else {
            int borrowedQuantity = reservedBook.getBorrowedQuantity();

            if (borrowedQuantity >= quantityToReturn && quantityToReturn > 0) {
                // Update the reservation record to mark the returned date
                markReturnedDate(reservedBook.getIsbn(), quantityToReturn);

                // Update the book's borrowed quantity
                reservedBook.setBorrowedQuantity(borrowedQuantity - quantityToReturn);
                updateBookBorrowedQuantity(reservedBook);

                System.out.println("Book returned successfully.");
            } else {
                System.out.println("Invalid quantity to return or book not reserved.");
            }
        }
    }
    //ajouter trigger pour changer status;
}
