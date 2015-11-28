package builders.samudra.utils;

/**
 * Created by Shabaz on 28-Nov-15.
 */
public class Helper
{
    public static String phoneNumber = "";
    public static String normalizeNumber(String number)
    {
        if (number.startsWith("+91"))
        {
            number = number.substring(3);
        }
        if(number.startsWith("0"))
        {
            number = number.substring(1);
        }
        number = number.replaceAll(" ","").replaceAll("-", "");
        return number;
    }
}
