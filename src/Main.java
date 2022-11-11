package src;

import usr.*;
import movList.*;
import Cinema.*;
import Cineplex.*;
import ticket.*;

import java.util.*;

public class Main {
    final static int MAX_CINEPLEX = 3;
    private static Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        // APPLICATION STARTUP
        Accounts.load();
        // Accounts.store();
        ArrayList<Cineplex> cineplex;
        Date globalClock = new Date();
        String cineplexName = "AA";
        StringBuilder strBuilder = new StringBuilder(cineplexName);
        for (int i = 0; i < MAX_CINEPLEX; i++) {
            cineplex.add(new Cineplex(strBuilder.toString()));
            char digit = strBuilder.charAt(1);
            digit++;
            strBuilder.setCharAt(1, digit);
        }
        // LOGIN SECTION
        while (true) {
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
            if (admin) {
                Admin adminUser = new Admin(username, pw);
            } else {
                String name;
                String hp;
                String email;
                System.out.println("Enter name: ");
                name = scan.nextLine();
                System.out.println("Enter handphone number: ");
                hp = scan.nextLine();
                System.out.println("Enter email: ");
                email = scan.nextLine();
                MovieGoer movieUser = new MovieGoer(username, pw, name, hp, email);
            }
            User currentUser = Accounts.get(admin, accLocation);
            int flag = 0;
            do {
                int max;
                int choice;
                // InputHandling.getInt(String message);
                if (currentUser.isAdmin()) {
                    adminUser.banner();
                    max = 4;
                    choice = InputHandling.getInt("Enter a digit between 1 and "+max, "Invalid input", 1, max);
                    switch (choice) {
                        // Create/Update/Remove movie listing
                        case 1:
                            int option = InputHandling.getInt("Enter a digit between 1 and 3","Invalid Option",1,3);
                            System.out.println("1: Create movie listing");
                            System.out.println("2: Update movie listing");
                            System.out.println("3: Remove movie listing");
                            if (option == 1) {
                                Cineplex selectedCineplex = adminUser.selectCineplex(cineplex); 
                                adminUser.createMovieListing(selectedCineplex);
                            } else if (option == 2) {
                                Cineplex selectedCineplex = adminUser.selectCineplex(cineplex); 
                                adminUser.updateMovieListing(selectedCineplex);
                            } else if (option == 3) {
                                Cineplex selectedCineplex = adminUser.selectCineplex(cineplex); 
                                adminUser.deleteMovieListing(selectedCineplex);
                            } else {
                                System.out.println("Invalid option");
                            }
                            break;
                        // Create/Update/Remove cinema showtimes and the movies to be shown
                        case 2:
                            int option = InputHandling.getInt("Enter a digit between 1 and 3","Invalid Option",1,3);
                            System.out.println("1: Create cinema showtimes");
                            System.out.println("2: Update cinema showtimes");
                            System.out.println("3: Remove cinema showtimes");
                            if (option == 1) {
                                Cineplex selectedCineplex = adminUser.selectCineplex(cineplex); 
                                adminUser.createCinemaShowtimes(selectedCineplex);
                            } else if (option == 2) {
                                Cineplex selectedCineplex = adminUser.selectCineplex(cineplex); 
                                adminUser.updateCinemaListing(selectedCineplex);
                            } else if (option == 3) {
                                Cineplex selectedCineplex = adminUser.selectCineplex(cineplex); 
                                adminUser.deleteMovieListing(selectedCineplex);
                            } else {
                                System.out.println("Invalid option");
                            }
                            break;
                        // Configure System Settings
                        case 3:
                            adminUser.configureSystemSettings();
                            break;
                        case 4:
                            System.out.println("Admin Logging Out");
                            flag = 1;
                            break;
                        default:
                            System.out.println("Invalid option");
                    }
                } else {
                    max = 7;
                    choice = InputHandling.getInt("Enter a digit between 1 and "+max, "Invalid option", 1, max);
                    switch (choice) {
                        // Search List Movie
                        case 1:
                            int option = InputHandling.getInt("Enter a digit between 1 and 2","Invalid Option",1,2);
                            System.out.println("1: List Movies ");
                            System.out.println("2: Search Movie ");
                            //List Movie
                            if(option==1){
                                ArrayList<Movie> movieList = MovieList.getMovieList();
                                for(int i=0;i<movieList.size();i++){
                                    System.out.println(movieList.get(i));
                                }
                            }
                            else if(option==2){
                                System.out.println("Enter Movie Title that you are searching");
                                String movieTitle = scan.nextLine();
                                boolean exist = MovieList.titleExists(movieTitle);
                                if(exist){
                                    System.out.println("Movie exist");
                                }
                                else{
                                    System.out.println("Movie does not exist");
                                }
                            }  
                            break;

                        // View movie details – including reviews and ratings
                        case 2:
                            ArrayList<Movie> movieList = MovieList.getMovieList();
                            for(int i=0;i<movieList.size();i++){
                                System.out.println(String.valueOf(i+1)+movieList.get(i));
                            }
                            int option=InputHandling.getInt("Enter index of movie", "Invalid index", 0,movieList.size());
                            break;

                        // Check seat availability and selection of seat/s.
                        // rmb to print legend
                        case 3:
                            // definitely need fix smth here way too nested

                            Cineplex selectedCineplex = movieUser.selectCineplex(cineplex);
                            selectedCineplex.ListAllMovies();
                            int movieIndex = InputHandling.getInt("Select Movie Index", "Invalid movie index", 0,
                                    selectedCineplex.getMovieCount);
                            String movieTitle = selectedCineplex.SearchMovies(movieIndex);
                            int startTime = InputHandling.getInt("Enter Start Time");
                            int date = InputHandling.getInt("Enter Date");
                            int cinemaCode = selectedCineplex.CinemaFinder(movieTitle, startTime, date);
                            int screeningIndex = selectedCineplex.getCinema().get(cinemaCode - 1).search(movieTitle,
                                    startTime, date);
                            selectedCineplex.getCineplexName().get(cinemaCode - 1).listVacancy(screeningIndex);
                            System.out.println("O/X Single Seat Available/ Taken");
                            System.out.println("OOO/XXX Couple Seat Available/ Taken");
                            break;

                        // Book and purchase ticket
                        case 4:

                            break;

                        // View booking history
                        case 5:
                        movieUser.viewBookingHistory();
                            break;

                        // List the Top 5 ranking by ticket sales OR by overall reviewers’ ratings
                        case 6:
                            for (int i = 0; i < MAX_CINEPLEX; i++) {
                                System.out.println(cineplex.get(i));
                            }
                            int cineplexIndex = InputHandling.getInt("Choose Cineplex to list from","Invalid index",0,3);
                               
                            movieUser.listTop5Movies(cineplex.get(cineplexIndex));
                            break;

                        // exit
                        case 7:
                            System.out.println("Logging out");
                            System.exit(0);
                        default:
                            System.out.println("Invalid option");
                    }

                }
            } while (true);
        }
    }
}
