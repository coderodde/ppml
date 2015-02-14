
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class Main {

    private static final Map<String, Integer> monthMap;
    
    static {
        monthMap = new HashMap<String, Integer>();
        
        monthMap.put("Jan", 0);
        monthMap.put("Feb", 1);
        monthMap.put("Mar", 2);
        monthMap.put("Apr", 3);
        monthMap.put("May", 4);
        monthMap.put("Jun", 5);
        monthMap.put("Jul", 6);
        monthMap.put("Aug", 7);
        monthMap.put("Sep", 8);
        monthMap.put("Oct", 9);
        monthMap.put("Nov", 10);
        monthMap.put("Dec", 11);
    }
    
    public static void main(String[] args) throws ParseException {
        System.out.println(extractDate("01-Jan-1995"));
    }
    
    private static Date extractDate(final String dateString) {
        final Date ret = new Date();
        
        final String[] parts = dateString.split("-");
        
        if (parts.length != 3) {
            return null;
        }
        
        final int day = Integer.parseInt(parts[0]);
        final int month = monthMap.get(parts[1]);
        final int year = Integer.parseInt(parts[2]);
        
        final Calendar c = Calendar.getInstance();
       
        c.set(year, month, day);
        
        return new Date(c.getTimeInMillis());
    }
}
