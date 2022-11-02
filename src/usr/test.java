package src.usr;

import java.util.*;

public class test {

    private static Scanner scan= new Scanner(System.in);
    
    public static void main(String[] args) {
        Accounts.createAcc();
        Accounts.printAcc();

        // login section
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
        
        User current= Accounts.get(admin, accLocation);

        System.out.println(current.isAdmin());
        System.out.println(current.getUser());
    }    
}
