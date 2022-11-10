package movList;
import java.util.*;
import java.time.*;

/**
  An input handler that prints custom messages
  and handles adversarial input
  @author Wesley Low
  @version 1.0
  @since 2022-11-11
 */
public class inputHandling {

    /**
     * Scanner for reading input buffer
     */
    final private static Scanner sc = new Scanner(System.in);

    /**
     * Prints a custom message, then loops until a valid
     * integer input is received
     * @param message Custom message to be printed
     * @return User's integer input
     */
    public static int getInt(String message) {
        int n;
        while(true){
            System.out.print(message);
            String str = sc.nextLine();
            try{
                n = Integer.parseInt(str);
                break;
            }
            catch(NumberFormatException e){
                System.out.printf("Input %s not a valid integer. \n", str);
            }
        }
        return n;
    }


    public static int getInt(String message, String errmessage, int lower, int upper) {
        int n;
        while(true){
            n = getInt(message);
            if(n<lower || n>upper) System.out.print(errmessage);
            else break;
        }
        return n;
    }

    // public static Calendar getDate() {
	// 	Calendar cal = Calendar.getInstance();
	// 	int year;
	// 	while(true){
	// 		year = inputHandling.getInt("Input year: ");
	// 		if(year<2000 || year>9999) System.out.print("Invalid input. Enter 4-digit number at least 2000: ");
	// 		else break;
    //     }
	// 	cal.set(Calendar.YEAR, year);
    //     int month;
	// 	while(true){
	// 		month = inputHandling.getInt("Input number of month: ")-1;
    //         if(month<0 || month>11) System.out.print("Invalid input. Enter integer between 1-12: ");
    //         else break;
    //     }
	// 	cal.set(Calendar.MONTH, month);
    //     int maxDays = YearMonth.of(year, month+1).lengthOfMonth();
    //     int date;
	// 	while(true){
	// 		date = inputHandling.getInt("Input date: ");
    //         if(date<1 || date>maxDays) System.out.printf("Invalid input. Enter integer between 1-%d: ", maxDays);
    //         else break;
    //     }
	// 	cal.set(Calendar.DATE, date);
    //     int hour;
	// 	while(true){
	// 		hour = inputHandling.getInt("Input hour of day: ");
    //         if(hour<0 || hour>23) System.out.print("Invalid input. Enter integer between 0-23: ");
    //         else break;
    //     }
	// 	cal.set(Calendar.HOUR_OF_DAY, hour);
    //     int half;
    //     while(true){
	// 		half = inputHandling.getInt("At 30 minutes? \n1. Yes \n2. No\n");
    //         if(half!=1 && half!=2) System.out.print("Invalid input. Enter 1 or 2: ");
    //         else break;
    //     }
    //     if(half==1) cal.set(Calendar.MINUTE, 30);
    //     else cal.set(Calendar.MINUTE, 0);
    //     return cal;
    // }
}
