package src.usr;

import java.util.*;
import java.io.*;

public class Accounts{
    /* 
     * This class stores all accounts, both movie-goers and admin accounts. 
     * Consequently, the class will be used to whenever a new class is 
     * created. Login verification and account retrieval methods will also 
     * be completed here. 
     * No instance of this class should be created, since all accounts are
     * stored together and global. 
     */
    final private static String PKG_DIR= System.getProperty("user.dir") +File.separator+ "src" +File.separator+ "usr" +File.separator+ "accounts" +File.separator;
    final private static String ADMIN_PATH= PKG_DIR + "admin" +File.separator;
    final private static String GOER_PATH= PKG_DIR + "moviegoer" +File.separator;
    final private static String ADMIN_KEY_PATH= PKG_DIR + "adminSecret.txt";


    private static ArrayList<User> goerAcc= new ArrayList<User>();
    private static ArrayList<User> adminAcc= new ArrayList<User>();

    /**
     * Used upon starting application, loading all 
     * accounts into code for access. 
     */
    public static void load() {
        File adminDir= new File(ADMIN_PATH);
        if (adminDir.exists()) {
            try {
                for (String accPath : adminDir.list()) {
                    BufferedReader adminFile= new BufferedReader(new FileReader(adminDir+accPath));
                    if (adminFile.ready()) {
                        String[] userDetails= adminFile.readLine().split(",", 2);
                        add(new Admin(userDetails[0], userDetails[1]));
                    }
                    adminFile.close();
                }
            } catch (Exception e) {
                System.out.println("Admin account files could not be retrieved.");
            }
        }
        else {
            try {
                adminDir.mkdir();
            } catch (Exception e) {
                System.out.println("Admin account files could not be created");
            }
        }

        File goerDir= new File(GOER_PATH);
        if (goerDir.exists()) {
            try {
                for (String accPath : goerDir.list()) {
                    BufferedReader goerFile= new BufferedReader(new FileReader(goerDir+accPath));
                    if (goerFile.ready()) {
                        String[] userDetails= goerFile.readLine().split(",", 5);
                        User user= new MovieGoer(
                            userDetails[0], 
                            userDetails[1], 
                            userDetails[2], 
                            userDetails[3],
                            userDetails[4]
                            );
                        // user;
                        add(user);

                        //add booking history
                        // while (goerFile.ready()) {
                        //     user.addTransactionHistory(goerFile.readLine());
                        //     user.addBookingHistory(goerFile.readLine().split(",", 10));
                        // }
                    }
                    goerFile.close();
                }
            } catch (Exception e) {
                System.out.println("MovieGoer account files could not be retrieved.");
            }
        }
        else {
            try {
                goerDir.mkdir();
            } catch (Exception e) {
                System.out.println("MovieGoer account files could not be created");
            }
        }
    }

    public static void store() {
        File adminDir= new File(ADMIN_PATH);
        if (!adminDir.exists()) adminDir.mkdir();
        try {
            for (User acc : adminAcc) {
                File adminFile= new File(ADMIN_PATH + acc.getUser() + ".txt");
                if (adminFile.exists()) continue;
                adminFile.createNewFile();
                BufferedWriter adminFileInfo= new BufferedWriter(new FileWriter(adminFile));
                PrintWriter writer= new PrintWriter(adminFileInfo);
                
                writer.println(acc.getUser() +","+ acc.getPW());
                writer.close();
            }
        } catch (Exception e) {
            System.out.println("Admin account files could not be retrieved.");
        }

        File goerDir= new File(GOER_PATH);
        if (!goerDir.exists()) goerDir.mkdir();
        try {
            for (User acc : goerAcc) {
                File goerFile= new File(GOER_PATH + acc.getUser() + ".txt");
                if (!goerFile.exists()) goerFile.createNewFile();
                BufferedWriter goerFileInfo= new BufferedWriter(new FileWriter(goerFile));
                PrintWriter writer= new PrintWriter(goerFileInfo);
                
                writer.println(
                    acc.getUser() +","+ 
                    acc.getPW() +","+
                    acc.getName() +","+
                    acc.getHp() +","+
                    acc.getEmail()
                    );
                
                // for (Map.Entry transaction : acc.getBookingHistory().entrySet()) {
                //     writer.println(transaction.getKey());
                //     for (Ticket tix : acc.getBookingHistory().get(transaction.getKey())) {
                //         writer.println(
                //             tix.getMovieTitle() +","+
                //             tix.getTypeOfMovie() +","+
                //             tix.getClassOfCinema() +","+
                //             tix.getDayOfWeek() +","+
                //             tix.getTimeOfMovie() +","+
                //             tix.getAgeGroup() +","+
                //             tix.getSeatID() +","+
                //             tix.getDate()
                //         );
                //     }
                // }

                writer.close();
            }
        } catch (Exception e) {
            System.out.println("MovieGoer account files could not be retrieved.");
        }
    }
    
    private static String getAdminKey() {
        try {
            FileReader keyFile= new FileReader(new File(ADMIN_KEY_PATH));
            BufferedReader keyFileInfo= new BufferedReader(keyFile);
            String adminSecretString= keyFileInfo.readLine();
            keyFileInfo.close();
            return adminSecretString;
        } catch (Exception e) {
            System.out.println("Key could not be retrieved.");
            return "\0";
        }
    }
    
    private static void add(User user) {
        // Add account to the appropriate storage
        if (user.isAdmin()) {
            adminAcc.add(user);
        }
        else {
            goerAcc.add(user);
        }
    }

    public static int isValid(boolean admin, String username, String pw) {
        // Verify if an account exists
        if (admin) {
            for (int i=0; i<adminAcc.size(); i++) {
                if (adminAcc.get(i).getUser().equals(username) && adminAcc.get(i).checkPW(pw)) 
                    return i;
            }
            return -1;
        }
        else {
            for (int i=0; i<goerAcc.size(); i++) {
                if (goerAcc.get(i).getUser().equals(username) && goerAcc.get(i).checkPW(pw)) 
                    return i;
            }
            return -1;
        }
    }

    public static User get(boolean admin, int index) {
        /*
         * Obtain the account from storage given its index. Used for login. 
         * This function MUST only be called after calling on validAcc, 
         * else random accounts may be returned. 
         */ 
        return admin ? adminAcc.get(index) : goerAcc.get(index);
    }

    public static void createAcc() {
        /* 
         * Creates new User objects, and adds accounts to storage. Method 
         * utilises Accounts.add() and Accounts.getAdminKey()
         */
        Scanner scan= new Scanner(System.in);
        boolean admin;
        String username, pw, name, email, hp, strInput;
        User account;

        do {
            System.out.print("Admin? Please input Y/N: ");
            strInput= scan.nextLine();
        } while ( !( (admin= strInput.equalsIgnoreCase("y")) || strInput.equalsIgnoreCase("n") ) );

        if (admin) {
            int tries= 1;
            System.out.print("Enter the Admin Key:\t");
            if (!scan.nextLine().equals(getAdminKey()))
                while (tries <3) {
                    System.out.print("Invalid key. Try again:\t");
                    if (scan.nextLine().equals(getAdminKey())) break;
                    tries++;
                }

            if (tries==3) {
                System.out.println("Too many tries. Exiting account creation...");
                scan.close();
                return;
            }
        }

        do {
            System.out.print("New username (no whitespaces):\t");
            username= scan.nextLine();
        } while (username.indexOf(' ')!=-1);

        do {
            System.out.print("New password:\t\t");
            pw= scan.nextLine();
            System.out.print("Confirm password:\t");
        } while ( !pw.equals(scan.nextLine()) );
        
        if (admin) {
            account= new Admin(username, pw);
        } 
        else {
            System.out.print("Enter name (as per NRIC):\t");
            name= scan.nextLine();
            System.out.print("Enter your mobile number:\t");
            hp= scan.nextLine();
            System.out.print("Enter your email address:\t");
            email= scan.nextLine();
            account= new MovieGoer(username, pw, name, hp, email);
        }
        
        scan.close();
        add(account);
    }

    // testing functions below
    public static void printAcc() {
        System.out.println("goers");
        for (User acc : goerAcc) System.out.println(acc.getUser());
        System.out.println("admin");
        for (User acc : adminAcc) System.out.println(acc.getUser());
    }
}
