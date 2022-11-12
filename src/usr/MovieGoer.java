package usr;

import usr.*;
import movList.*;
import Cinema.*;
import Cineplex.*;
import ticket.*;

import java.text.SimpleDateFormat;
import java.util.*;
// import java.text.SimpleDateFormat;
// import ticket.*;

public class MovieGoer extends User {
    private String username, pw;
    private String name, hp, email;

    private static Scanner scan= new Scanner(System.in);
    private Map<String, ArrayList<Ticket>> bookingHistory= new HashMap<String, ArrayList<Ticket>>();
    private Map<String, ArrayList<String>> movieTickets= new HashMap<String, ArrayList<String>>();
    private Map<String, String> reviews= new HashMap<String, String>();
    private Map<String, Integer> ratings= new HashMap<String, Integer>();

    public MovieGoer(){}
    public MovieGoer(String username, String pw, String name, String hp, String email) {
        this.username = username;
        this.pw = pw;
        this.name = name;
        this.hp = hp;
        this.email = email;
    }

    public void banner() {
        System.out.println("1: Search/List movie");
        System.out.println("2: View movie details - including reviews and ratings");
        System.out.println("3: Check seat availability and selection of seat/s.");
        System.out.println("4: Book and purchase ticket");
        System.out.println("5: View booking history");
        System.out.println("6: List the Top 5 ranking by ticket sales OR by overall reviewers' ratings");
        System.out.println("7: Exit");
    }

    public String getUser() {
        return username;
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

    String getPW() {
        return pw;
    }

    boolean checkPW(String pw) {
        return (this.pw.equals(pw)) ? true : false;
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

    private String getthefuckingmovid(ArrayList<String> movielist, String message) {
        System.out.println(message);
        for(int i=0;i<movielist.size();i++) System.out.printf("%d. %s \n", i+1, movielist.get(i));
        System.out.println((movielist.size()+1) + ". Exit");
        int choice = inputHandling.getInt("", "Invalid input: ", 1, movielist.size()+1);
        if(choice==movielist.size()+1) return null;
        return movielist.get(choice-1);
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

    public void viewBookingHistory() {
        System.out.println("Bookings by " + name);
        int i = 0;
        for (Map.Entry transaction : bookingHistory.entrySet()) {
            System.out.println(i++ + ": " + transaction.getKey());
            for (Ticket tix : bookingHistory.get(transaction.getKey())) {
                printTicket(tix);
            }
        }
    }

    private void printTicket(Ticket tix) {
        Date time = tix.getDate();
        String dateStr = new SimpleDateFormat("dd MMM yyyy").format(time);
        String timeStr = new SimpleDateFormat("HH:mm:ss").format(time);
        System.out.println(
            "\t" + tix.getTicketID().substring(0,3) + "\n" +
            "\t" + tix.getMovieTitle() + "\n" +
            "\t" + dateStr + "\n" +
            "\t" + timeStr + "\n" +
            "\t" + tix.getSeatID() + "\n"
        );
        if (tix.getAgeGroup()==AgeGroup.STUDENT) System.out.println("\tFree 12oz Coke\n");
        if (tix.getAgeGroup()==AgeGroup.SENIOR) System.out.println("\tFree Tea / Coffee\n");
    }

    void addBookingHistory(String[] bookingDetails) {
        Ticket tix= new Ticket(
            bookingDetails[0],
            TypeOfMovie.valueOf(bookingDetails[1]),
            ClassOfCinema.valueOf(bookingDetails[2]),
            name,
            hp,
            Day.valueOf(bookingDetails[3]),
            Integer.valueOf(bookingDetails[4]),
            AgeGroup.valueOf(bookingDetails[5]),
            bookingDetails[6],
            Integer.valueOf(bookingDetails[7]), 
            bookingDetails[8]
        );
        if (!movieTickets.containsKey(bookingDetails[0])) 
            movieTickets.put(bookingDetails[0], new ArrayList<String>());
        
        movieTickets.get(bookingDetails[0]).add(bookingDetails[8]);
    }

    public void listTop5Movies(Cineplex cineplex) {
        try {
            cineplex.listTop5();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
                System.out.println(i +":\t---ADD A REVIEW---");
            } else {
                System.out.println(
                    i +":\t"+ 
                    ratings.get(tickets.get(i)) +" / 5 STARS;\t"+
                    reviews.get(tickets.get(i))
                    );
            }
        }
        do {
            System.out.println("Select Option:\t");
            intInput= Integer.valueOf(scan.nextLine());
        } while (intInput>tickets.size() || intInput<=0);
        
        // review= updateMovieReviews(tickets.get(intInput-1), movieName);
        // reviews.replace(tickets.get(intInput-1), review.substring(1));
        // ratings.replace(tickets.get(intInput-1), Integer.valueOf(review.substring(0,1)));
        
        return;
    }

    public void addReview(String tixId, String review, String rating) {
        reviews.put(tixId, review);
        ratings.put(tixId, Integer.valueOf(rating));
        
    }
}
