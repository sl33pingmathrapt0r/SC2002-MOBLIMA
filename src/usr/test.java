package src.usr;


import java.util.*;

public class test {

    private static Scanner scan= new Scanner(System.in);
    
    public static void main(String[] args) {

        // APPLICATION STARTUP
        Accounts.load();

        Accounts.createAcc();
        Accounts.printAcc();

        Accounts.store();
        // LOGIN SECTION
        String username, pw, strInput;
        boolean admin;
        int accLocation;
        do {
            System.out.print("Admin? Please input Y/N: ");
            strInput= scan.nextLine();
        } while (! ( (admin= strInput.equalsIgnoreCase("y")) || strInput.equalsIgnoreCase("n")) );

        System.out.print("Enter username:\t");
        username= scan.nextLine();
        System.out.print("Enter password:\t");
        pw= scan.nextLine();

        int tries= 1;
        if ( (accLocation= Accounts.isValid(admin, username, pw)) == -1 ) {
            while (tries <3) {
                System.out.println("Invalid Account. Try again. ");
                System.out.print("Enter username:\t");
                username= scan.nextLine();
                System.out.print("Enter password:\t");
                pw= scan.nextLine();
                if ( (accLocation= Accounts.isValid(admin, username, pw)) != -1 ) break;
                tries++;
            }
        }
        if (tries==3) {
            System.out.println("Too many tries. Exiting account login...");
            // break; // break from switch-case (login), return to main menu
        }
        
        User currentUser= Accounts.get(admin, accLocation);

        System.out.println(currentUser.isAdmin());
        System.out.println(currentUser.getUser());

        // SELECT SEATS & BOOK TICKETS
        // int intInput;
        // Cineplex.listMovies();
        // intInput= Integer.valueOf(scan.nextLine());
        // choose Movie
        // choose showtime
        // Set<String> selectedSeats= new HashSet<String>();
        // do {
        //      cancel selections? if true => break;
        //      list vacancy
        //      option to enquire price
        //      selectSeat(); --> will add if not in set, option for remove if in set
        // } while ( !currentUser.bookTicket(..., selectedSeats) );
        //
        // selectedSeats.clear();
    }    
}
