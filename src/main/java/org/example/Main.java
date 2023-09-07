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
        System.out.println(" 6 -- les statistic  :");
        System.out.print(" ----------------------- : ");
       // System.out.print(" ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        switch ( choice){

            case 1: Book book = new Book();
                    book.getAllBooks();
                    break;
            case 2: Book book1 = new Book();
                book1.add();
                break;
            case 3: Book book2 = new Book();
                book2.editeBook();
                break;
            case 4: Book book3 = new Book();
                book3.deleteBook();
                break;
            case 5: Book book4 = new Book();
                book4.searchBook();
                break;

        }


    }
}
