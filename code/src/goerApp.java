package src;

import java.io.IOException;
import java.util.ArrayList;

import src.cineplex.Cineplex;
import src.movlist.MovieList;
import src.usr.Accounts;
import src.usr.MovieGoer;

/**
  Application system for movie goers to use their features
  @author Jun Xiong
  @version 1.1
  @since 2022-11-13
 */
public class goerApp {
    /**
     * constant number of cineplexes required by assignment
     */
    final static int MAX_CINEPLEX= 3; 

    /**
     * main method to run application interface of moviegoer
     * @param goer user account
     */
    public static void goerMain(MovieGoer goer) throws IOException {
        // SETUP
        System.out.println(goer.getClock()+"\n");
        ArrayList<Cineplex> cineplex= new ArrayList<Cineplex>();
        Cineplex.setCineplexCount(0);
        String[] cineplexName={"DT","JE","WM"};
        MovieList.initmovlist(goer.getClock());
        for (int i = 0; i < MAX_CINEPLEX; i++) {
            try {
                cineplex.add(new Cineplex(cineplexName[i]));
            } catch (Exception e) {
                e.printStackTrace();
            }
          
        }
        
        boolean flag= false;
        int intInput;
        while (true) {
            goer.banner();
            int max = 6;
            intInput = InputHandling.getInt("Enter a digit between 1 and "+max+": ", "Invalid input", 1, max);
            System.out.println();
            switch (intInput) {

                // List Movies and View Movie Details
                case 1:
                    goer.viewMovieDetails(goer.selectMovie());
                    break;

                // View Available Seats and Book Tickets
                case 2:
                    Cineplex chosenLocation= goer.selectCineplex(cineplex);
                    goer.bookTicket(chosenLocation, goer.selectMovie(chosenLocation));
                    break;

                // View Top 5 Listings
                case 3:
                    System.out.println("View Listing in Cineplex");
                    goer.listTop5Movies(goer.selectCineplex(cineplex));
                    break;

                // View Booking History
                case 4:
                    goer.viewBookingHistory();
                    break;

                // Add/Edit a Review
                case 5:
                    ArrayList<String> moviesWatched= new ArrayList<String>();
                    moviesWatched.addAll(goer.getMoviesWatched());
                    for (int i=0; i<moviesWatched.size(); i++) {
                        System.out.println( (i+1) + ": " + moviesWatched.get(i) );
                    }
                    System.out.println((moviesWatched.size()+1) +": "+ "---Cancel Reviewing---");
                    intInput= InputHandling.getInt("Choose a movie to review: ", "Please choose a valid option: ", 1, moviesWatched.size()+1);
                    System.out.println();
                    if (intInput==moviesWatched.size()+1) break;
                    goer.newReview(moviesWatched.get(intInput-1));
                    break;

                // Exit
                case 6: 
                    System.out.println(goer.getName()+ " Logging Out...\n----------------------");
                    flag = true;
                    break;

                default:
                    System.out.println("\nInvalid Input!\n");
                    break;
            }
            if (flag) break;
        }
        // cineplex store
        for(int i=0;i<Cineplex.getCineplexCount();i++){
            cineplex.get(i).writeFile();
        }
        MovieList.updateFiles();
        Accounts.goerStore();
    }
}