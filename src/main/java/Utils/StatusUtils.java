package Utils;

public class StatusUtils {
public static  Boolean  isValidStatus(String status){
    return status.equals("unavailable") || status.equals("available");
}
}
