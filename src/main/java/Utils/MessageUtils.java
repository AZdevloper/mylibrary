package Utils;

import java.sql.SQLException;

public class MessageUtils {
    public static void showMessage(String message, String variation) {
        final String RESET = "\u001B[0m";
        final String RED = "\u001B[31m";
        final String BLUE = "\u001B[34m";
        final String GREEN = "\u001B[32m";
        if(variation.equals("info"))
            System.out.println(BLUE +message + RESET);
        else if (variation.equals("error")){
            System.out.println(RED +message + RESET);
        }
        else if (variation.equals("success"))
            System.out.println(GREEN +message + RESET);
    }
}
