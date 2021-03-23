/**
 *
 */
package ServerFileSystem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author SHIKO
 *
 */
public class Tester {

	/**
	 * @param args
	 */

	    public static void main(String[] args) throws ParseException {

	    	SimpleDateFormat sdf = new SimpleDateFormat("E, y-M-d 'at' h:m a");
	        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	        Date date1 = sdf.parse("Mon, 2018-5-7 at 6:0 pm");

	        Date date2 = sdf2.parse("2018-05-08");
	        String s = sdf2.format(date1);

	        Date d = sdf2.parse(s);
	        System.out.println(sdf2.format(d));

	        System.out.println("date1 : " + sdf.format(date1));
	        System.out.println("date2 : " + sdf2.format(date2));

	        if (date1.compareTo(date2) > 0) {
	            System.out.println("Date1 is after Date2");
	        } else if (date1.compareTo(date2) < 0) {
	            System.out.println("Date1 is before Date2");
	        } else if (date1.compareTo(date2) == 0) {
	            System.out.println("Date1 is equal to Date2");
	        } else {
	            System.out.println("How to get here?");
	        }



	}

}
