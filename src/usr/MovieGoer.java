package usr;

import usr.*;
import movList.*;
import cinema.*;
import cineplex.*;
import ticket.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.text.SimpleDateFormat;
import ticket.*;

import Cineplex.Cineplex;

public class MovieGoer extends User {
    private String name, hp, email;

    private static Scanner scan= new Scanner(System.in);
    private Map<String, ArrayList<Ticket>> bookingHistory= new HashMap<String, ArrayList<Ticket>>();
    private Map<String, ArrayList<String>> movieTickets= new HashMap<String, ArrayList<String>>();
    private Map<String, String> reviews= new HashMap<String, String>();
    private Map<String, Integer> ratings= new HashMap<String, Integer>();

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
        System.out.println(mov.getTitle());
        System.out.println("Duration: " + mov.getDuration());
        System.out.println("Director: " + mov.getDirector());
        System.out.println("Synopsis: \n" + mov.getSynopsis());
        System.out.print("Cast: ");
        for(String member : mov.getCast()) System.out.println(member + "; ");
        System.out.println("Age rating: " + mov.getAgeRating());
        System.out.printf("Overall rating: %.1f/5 \n", mov.getTotalRating());
        System.out.println("Reviews: ");
        ArrayList<Integer> ratings = mov.getPastRatings();
        ArrayList<String> reviews = mov.getReviews();
        for(int i=0; i<ratings.size(); i++) System.out.printf("%d/5 - %s\n", ratings.get(i), reviews.get(i));
        System.out.println();
    }

    public boolean bookTicket(Cineplex cineplex, String title) {
        int exit = cineplex.listShowtimeByMovie(title);
        int choice = inputHandling.getInt("Select showtime", "Invalid input: ", 1, exit);
        Date showtime = cineplex.choiceOfListing(choice, title);
        if(showtime==null) return false;

        int cinemaID = cineplex.cinemaFinder(title, showtime);
        Cinema cinema = cineplex.getCinemas().get(cinemaID);
        int movieID = cinema.search(title, showtime);
        if(movieID==-1){
            System.out.println("Error occured");
            return false;
        }
        try{cinema.listVacancy(movieID);}
        catch(Exception e){
            System.out.println("Error occured");
            return false;
        }

        ArrayList<String> bookedSeatID = new ArrayList<String>();
        ArrayList<AgeGroup> bookedAgeGroup = new ArrayList<AgeGroup>(); 
        double totalPrice = 0;
        while(bookedSeatID.size()<9){
            cineplex.listVacancy(cinemaID, movieID);
            String seatID;
            System.out.println("Select vacant seat");
            while(true){
                seatID = inputHandling.getSeat(cinemaID);
                if(seatID==null) break;
                if(!cineplex.checkSeatVacant(cinemaID, movieID, seatID)){
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
            AgeGroup age;
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

            double price = Ticket.calculatePrice(cinema.getClassOfCinema(),
                                    cineplex.getMovieType(cinemaID, movieID),
                                    age, 
                                    cineplex.getTypeOfSeat(cinemaID, movieID, seatID), 
                                    showtime, 
                                    MovieList.getMovieByTitle(title).isBlockBuster(), 
                                    MovieList.getMovieByTitle(title).getStatus()==STATUS.PREVIEW);
            System.out.printf("Ticket price: %.2f. Confirm purchase? \n", price);
            System.out.println("1. Yes");
            System.out.println("2. No");
            if(inputHandling.getInt("", "Invalid input", 1, 2)==1){
                System.out.println("Purchase confirmed");
                bookedSeatID.add(seatID);
                bookedAgeGroup.add(age);
                totalPrice += price;
            }
            else{
                System.out.println("Purchase removed");
            }

        }

        if(bookedSeatID.isEmpty()){
            System.out.println("No purchases made, exiting seat booking");
            return false;
        }
        if(bookedSeatID.size()==9){
            System.out.println("Purchase limit reached, please claim your free prize from the counter");
        }
        
        System.out.printf("Total price: %.2f. Confirm purchase? \n", totalPrice);
        System.out.println("1. Yes");
        System.out.println("2. No");
        if(inputHandling.getInt("", "Invalid input", 1, 2)==2){
            System.out.println("Purchase cancelled, exiting seat booking");
            return false;
        }

        ArrayList<Ticket> purchase = new ArrayList<Ticket>();
        String transactionId= cinema.getCinemaCode() + new SimpleDateFormat("yyyyMMddHHmm").format(globalClock);
        for(int i=0; i<bookedSeatID.size(); i++){
            String tixId= transactionId+Integer.toString(i);
            purchase.add(new Ticket(name, hp, title, 
                                    bookedSeatID.get(i), 
                                    transactionId, 
                                    cineplex.getMovieType(cinemaID, movieID), 
                                    cinema.getClassOfCinema(), 
                                    showtime, 
                                    bookedAgeGroup.get(i), 
                                    cineplex.getTypeOfSeat(cinemaID, movieID, bookedSeatID.get(i)), 
                                    MovieList.getMovieByTitle(title).isBlockBuster(), 
                                    MovieList.getMovieByTitle(title).getStatus()==STATUS.PREVIEW));
            ratings.put(tixId, -1);
            reviews.put(tixId, "");
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
    }    
    
    public void viewBookingHistory() {
        System.out.println("Bookings by " + name);
        int i= 0;
        for (Map.Entry transaction : bookingHistory.entrySet()) {
            System.out.println((i+1)+": "+ transaction.getKey());
            for (Ticket tix : bookingHistory.get(transaction.getKey()))
                printTicket(tix);
            i++;
        }
    }

    private void printTicket(Ticket tix) {
        System.out.println(
            "\t" + tix.getCinemaCode() + "\n" +
            "\t" + tix.getMovieTitle() + "\n" +
            "\t" + (new SimpleDateFormat("yyyy-MM-dd\nHH:mm")).format(tix.getDate()) + "\n" +
            "\t" + tix.getSeatID() + "\n"
            );
        if (tix.getAgeGroup()==AgeGroup.valueOf("STUDENT")) System.out.println("\tFree 12oz Coke\n");
        if (tix.getAgeGroup()==AgeGroup.valueOf("SENIOR")) System.out.println("\tFree Tea / Coffee\n");
    }

    // void loadBookingHistory(String[] bookingDetails) {
    //     Ticket tix= new Ticket(
    //         bookingDetails[0],
    //         TypeOfMovie.valueOf(bookingDetails[1]),
    //         ClassOfCinema.valueOf(bookingDetails[2]),
    //         name,
    //         hp,
    //         Day.valueOf(bookingDetails[3]),
    //         Integer.valueOf(bookingDetails[4]),
    //         AgeGroup.valueOf(bookingDetails[5]),
    //         bookingDetails[6],
    //         Integer.valueOf(bookingDetails[7]), 
    //         bookingDetails[8]
    //     );
    //     if (!movieTickets.containsKey(bookingDetails[0])) 
    //         movieTickets.put(bookingDetails[0], new ArrayList<String>());
        
    //     movieTickets.get(bookingDetails[0]).add(bookingDetails[8]);
    // }

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

        if (intInput==tickets.size()+1) return;
        
        review= updateMovieReviews(tickets.get(intInput-1), movieName);
        reviews.replace(tickets.get(intInput-1), review.substring(1));
        ratings.replace(tickets.get(intInput-1), Integer.valueOf(review.substring(0,1)));
        
        return;
    }

    public void addReview(String tixId, String review, String rating) {
        reviews.put(tixId, review);
        ratings.put(tixId, Integer.valueOf(rating));
        
    }
}