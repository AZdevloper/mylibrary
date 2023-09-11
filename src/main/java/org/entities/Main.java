package org.entities;

import Utils.MenuUtils;
import Utils.MessageUtils;
import Utils.ConversionUtils;


import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws SQLException {


       // System.out.print(" ");
        Scanner sc = new Scanner(System.in);
        int choice = 10;
        do {

            MessageUtils.showMessage("\n\n\n ***** Bienvenu à \"MyLibrary\" *****","info");
            System.out.println("\u001B[31m"+"vuiellez choisez une option :"+"\u001B[0m");

            System.out.println(" 1 -- Ajouter un livre.");
            System.out.println(" 2 -- Voire tous les livres.");
            System.out.println(" 3 -- Modifier un livre.");
            System.out.println(" 4 -- Suprimmer un livre.");
            System.out.println(" 5 -- Cherher par un livre.");
            System.out.println(" 6 -- Voire les statistics.");
            System.out.println(" 7 -- réserver un livre.");
            System.out.println(" 8 -- Retourner le livre.");
            System.out.println(" 9 -- consenltée les livres perdu .");
            System.out.println(" 0 -- Sortie. ");
            MessageUtils.showMessage("_________________________","info");

            System.out.print("entrer votre choise : ");
             String scanChoice = sc.nextLine();
            if(ConversionUtils.isInteger(scanChoice)){

                choice = Integer.parseInt(scanChoice);
                switch (choice){

                case 1: new Book(0).add();
                    break;
                case 2: new Book(0).getAllBooks();
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
                    case 9: new Book(0).checkForLostBooks();
                        break;
                case 0:
                    System.out.println("Thla al3chir");
                    break;
                default:
                    System.out.println("ce choix est pas disponible");
                    break;
            }
            }else {
                MessageUtils.showMessage("juste les nomber sont accepter ","error");
                MenuUtils.showMenu();
            }
        } while (choice != 0);


    }
}
