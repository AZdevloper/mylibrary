package Utils;

public class ConversionUtils {
    public static boolean isInteger(String value) {
        try {
            int intValue = Integer.parseInt(value);
            return true; // Parsing succeeded; it's an integer
        } catch (NumberFormatException e) {
            //MessageUtils.showMessage("vueillez entrer un nombre","error");
            return false; // Parsing failed; it's not an integer
        }
    }


}
