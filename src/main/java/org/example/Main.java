package org.example;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        System.out.println("vuiellez choisez une option :");

        System.out.println(" 1 -- afficher tous les livres ");
        System.out.println(" 2 -- ajouter un livre");
        System.out.println(" 3 -- modifier un livre");
        System.out.println(" 4 -- suprimmer un livre");
        System.out.println(" 5 -- cherher un livre");
        System.out.println(" 6 -- voire les statistics  :");
        System.out.println(" 7 -- fair une reservation :");
        System.out.println(" 8 -- return le livre :");
        System.out.print(" ----------------------- : ");
       // System.out.print(" ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        switch ( choice){

            case 1: new Book(0).getAllBooks();

                    break;
            case 2: new Book(0).add();
                break;
            case 3:  new Book(0).editeBook();
                break;
            case 4: new Book(0).deleteBook();
                break;
            case 5: new Book(0).searchBook();
                break;
            case 6: new Book(0).getReport();
                break;
            case 7: new Reservation().createReservation();
                break;
            case 8: new Reservation().returnBook();
                break;
        }


    }
}
