
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

        src.usr.Accounts.load();
        ArrayList<Cineplex> cineplex;
        Date globalClock= new Date();
        String cineplexName="AA";
        StringBuilder strBuilder = new StringBuilder(cineplexName);
        MovieList.initMovList();
        for (int i = 0; i < MAX_CINEPLEX; i++) {
            try {
                cineplex.add(new Cineplex(strBuilder.toString()));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
            
            Admin adminUser=new Admin();
            MovieGoer movieUser=new MovieGoer();
           
            System.out.println("Create new Account?");
            strInput = scan.nextLine();
            System.out.print("Enter username:\t");
            username = scan.nextLine();
            System.out.print("Enter password:\t");
            pw = scan.nextLine();
            boolean newAcc = strInput.equalsIgnoreCase("y");
            if(admin&&newAcc){
                Admin adminAcc = new Admin(username,pw);
                Accounts.add(adminAcc);
                Accounts.store();
            }
            else if(!admin&&newAcc){
                String name;
                String hp;
                String email;
                System.out.println("Enter name: ");
                name = scan.nextLine();
                System.out.println("Enter handphone number: ");
                hp = scan.nextLine();
                System.out.println("Enter email: ");
                email = scan.nextLine();
                movieUser = new MovieGoer(username, pw, name, hp, email);
                Accounts.add(movieUser);
                Accounts.store();
            }
            
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
            
            if (admin && !newAcc) {
                adminUser = new Admin(username, pw);
            }
            //user log in 
            else if(!newAcc) {
                String name;
                String hp;
                String email;
                System.out.println("Enter name: ");
                name = scan.nextLine();
                System.out.println("Enter handphone number: ");
                hp = scan.nextLine();
                System.out.println("Enter email: ");
                email = scan.nextLine();
                movieUser = new MovieGoer(username, pw, name, hp, email);
            }
            User currentUser = Accounts.get(admin, accLocation);
            
            int flag = 0;
            int option;
            do {
                int max;
                int choice;
                // InputHandling.getInt(String message);
                if (currentUser.isAdmin()) {
                    adminUser.banner();
                    max = 4;
                    choice = inputHandling.getInt("Enter a digit between 1 and "+max+": ", "Invalid input", 1, max);
                    switch (choice) {
                        // Create/Update/Remove movie listing
                        case 1:
                            System.out.println("1: Create movie listing");
                            System.out.println("2: Update movie listing");
                            System.out.println("3: Remove movie listing");
                            option = inputHandling.getInt("Enter a digit between 1 and 3: ","Invalid Option",1,3);
                            if (option == 1) {
                                adminUser.createMovieListing();
                                MovieList.updateFiles();
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
                            System.out.println("1: Create cinema showtimes");
                            System.out.println("2: Update cinema showtimes");
                            System.out.println("3: Add movie to cineplex");
                            System.out.println("4: Remove cinema showtimes");
                            option = inputHandling.getInt("Enter a digit between 1 and 4: ","Invalid Option",1,4);
                            if (option == 1) {
                                Cineplex selectedCineplex = adminUser.selectCineplex(cineplex); 
                                adminUser.createCinemaShowtimes(selectedCineplex);
                            } else if (option == 2) {
                                Cineplex selectedCineplex = adminUser.selectCineplex(cineplex); 
                                adminUser.updateCinemaShowtimes(selectedCineplex);
                            } else if (option == 3) {
                                Cineplex selectedCineplex = adminUser.selectCineplex(cineplex); 
                                adminUser.addMovieToCineplex(selectedCineplex);
                            } else if (option == 4) {
                                Cineplex selectedCineplex = adminUser.selectCineplex(cineplex); 
                                adminUser.deleteMovieListing(selectedCineplex);
                            } else {
                                System.out.println("Invalid option");
                            }
                            break;
                        // Configure System Settings
                        case 3:
                            //adminUser.configureSystemSettings();
                            break;
                        case 4:
                            System.out.println("Admin Logging Out");
                            flag = 1;
                            break;
                        default:
                            System.out.println("Invalid option");
                    }
                } else {
                    movieUser.banner();
                    max = 7;
                    choice = inputHandling.getInt("Enter a digit between 1 and "+max+": ", "Invalid option", 1, max);
                    switch (choice) {
                        // Search List Movie
                        case 1:
                            option = inputHandling.getInt("Enter a digit between 1 and 2: ","Invalid Option",1,2);
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
                                System.out.println(String.valueOf(i+1)+movieList.get(i).getTitle());
                            }
                            option=inputHandling.getInt("Enter index of movie: ", "Invalid index", 1,movieList.size()+1);
                            Movie movie = movieList.get(option);
                            System.out.println(movie.getTitle());
                            System.out.println(movie.getDirector());
                            System.out.println(movie.getDuration());
                            System.out.println(movie.getCast());
                            System.out.println(movie.getAgeRating());
                            System.out.println(movie.getSynopsis());
                            System.out.println(movie.getReviews());
                            System.out.println(movie.getTotalRating());
                            
                            break;

                        // Check seat availability and selection of seat/s.
                        // rmb to print legend
                        /* 
                        case 3:
                            // definitely need fix smth here way too nested

                            Cineplex selectedCineplex = movieUser.selectCineplex(cineplex);
                            selectedCineplex.listAllMovies();
                            int movieIndex = inputHandling.getInt("Select Movie Index", "Invalid movie index", 0,
                                    selectedCineplex.getMovieCount());
                            String movieTitle = selectedCineplex.searchMovies(movieIndex);
                            int startTime = inputHandling.getInt("Enter Start Time");
                            int date = inputHandling.getInt("Enter Date");
                            int cinemaCode = selectedCineplex.cinemaFinder(movieTitle, startTime, date);
                            int screeningIndex = selectedCineplex.getCinemas().get(cinemaCode - 1).search(movieTitle,
                                    startTime, date);
                            selectedCineplex.getCineplexName().get(cinemaCode - 1).listVacancy(screeningIndex);
                            System.out.println("O/X Single Seat Available/ Taken");
                            System.out.println("OOO/XXX Couple Seat Available/ Taken");
                            break;
*/
                        // Book and purchase ticket
                        case 4:

                            break;

                        // View booking history
                        case 5:
                        //movieUser.viewBookingHistory();
                            break;

                        // List the Top 5 ranking by ticket sales OR by overall reviewers’ ratings
                        case 6:
                            for (int i = 0; i < MAX_CINEPLEX; i++) {
                                System.out.println(String.valueOf(i+1)+cineplex.get(i));
                            }
                            int cineplexIndex = inputHandling.getInt("Choose Cineplex to list from: ","Invalid index",1,3);
                               
                            movieUser.listTop5Movies(cineplex.get(cineplexIndex-1));
                            break;

                        // exit
                        case 7:
                            System.out.println("Logging out");
                            flag = 1;
                        default:
                            System.out.println("Invalid option");
                    }

                }
            } while (flag != 1);
        }
    }
}