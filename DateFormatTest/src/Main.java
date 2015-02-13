
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class Main {

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        
        Date d = sdf.parse("01-Jan-1995");
        
        System.out.println(d);
    }
    
}
