package src.usr;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import src.cineplex.Cineplex;
import src.cineplex.Screenings;
import src.cinema.Cinema;
import src.InputHandling;
import src.movlist.Movie;
import src.movlist.MovieList;
import src.movlist.Status;
import src.ticket.AgeGroup;
import src.ticket.Ticket;

/**
  This class represents a MovieGoer account, 
  a type of User account, the features 
  MovieGoer can call upon.
  @author Jun Xiong
  @version 1.3
  @since 2022-11-13
 */
public class MovieGoer extends User {

    /**
     * String of user's name
     */
    private String name; 

    /**
     * String of user's mobile contact
     */
    private String hp;

    /**
     * String of user's email contact
     */
    private String email;

    /**
     * Mapping of transaction IDs to tickets bought
     */
    private Map<String, ArrayList<Ticket>> bookingHistory= new HashMap<String, ArrayList<Ticket>>();

    /**
     * Mapping of movie title to ticket IDs
     */
    private Map<String, ArrayList<String>> movieTickets= new HashMap<String, ArrayList<String>>();

    /**
     * Mapping of ticket ID to review
     */
    private Map<String, String> reviews= new HashMap<String, String>();

    /**
     * Mapping of ticket ID to rating
     */
    private Map<String, Integer> ratings= new HashMap<String, Integer>();

    /**
     * Constructor for MovieGoer account
     * @param username String username used for login
     * @param pw String password used for login
     * @param name String user's name
     * @param hp String user's mobile contact
     * @param email String user's email contact
     */
    public MovieGoer(String username, String pw, String name, String hp, String email) {
        super(false, username, pw);
        this.name= name;
        this.hp = hp;
        this.email = email;
    }

    /**
     * Override User method
     * Logs user out of programme
     */
    @Override
    public void logout() {
        Accounts.goerStore();
    }

    /**
     * Prints menu available to movie goer
     */
    public void banner() {
        System.out.println(
            "1: List Movies and View Movie Details\n" +
            "2: View Available Seats and Book Tickets\n" +
            "3: View Top 5 Listings\n" +
            "4: View Booking History\n" +
            "5: Add/Edit a Review\n" +
            "6: Exit"
            );
    }

    /**
     * Return the movies watched by user
     * @return Set of movies watched
     */
    public Set<String> getMoviesWatched() {
        return movieTickets.keySet();
    }

    /**
     * Allows users to view movie details of chosen movie
     * @param title String movie title of choice
     */
    public void viewMovieDetails(String title) {
        Movie mov = MovieList.getMovieByTitle(title);
        System.out.println();
        System.out.println(mov.getTitle());
        System.out.println("Duration: " + mov.getDuration());
        System.out.println("Director: " + mov.getDirector());
        System.out.println("Synopsis: \n" + mov.getSynopsis());
        System.out.print("Cast: ");
        for(String member : mov.getCast()) System.out.println(member + "; ");
        System.out.println("Age rating: " + mov.getAgeRating());
        System.out.println();
        System.out.printf("Overall rating: %.1f/5 \n", mov.getTotalRating());
        System.out.println("Reviews: ");
        ArrayList<Integer> ratings = mov.getPastRatings();
        ArrayList<String> reviews = mov.getReviews();
        for(int i=0; i<ratings.size(); i++) System.out.printf("%d/5 - %s\n", ratings.get(i), reviews.get(i));
        System.out.println();
    }

    /**
     * Allows users to book tickets for a movie at a cineplex
     * @param cineplex Cineplex location
     * @param title String movie title
     * @return boolean success of ticket booking
     */
    public boolean bookTicket(Cineplex cineplex, String title) throws IOException {
        int exit = cineplex.listShowtimeByMovie(title);
        if (exit == 0)
            return false;
        int choice = InputHandling.getInt("Select showtime: ", "Invalid input: ", 1, exit+1);
        if(choice == exit+1)
            return false;
        Date showtime = cineplex.choiceOfListing(choice-1, title);
        if(showtime==null) return false;

        int cinemaID = cineplex.cinemaFinder(title, showtime);
        Cinema cinema = cineplex.getCinemas().get(cinemaID-1);
        int movieID = cinema.search(title, showtime);
        if(movieID==-1){
            System.out.println("Movie not found");
            return false;
        }
        
        ArrayList<String> bookedSeatID = new ArrayList<String>();
        ArrayList<AgeGroup> bookedAgeGroup = new ArrayList<AgeGroup>(); 
        double totalPrice = 0;
        while(bookedSeatID.size()<9){
            cineplex.listVacancy(cinemaID, movieID);
            String seatID;
            System.out.println("\nSelect vacant seat");
            while(true){
                seatID = InputHandling.getSeat(cinemaID);
                if(seatID==null) {
                    break;}
                if(cineplex.checkVacancy(cinemaID, movieID, seatID)){
                    System.out.println("Seat occupied");
                    continue;
                }
                boolean flag = false;
                for(String bookedSeats : bookedSeatID){
                    if(bookedSeats.equals(seatID)){
                        System.out.println("Seat occupied");
                        flag = true;
                        break;
                    }
                }
                if(flag) continue;
                break;
            }
            if(seatID==null) break;
            AgeGroup age=AgeGroup.ADULT;
            System.out.println("Select ticket type: ");
            System.out.println("1. STUDENT");
            System.out.println("2. ADULT");
            System.out.println("3. SENIOR");
            System.out.println("4. Exit");
            int c = InputHandling.getInt("", "Invalid input: ", 1, 3);
            boolean flag = false;
            switch(c){
                case 1: age = AgeGroup.STUDENT; break;
                case 2: age = AgeGroup.ADULT; break;
                case 3: age = AgeGroup.SENIOR; break;
                case 4: flag = true; break;
            }
            if(flag) break;

            double price =Ticket.calculatePrice(
                cinema.getType(),
                cineplex.getMovieType(cinemaID, movieID),
                age, 
                cineplex.getTypeofSeat(cinemaID, movieID, seatID), 
                showtime, 
                MovieList.getMovieByTitle(title).isBlockBuster(), 
                MovieList.getMovieByTitle(title).getStatus()==Status.PREVIEW
                );
            System.out.printf("Ticket price: %.2f. Confirm purchase? \n", price);
            System.out.println("1. Yes");
            System.out.println("2. No");
            if(InputHandling.getInt("", "Invalid input", 1, 2)==1){
                System.out.println("Selection confirmed");
                bookedSeatID.add(seatID);
                bookedAgeGroup.add(age);
                totalPrice += price;
            }
            else{
                System.out.println("Selection removed");
            }

        }

        if(bookedSeatID.isEmpty()){
            System.out.println("No purchases made, exiting seat booking");
            return false;
        }
        if(bookedSeatID.size()==9){
            System.out.println("Purchase limit reached, please claim your free prize from the counter");
        }
        
        System.out.printf("Total price: %.2f. Purchase? \n", totalPrice);
        System.out.println("1. Yes");
        System.out.println("2. No");
        if(InputHandling.getInt("", "Invalid input", 1, 2)==2){
            System.out.println("Purchase cancelled, exiting seat booking");
            return false;
        }

        ArrayList<Ticket> purchase = new ArrayList<Ticket>();
        String transactionId= cinema.getCinemaCode() + new SimpleDateFormat("yyMMdd").format(getClock()) + new SimpleDateFormat("HHmm").format(new Date());
        if (!movieTickets.containsKey(title)) {
            System.out.println(movieTickets);
            movieTickets.put(title, new ArrayList<String>());
        }
        for(int i=0; i<bookedSeatID.size(); i++){
            String tixId= transactionId+Integer.toString(i);
            purchase.add(new Ticket(name, hp, title, 
                                    bookedSeatID.get(i), 
                                    transactionId, 
                                    cineplex.getMovieType(cinemaID, movieID), 
                                    cinema.getType(), 
                                    showtime, 
                                    bookedAgeGroup.get(i), 
                                    cineplex.getTypeofSeat(cinemaID, movieID, bookedSeatID.get(i)), 
                                    MovieList.getMovieByTitle(title).isBlockBuster(), 
                                    MovieList.getMovieByTitle(title).getStatus()==Status.PREVIEW));
            movieTickets.get(title).add(tixId);
            ratings.put(tixId, -1);
            reviews.put(tixId, "");
            cinema.updateVacancy(cinema.search(title,showtime), bookedSeatID.get(i));
        }
        System.out.println("Payment successful!");
        System.out.println("Tickets bought by " + name + "."); 
        System.out.println("You will receive a message to HP(" +hp+ ") and E-mail(" +email+ ") confirming your booking.");
        // print transactionID
        bookingHistory.put(transactionId, purchase);
        System.out.println(transactionId + "\n");
        return true;
    }


    /**
     * Allows user to choose a showtime at a Cineplex
     * @param cineplex Cineplex location
     * @return Screenings object selected by user
     */
    public Screenings selectScreening(Cineplex cineplex) {
        ArrayList<String> movieList = cineplex.getListOfMovies();
        System.out.println("Select movie to watch: ");
        for(int i=0; i<movieList.size(); i++) System.out.printf("%d. %s\n", i+1, movieList.get(i));
        System.out.println("%d. Exit");
        int choice = InputHandling.getInt("Enter number between 1 to "+movieList.size()+1, "", 1, movieList.size()+1);
        if(choice==movieList.size()+1) return null;
        return cineplex.getScreeningTimes().get(choice-1);
    }    
    
    /**
     * Allows user to see their booking history
     */
    public void viewBookingHistory() {
        System.out.println("\nBookings by " + name +":\n");
        int i= 0;
        if (bookingHistory.isEmpty()) {
            System.out.println("No bookings have been made...\n");
            return;
        }
        for (Map.Entry transaction : bookingHistory.entrySet()) {
            System.out.println((i+1)+": "+ transaction.getKey());
            for (Ticket tix : bookingHistory.get(transaction.getKey()))
                printTicket(tix);
            i++;
        }
        System.out.println();
    }

    /**
     * Prints the details  of each ticket
     * @param tix Ticket instance of a ticket
     */
    private void printTicket(Ticket tix) {
        System.out.println(
            "\t    Hall: " + tix.getCinemaCode() + "\n" +
            "\t   Movie: " + tix.getMovieTitle() + "\n" +
            "\tShowtime: " + (new SimpleDateFormat("yyyy-MM-dd\n\t\t  HH:mm")).format(tix.getDate()) + "\n" +
            "\t    Seat: " + tix.getSeatID() + "\n"
            );
        if (tix.getAgeGroup()==AgeGroup.valueOf("STUDENT")) System.out.println("\tFree 12oz Coke\n");
        if (tix.getAgeGroup()==AgeGroup.valueOf("SENIOR")) System.out.println("\tFree Tea / Coffee\n");
    }

    /**
     * Allows user to write a review for a movie they have watched
     * @param movieName String movie title for which user is reviewing
     */
    public void newReview(String movieName) {
        // ASSUMPTION: EACH TICKET HAS BEEN WATCHED BY UNIQUE VIEWER

        // check valid
        if (!movieTickets.containsKey(movieName)) {
            System.out.println("You have not watched this movie.");
            return;
        }
        
        ArrayList<String> tickets= movieTickets.get(movieName);
        String review;
        int intInput;
        System.out.println("Choose a review to edit: "); 
        for (int i=0; i<tickets.size(); i++) {
            if (ratings.get(tickets.get(i))!=null&&ratings.get(tickets.get(i))== -1 ) {
                System.out.println((i+1) +":\t---ADD A REVIEW---");
            } else {
                System.out.println(
                    (i+1) +":\t"+ 
                    ratings.get(tickets.get(i)) +" / 5 STARS;\t"+
                    reviews.get(tickets.get(i))
                    );
            }
        }
        System.out.println((tickets.size()+1) +":\t"+ "---CANCEL REVIEWING---");
        intInput= InputHandling.getInt("Select Review: ", "Please select a valid option: ", 1, tickets.size()+1);
        System.out.println();

        if (intInput==tickets.size()+1) return;
        
        review= MovieList.updateMovieReviews(tickets.get(intInput-1), movieName);
        reviews.replace(tickets.get(intInput-1), review.substring(1));
        ratings.replace(tickets.get(intInput-1), Integer.valueOf(review.substring(0,1)));
        
        return;
    }

    /**
     * Helper method for loading the users' reviews and ratings data from .txt file
     * @param tixId String ticket ID
     * @param review String review by user
     * @param rating String rating by user
     */
    public void loadReview(String tixId, String review, String rating) {
        reviews.put(tixId, review);
        ratings.put(tixId, Integer.valueOf(rating));
    }

    /**
     * Getter for name
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for mobile contact
     * @return String mobile number
     */
    public String getHp() {
        return hp;
    }

    /**
     * Getter for email contact
     * @return String email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter for booking history
     * @return Map of bookingHistory
     */
    Map<String, ArrayList<Ticket>> getBookingHistory() {
        return bookingHistory;
    }

    /**
     * Setter for booking history
     * @param bookingHistory Data structure of bookings to store
     */
    public void setBookingHistory(Map<String, ArrayList<Ticket>> bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    /**
     * Setter for movieTickets bought by user
     * @param movieTickets Data structure of movieTickets to set
     */
    public void setMovieTickets(Map<String, ArrayList<String>> movieTickets) {
        this.movieTickets = movieTickets;
    }

    /**
     * Getter for reviews by user
     * @return Map of reviews
     */
    Map<String, String> getReviews() {
        return reviews;
    }

    /**
     * Getter for ratings by user
     * @return Map of ratings
     */
    Map<String, Integer> getRatings() {
        return ratings;
    }
}