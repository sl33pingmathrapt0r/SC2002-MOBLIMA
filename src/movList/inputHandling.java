package movList;
import java.util.*;
import java.text.SimpleDateFormat;

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

    public static Date getDate() {
        while(true){
            System.out.print("Enter date in dd/MM/yyyy format: ");
            String date = sc.nextLine();
            System.out.println("Enter 24 hour time in HH:mm:ss");
            String time = sc.nextLine();
            try{
                SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                sdf.setLenient(false);
                return sdf.parse(date + " " + time);
            }
            catch(Exception e){System.out.println("Invalid input");}
        }
    }
}
