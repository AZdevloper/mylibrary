package Utils;

import org.example.Main;

import java.sql.SQLException;
import java.util.Scanner;

public class MenuUtils {
   static Scanner sc = new Scanner(System.in);
    public static void showMenu() throws SQLException {
            System.out.print("cliquer sur \"Entrer\" pour retourner au menu princepale ...");
            sc.nextLine();

            Main.main(null);


    }
}
