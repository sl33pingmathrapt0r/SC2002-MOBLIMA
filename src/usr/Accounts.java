package src.usr;

import java.util.*;

public class Accounts {
    /* 
     * This class stores all accounts, both movie-goers and admin accounts. 
     * Consequently, the class will be used to whenever a new class is 
     * created. Login verification and account retrieval methods will also 
     * be completed here. 
     * No instance of this class should be created, since all accounts are
     * stored together and global. 
     */
    final private static String adminSecretKey= "helloadmin";
    // add to text file 
    private static ArrayList<User> goerAcc= new ArrayList<User>();
    private static ArrayList<User> adminAcc= new ArrayList<User>();

    private static Scanner scan= new Scanner(System.in);

    private static String getAdminKey() {
        return adminSecretKey;
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

        System.out.print("New username:\t\t");
        username= scan.nextLine();

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
