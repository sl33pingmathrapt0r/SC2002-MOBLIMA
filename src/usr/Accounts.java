package usr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import ticket.AgeGroup;
import ticket.ClassOfCinema;
import ticket.SeatType;
import ticket.Ticket;
import ticket.TypeOfMovie;

public class Accounts{
    /* 
     * This class stores all accounts, both movie-goers and admin accounts. 
     * Consequently, the class will be used to whenever a new class is 
     * created. Login verification and account retrieval methods will also 
     * be completed here. 
     * No instance of this class should be created, since all accounts are
     * stored together and global. 
     */
    final private static String ACC_DIR= System.getProperty("user.dir") +File.separator+ "src" +File.separator+ "usr" +File.separator+ "accounts" +File.separator;
    final private static String ADMIN_PATH= ACC_DIR + "admin" +File.separator;
    final private static String GOER_PATH= ACC_DIR + "moviegoer" +File.separator;
    final private static String ADMIN_KEY_PATH= ACC_DIR + "adminSecret.txt";



    private static ArrayList<MovieGoer> goerAcc= new ArrayList<MovieGoer>();
    private static ArrayList<Admin> adminAcc= new ArrayList<Admin>();
    private static Scanner scan= new Scanner(System.in);

    /**
     * Used upon starting application, loading all 
     * accounts into code for access. 
     */
    public static void load() {
        File adminDir= new File(ADMIN_PATH);
        if (adminDir.exists()) {
            try {
                for (File accFile : adminDir.listFiles()) {
                    BufferedReader adminFile= new BufferedReader(new FileReader(accFile));
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
        String strInput= "";
        String[] data;
        
        Map<String,ArrayList<Ticket>> txIDTicket=new HashMap<>();
        Map<String, ArrayList<String>> movieTickets= new HashMap<String, ArrayList<String>>(); 
        //tx to ticketid
        if (goerDir.exists()) {
            try {
                for (File accFile : goerDir.listFiles()) {
                    BufferedReader goerFile= new BufferedReader(new FileReader(accFile));
                    if (!goerFile.ready()) {
                        goerFile.close();
                        continue;
                    }
                    String[] userDetails= goerFile.readLine().split(",", 5);
                    MovieGoer user= new MovieGoer(
                        userDetails[0], 
                        userDetails[1], 
                        userDetails[2], 
                        userDetails[3],
                        userDetails[4]
                        );
                    // user;
                    add(user);

                    // No booking history:
                    if (!goerFile.ready()) {
                        goerFile.close();
                        continue;   
                    }
                     //add booking history
                     String i=goerFile.readLine(); //no of key value pairs
                     int noKV = Integer.valueOf(i);
                     for(int j=0;j<noKV;j++){
                         String tx = goerFile.readLine(); //transaction id
                         ArrayList<Ticket> ticketList = new ArrayList<Ticket>();
                         ArrayList<String> ticketIDs = new ArrayList<String>();
                         String tic = goerFile.readLine();
                         String movieTitle="";
                         int noOfTix = Integer.valueOf(tic); //no of tickets to be in the list
                         //creating ticket
                         for(int k=0;k<noOfTix;k++){
                             //Strings
                             String clientName = goerFile.readLine();
                             String clientContact = goerFile.readLine();
                             movieTitle = goerFile.readLine();
                             String seatID = goerFile.readLine();
                             String ticketID = goerFile.readLine();
                             //enum
                             String typeOfMovie = goerFile.readLine();
                             String classOfCinema = goerFile.readLine();
                             String date = goerFile.readLine();
                             String ageGroup = goerFile.readLine();
                             String seatType = goerFile.readLine();
                             //boolean
                             String blockbuster = goerFile.readLine();
                             String preview = goerFile.readLine();
                             
                             //type casting 
                             TypeOfMovie type = TypeOfMovie.valueOf(typeOfMovie);
                             ClassOfCinema classCinema = ClassOfCinema.valueOf(classOfCinema);
                             long d = Long.valueOf(date);
                             Date day = new Date(d);
                             AgeGroup age = AgeGroup.valueOf(ageGroup);
                             SeatType seat = SeatType.valueOf(seatType);
                             Boolean isBlockBuster = Boolean.valueOf(blockbuster);
                             Boolean isPreview = Boolean.valueOf(preview);
                             Ticket tix = new Ticket(clientName, clientContact, movieTitle, seatID, tic, type, classCinema, day, age, seat, isBlockBuster, isPreview);
                             tix.setTicketID(ticketID);
                             ticketList.add(tix);
                             ticketIDs.add(ticketID);
                         }
                         txIDTicket.put(tx, ticketList);
                         movieTickets.put(movieTitle,ticketIDs);
                     }
                    user.setBookingHistory(txIDTicket);
                    user.setMovieTickets(movieTickets);
                    // add reviews
                    while (goerFile.ready()) {
                        strInput= goerFile.readLine();
                        data= goerFile.readLine().split(",", 2);
                        user.loadReview(strInput, data[0], data[1]);
                    }
                    goerFile.close();
                }
            } catch (Exception e) {
                e.getStackTrace();
                System.out.println(e.getMessage());
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

    public static void adminStore() {
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
    }

    public static void goerStore() {
        File goerDir= new File(GOER_PATH);
        if (!goerDir.exists()) goerDir.mkdir();
        try {
            for (MovieGoer acc : goerAcc) {
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
                    Map<String, ArrayList<Ticket>> bookingHistory = acc.getBookingHistory();
                    int i = bookingHistory.size(); //number of key value pairs
                    writer.println(i);
                    for(String tx:bookingHistory.keySet()){
                        writer.println(tx); //transaction id
                        ArrayList<Ticket> ticketList = bookingHistory.get(tx);
                        int noOfTix = ticketList.size();
                        writer.println(noOfTix); //no of ticket in ticket list
                        
                        for(int j=0;j<noOfTix;j++){
                            writer.println(ticketList.get(j).getClientName());
                            writer.println(ticketList.get(j).getClientContact());
                            writer.println(ticketList.get(j).getMovieTitle());
                            writer.println(ticketList.get(j).getSeatID());
                            writer.println(ticketList.get(j).getTicketID());
                            writer.println(ticketList.get(j).getTypeOfMovie());
                            writer.println(ticketList.get(j).getClassOfCinema());
                            writer.println(ticketList.get(j).getDate().getTime());
                            writer.println(ticketList.get(j).getAgeGroup());
                            writer.println(ticketList.get(j).getSeatType());
                            writer.println(ticketList.get(j).isBlockBuster());
                            writer.println(ticketList.get(j).isPreview());
    
                        }
                    }

                for (Map.Entry review : acc.getReviews().entrySet()){
                    writer.println(review.getKey());
                    writer.println(
                        acc.getReviews().get(review.getKey()) +","+
                        acc.getRatings().get(review.getKey())
                        );
                }

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
    
    private static void add(Admin user) {
        // Add account to the appropriate storage
        adminAcc.add(user);
    }
    
    private static void add(MovieGoer user) {
        // Add account to the appropriate storage
        goerAcc.add(user);
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

    public static Admin getAdminAcc(int index) {
        /*
         * Obtain admin account from storage given its index. Used for login. 
         * This function MUST only be called after calling on validAcc, 
         * else random accounts may be returned. 
         */ 
        return adminAcc.get(index);
    }

    public static MovieGoer getGoerAcc(int index) {
        /*
         * Obtain moviegoer account from storage given its index. Used for login. 
         * This function MUST only be called after calling on validAcc, 
         * else random accounts may be returned. 
         */ 
        return goerAcc.get(index);
    }

    public static void createAcc() {
        /* 
         * Creates new User objects, and adds accounts to storage. Method 
         * utilises Accounts.add() and Accounts.getAdminKey()
         */
        boolean admin;
        String username, pw, name, email, hp, strInput;

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
                return;
            }
        }

        File adminFile= new File(ADMIN_PATH);
        System.out.print("New username (no whitespaces):\t");
        username= scan.nextLine();
        while (username.indexOf(' ')!=-1 || Arrays.asList(adminFile.list()).contains(username + ".txt")) {
            System.out.println("Invalid username or username already taken.");
            System.out.print("New username (no whitespaces):\t");
            username= scan.nextLine();
        }

        do {
            System.out.print("New password:\t\t");
            pw= scan.nextLine();
            System.out.print("Confirm password:\t");
        } while ( !pw.equals(scan.nextLine()) );
        
        if (admin) {
            Admin account= new Admin(username, pw);
            add(account);
            Accounts.adminStore();
        } 
        else {
            System.out.print("Enter name (as per NRIC):\t");
            name= scan.nextLine();
            System.out.print("Enter your mobile number:\t");
            hp= scan.nextLine();
            System.out.print("Enter your email address:\t");
            email= scan.nextLine();
            MovieGoer account= new MovieGoer(username, pw, name, hp, email);
            add(account);
            Accounts.goerStore();
        }
    }


    // testing functions below
    public static void printAcc() {
        System.out.println("goers");
        for (User acc : goerAcc) System.out.println(acc.getUser());
        System.out.println("admin");
        for (User acc : adminAcc) System.out.println(acc.getUser());
    }
}
