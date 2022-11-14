package src;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
  An input handler that prints custom messages
  and handles adversarial input
  @author Wesley Low
  @version 1.0
  @since 2022-11-11
 */
public class InputHandling {

    /**
     * Scanner for reading input buffer
     */
    final private static Scanner sc = new Scanner(System.in);

    /**
     * Prints a custom message, then loops until a valid integer input is received
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

    /**
     * Prints a custom message, then loops until a valid integer input is received
     * A lower and upper bound is set on user input, and an error message displays
     * if user input is out of bounds
     * @param message Custom message to be printed
     * @param errmessage Error message if user input is out of bounds
     * @param lower Lower bound of input
     * @param upper Upper bound of input
     * @return Valid user input
     */
    public static int getInt(String message, String errmessage, int lower, int upper) {
        int n;
        while(true){
            n = getInt(message);
            if(n<lower || n>upper) System.out.print(errmessage);
            else break;
        }
        return n;
    }

    /**
     * Prints a custom message, then loops until a valid Date input is received
     * @return User's Date input
     */
    public static Date getDate() {
        while(true){
            System.out.print("Enter date in dd/MM/yyyy format: ");
            String date = sc.nextLine();
            System.out.println("Enter 24 hour time in HH:mm");
            String time = sc.nextLine();
            try{
                SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy HH:mm");
                sdf.setLenient(false);
                return sdf.parse(date + " " + time);
            }
            catch(Exception e){System.out.println("Invalid input");}
        }
    }

    /**
     * Prints a custom message, then loops until a valid Date input is received
     * Date input does not ask for time, only month and day
     * @return User's Date input
     */
    public static Date getDateOnly() {
        while(true){
            System.out.print("Enter date in dd/MM format: ");
            String date = sc.nextLine();
            try{
                SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM");
                sdf.setLenient(false);
                return sdf.parse(date);
            }
            catch(Exception e){System.out.println("Invalid input");}
        }
    }

    /**
     * Prints a custom message, then loops until a valid seat ID is received
     * Seat ID bounds are dependent on cinema type, which is tied to its ID
     * @param cinemaID Cinema ID
     * @return User's seat ID input
     */
    public static String getSeat(int cinemaID) {
        if(cinemaID==3){
            while(true){
                System.out.print("Enter seat ID (enter \"Exit\" to exit): ");
                String seatID = sc.nextLine();
                if(seatID.matches("^[A-D]{1}[0-5]{1}$")) return seatID;
                if(seatID.equals("Exit")) return null;
                else System.out.println("Invalid input");
            }
        }
        else{
            while(true){
                System.out.print("Enter seat ID (enter \"Exit\" to exit): ");
                String seatID = sc.nextLine();
                if(seatID.matches("^[A-F]{1}[0-9]{1}$") || seatID.matches("^[G-J]{1}[0-4]{1}$")) return seatID;
                if(seatID.equals("Exit")) return null;
                else System.out.println("Invalid input");
            }
        }
    }
}
