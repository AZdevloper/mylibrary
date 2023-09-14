package Utils;

public class StatusUtils {
public static  Boolean  isValidStatus(String status){
    return status.equals("disponible") || status.equals("indisponible");
}
}
