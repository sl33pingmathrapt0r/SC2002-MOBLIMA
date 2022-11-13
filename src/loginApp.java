import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import movList.inputHandling;
import usr.Accounts;
import usr.Admin;
import usr.MovieGoer;

public class loginApp {
    // TO BE RENAMED AS Main
    // CURRENT MAIN NEED TO BE SPLIT TO AdminMain AND GoerMain
    private static Scanner scan= new Scanner(System.in);

    public static void loginMenu() throws IOException, ParseException{
        // APPLICATION STARTUP
        Date globalClock= new SimpleDateFormat("yyyyMMdd").parse("20221112");
        int intInput;
        while (true) {
            System.out.println(
                "\nWelcome to MOBLIMA\n\n" +
                "1. Register new account\n" +
                "2. Already a user? Login\n" +
                "3. Exit"
                );
            intInput= inputHandling.getInt(
                "Please select an option:\t", 
                "Please input a valid option, (1 or 2):\t",
                1, 3
                );
            System.out.println();

            if (intInput==3) break;

            if (intInput==1) {

                Accounts.createAcc();

            } else {

                // LOGIN
                String username, pw, strInput;
                boolean admin;
                int accLocation;
                do {
                    System.out.print("Admin Login? Please input Y/N: ");
                    strInput = scan.nextLine();
                } while (!((admin = strInput.equalsIgnoreCase("y")) || strInput.equalsIgnoreCase("n")));

                System.out.print("Enter username:\t");
                username = scan.nextLine();
                System.out.print("Enter password:\t");
                pw = scan.nextLine();
                System.out.println();

                int tries = 1;
                if ((accLocation = Accounts.isValid(admin, username, pw)) == -1) {
                    while (tries < 3) {
                        System.out.println("Invalid Account. Try again. ");
                        System.out.print("Enter username:\t");
                        username = scan.nextLine();
                        System.out.print("Enter password:\t");
                        pw = scan.nextLine();
                        System.out.println();
                        if ((accLocation = Accounts.isValid(admin, username, pw)) != -1)
                            break;
                        tries++;
                    }
                }
                if (tries == 3) {
                    System.out.println("Too many tries. Exiting account login...");
                    System.out.println();
                    continue;
                }
                System.out.println("---LOGIN SUCCESSFUL---\n");

                if (admin) {
                    Admin user= Accounts.getAdminAcc(accLocation);
                    user.setClock(globalClock);
                    globalClock= adminApp.adminMain(user);
                } else {
                    MovieGoer user= Accounts.getGoerAcc(accLocation);
                    user.setClock(globalClock);
                    goerApp.goerMain(user);
                }

            }
        } 
        scan.close();
    }
}
