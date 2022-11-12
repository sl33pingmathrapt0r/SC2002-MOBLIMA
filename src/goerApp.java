// package src;

import java.util.*;
import usr.*;
import cineplex.*;
import movList.*;

public class goerApp {
    final static int MAX_CINEPLEX= 3; 

    public static void goerMain(MovieGoer goer) {
        // SETUP
        ArrayList<Cineplex> cineplex= new ArrayList<Cineplex>();
        String cineplexName="AA";
        StringBuilder strBuilder = new StringBuilder(cineplexName);
        MovieList.initMovList();
        for (int i = 0; i < MAX_CINEPLEX; i++) {
            try {
                cineplex.add(new Cineplex(strBuilder.toString()));
            } catch (Exception e) {
                e.getMessage();
            }
            char digit = strBuilder.charAt(1);
            digit++;
            strBuilder.setCharAt(1, digit);
        }
        
        boolean flag= false;
        int intInput;
        while (true) {
            goer.banner();
            int max = 7;
            intInput = inputHandling.getInt("Enter a digit between 1 and "+max+": ", "Invalid input", 1, max);
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
                    System.out.println("View Listing a Cineplex");
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
                    intInput= inputHandling.getInt("Choose a movie to review: ", "Please choose a valid option: ", 1, moviesWatched.size()+1);
                    if (intInput==moviesWatched.size()+1) break;
                    goer.newReview(moviesWatched.get(intInput-1));
                    break;

                // Exit
                case 6: 
                    System.out.println(goer.getName()+ " Logging Out");
                    flag = true;
                    break;

                default:
                    System.out.println("Invalid Input");
                    break;
            }
            if (flag) break;
        }
    }
}
