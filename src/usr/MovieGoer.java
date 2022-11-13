package usr;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import Cineplex.Cineplex;
import Cineplex.Screenings;
import cinema.Cinema;
import movList.inputHandling;
import movList.Movie;
import movList.MovieList;
import movList.STATUS;
import ticket.AgeGroup;
import ticket.Ticket;

public class MovieGoer extends User {
    private String name, hp, email;

    public void setMovieTickets(Map<String, ArrayList<String>> movieTickets) {
        this.movieTickets = movieTickets;
    }

    private Map<String, ArrayList<Ticket>> bookingHistory= new HashMap<String, ArrayList<Ticket>>();
    private Map<String, ArrayList<String>> movieTickets= new HashMap<String, ArrayList<String>>();
    private Map<String, String> reviews= new HashMap<String, String>();
    private Map<String, Integer> ratings= new HashMap<String, Integer>();

    public void setBookingHistory(Map<String, ArrayList<Ticket>> bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public MovieGoer(String username, String pw, String name, String hp, String email) {
        super(false, username, pw);
        this.name= name;
        this.hp = hp;
        this.email = email;
    }

    @Override
    public void logout() {
        Accounts.goerStore();
    }

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

    public String getName() {
        return name;
    }

    public String getHp() {
        return hp;
    }

    public String getEmail() {
        return email;
    }

    Map<String, ArrayList<Ticket>> getBookingHistory() {
        return bookingHistory;
    }

    Map<String, String> getReviews() {
        return reviews;
    }

    Map<String, Integer> getRatings() {
        return ratings;
    }

    public Set<String> getMoviesWatched() {
        return movieTickets.keySet();
    }

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

    public boolean bookTicket(Cineplex cineplex, String title) throws IOException {
        int exit = cineplex.listShowtimeByMovie(title);
        if (exit == 0)
            return false;
        int choice = inputHandling.getInt("Select showtime: ", "Invalid input: ", 1, exit+1);
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
                seatID = inputHandling.getSeat(cinemaID);
                if(seatID==null) {
                    System.out.println("Dog\n");
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
            int c = inputHandling.getInt("", "Invalid input: ", 1, 3);
            boolean flag = false;
            switch(c){
                case 1: age = AgeGroup.STUDENT; break;
                case 2: age = AgeGroup.ADULT; break;
                case 3: age = AgeGroup.SENIOR; break;
                case 4: flag = true; break;
            }
            if(flag) break;

            double price =Ticket.calculatePrice(cinema.getType(),
                                    cineplex.getMovieType(cinemaID, movieID),
                                    age, 
                                    cineplex.getTypeofSeat(cinemaID, movieID, seatID), 
                                    showtime, 
                                    MovieList.getMovieByTitle(title).isBlockBuster(), 
                                    MovieList.getMovieByTitle(title).getStatus()==STATUS.PREVIEW);
            System.out.printf("Ticket price: %.2f. Confirm purchase? \n", price);
            System.out.println("1. Yes");
            System.out.println("2. No");
            if(inputHandling.getInt("", "Invalid input", 1, 2)==1){
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
        if(inputHandling.getInt("", "Invalid input", 1, 2)==2){
            System.out.println("Purchase cancelled, exiting seat booking");
            return false;
        }

        ArrayList<Ticket> purchase = new ArrayList<Ticket>();
        String transactionId= cinema.getCinemaCode() + new SimpleDateFormat("yyMMdd").format(getClock()) + new SimpleDateFormat("HHmm").format(new Date());
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
                                    MovieList.getMovieByTitle(title).getStatus()==STATUS.PREVIEW));
            ratings.put(tixId, -1);
            reviews.put(tixId, "");
            cinema.updateVacancy(cinema.search(title,showtime),bookedSeatID.get(i));
        }
        System.out.println("Payment successful!");
        System.out.println("Tickets bought by " + name + "."); 
        System.out.println("You will receive a message to HP(" +hp+ ") and E-mail(" +email+ ") confirming your booking.");
        // print transactionID
        bookingHistory.put(transactionId, purchase);
        System.out.println(transactionId + "\n");
        return true;
    }


    public Screenings selectScreening(Cineplex cineplex) {
        ArrayList<String> movieList = cineplex.getListOfMovies();
        System.out.println("Select movie to watch: ");
        for(int i=0; i<movieList.size(); i++) System.out.printf("%d. %s\n", i+1, movieList.get(i));
        System.out.println("%d. Exit");
        int choice = inputHandling.getInt("Enter number between 1 to "+movieList.size()+1, "", 1, movieList.size()+1);
        if(choice==movieList.size()+1) return null;
        return cineplex.getScreeningTimes().get(choice-1);
    }    
    
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

    private void printTicket(Ticket tix) {
        System.out.println(
            "\t    Hall: " + tix.getCinemaCode() + "\n" +
            "\t   Movie: " + tix.getMovieTitle() + "\n" +
            "\tShowtime: " + (new SimpleDateFormat("yyyy-MM-dd\n\t\tHH:mm")).format(tix.getDate()) + "\n" +
            "\t    Seat: " + tix.getSeatID() + "\n"
            );
        if (tix.getAgeGroup()==AgeGroup.valueOf("STUDENT")) System.out.println("\tFree 12oz Coke\n");
        if (tix.getAgeGroup()==AgeGroup.valueOf("SENIOR")) System.out.println("\tFree Tea / Coffee\n");
    }

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
            if (ratings.get(tickets.get(i))== -1) {
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
        intInput= inputHandling.getInt("Select Review: ", "Please select a valid option: ", 1, tickets.size()+1);
        System.out.println();

        if (intInput==tickets.size()+1) return;
        
        review= MovieList.updateMovieReviews(tickets.get(intInput-1), movieName);
        reviews.replace(tickets.get(intInput-1), review.substring(1));
        ratings.replace(tickets.get(intInput-1), Integer.valueOf(review.substring(0,1)));
        
        return;
    }

    public void loadReview(String tixId, String review, String rating) {
        reviews.put(tixId, review);
        ratings.put(tixId, Integer.valueOf(rating));
    }
}