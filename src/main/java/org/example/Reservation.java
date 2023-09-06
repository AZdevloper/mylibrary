package org.example;

import java.util.Date;

public class Reservation {
    private int id;
    private Book  book;
    private  Borrower borrower;
    private Date reservedDate;
    private Date returnedDate;
    private int Quantity;

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
    public boolean reservation(){
       return true;
    };
}
