package Utils;

import org.entities.Main;

import java.sql.SQLException;
import java.util.Scanner;

public class MenuUtils {
   static Scanner sc = new Scanner(System.in);
    public static void showMenu() throws SQLException {
            System.out.print("cliquer sur   "+"\u001B[34m"+"\"Entrer\"" + "\u001B[0m"+" pour retourner au menu princepale ...");
            sc.nextLine();

            Main.main(null);


    }
}
