package src;

import java.util.*;

public class Main {
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        // APPLICATION STARTUP
        Accounts.load();
        // Accounts.store();

        // LOGIN SECTION
        String username, pw, strInput;
        boolean admin;
        int accLocation;
        Scanner scan = new Scanner(System.in);
        do {
            System.out.print("Admin? Please input Y/N: ");
            strInput = scan.nextLine();
        } while (!((admin = strInput.equalsIgnoreCase("y")) || strInput.equalsIgnoreCase("n")));

        System.out.print("Enter username:\t");
        username = scan.nextLine();
        System.out.print("Enter password:\t");
        pw = scan.nextLine();

        int tries = 1;
        if ((accLocation = Accounts.isValid(admin, username, pw)) == -1) {
            while (tries < 3) {
                System.out.println("Invalid Account. Try again. ");
                System.out.print("Enter username:\t");
                username = scan.nextLine();
                System.out.print("Enter password:\t");
                pw = scan.nextLine();
                if ((accLocation = Accounts.isValid(admin, username, pw)) != -1)
                    break;
                tries++;
            }
        }
        if (tries == 3) {
            System.out.println("Too many tries. Exiting account login...");
            // break; // break from switch-case (login), return to main menu
        }

        User currentUser = Accounts.get(admin, accLocation);
        do {
            currentUser.banner();
            int max;
            string possibleBadChoice;
            int choice;
            //InputHandling.getInt(String message)
            if (currentUser.isAdmin) {
                max=4;
                choice=InputHandling.getInt("Enter a digit between 1 and ",max);
                switch(choice){
                    //Create/Update/Remove movie listing
                    case 1:
                        int option;
                        option=InputHandling.getInt("Enter a digit between 1 and 3");
                        System.out.println("1: Create movie listing");
                        System.out.println("2: Update movie listing");
                        System.out.println("3: Remove movie listing");
                        if(option==1){
                            currentUser.createMovieListing();
                        }
                        else if (option == 2){

                        }
                        else if (option == 3){

                        }
                        else{
                            System.out.println("Invalid option");
                        }
                        break;
                    //Create/Update/Remove cinema showtimes and the movies to be shown
                    case 2:
                        break;
                    //Configure System Settings
                    case 3:
                        break;
                    case 4:
                        System.out.println("Program Terminating");
                        System.exit(0);
                    default:
                        System.out.println("Invalid option");
                }
            }
            else {
                max=7;
                choice=InputHandling.getInt("Enter a digit between 1 and ",max);
                switch(choice){
                    //Search List Movie
                    case 1:
                        break;

                    //View movie details – including reviews and ratings
                    case 2:
                        break;

                    //Check seat availability and selection of seat/s.
                    case 3:
                        break;

                    //Book and purchase ticket
                    case 4:
                        break;

                    //View booking history
                    case 5:
                        currentUser.viewBookingHistory();
                        break;

                    //List the Top 5 ranking by ticket sales OR by overall reviewers’ ratings
                    case 6:
                        currentUser.listTop5Movies();
                        break;
                    
                    //exit
                    case 7:
                        System.out.println("Program Terminating");
                        System.exit(0);
                    default:
                        System.out.println("Invalid option");
                }
                
            }
        } while (true);
    }
}
