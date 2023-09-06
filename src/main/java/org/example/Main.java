package org.example;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {
        System.out.println("vuiellez choisez une option :");

        System.out.println(" 1 -- afficher tous les livres ");
        System.out.print(" ----------------------- : ");
       // System.out.print(" ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        switch ( choice){

            case 1: Book book = new Book();
                    book.getAllBooks();
                    break;



        }


    }
}
