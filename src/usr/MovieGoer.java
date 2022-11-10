package src.usr;

import java.util.*;
// import java.text.SimpleDateFormat;
// import ticket.*;

/**
   
   
   
  
 */
public class MovieGoer extends User {
    private String username, pw;
    private String name, hp, email;
    private static Scanner scan = new Scanner(System.in);
    // private Map<String, ArrayList<Ticket>> bookingHistory= new HashMap<String,
    // ArrayList<Ticket>>();
    // private ArrayList<String> transactionHistory= new ArrayList<String>();

    MovieGoer(String username, String pw, String name, String hp, String email) {
        this.username = username;
        this.pw = pw;
        this.name = name;
        this.hp = hp;
        this.email = email;
    }

    public void banner() {
        System.out.println("1: Search/List movie");
        System.out.println("2: View movie details – including reviews and ratings");
        System.out.println("3: Check seat availability and selection of seat/s.");
        System.out.println("4: Book and purchase ticket");
        System.out.println("5: View booking history");
        System.out.println("6: List the Top 5 ranking by ticket sales OR by overall reviewers’ ratings");
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

    // Map<String, ArrayList<Ticket>> getBookingHistory() {
    // return bookingHistory;
    // }

    String getPW() {
        return pw;
    }

    boolean checkPW(String pw) {
        return (this.pw.equals(pw)) ? true : false;
    }

    // public void viewMovieDetails(Movie movie) {
    // /*
    // * prints details of movies offered by CINEPLEX, not
    // * cinema, i.e. without showtimes, screening hall,
    // * seat number.
    // */
    // movie.print();
    // }

    // NO LONGER NEEDED, TO IMPLEMENT DIRECTLY INTO MAIN
    // public void selectSeat(ArrayList<String> seats) {
    // /*
    // * choose a seat from CINEMA, w.r.t movie,
    // * showtime, and no single-seat gaps.
    // * Requires Cinema to check for validity (correct
    // * gap, available).
    // * Stores selected Seats temporarily before booking
    // */
    // }

    /**
     * 
     * @param movieName
     * @param cinemaClass
     * @param movieType
     * @param time
     * @param day
     * @param seatInfo
     * @param cinema
     * @return
     */
    // public boolean bookTicket(
    // String movieName,
    // ClassOfCinema cinemaClass,
    // TypeOfMovie movieType,
    // int time,
    // Day day,
    // Set<String> seatInfo,
    // Cinema cinema
    // ) {

    // if (seatInfo.isEmpty()) {
    // System.out.println("No seats have been selected yet. ");
    // return false;
    // }

    // String transactionId, strInput;
    // boolean exit;
    // AgeGroup age= ADULT;
    // double price= 0;
    // int intInput;
    // ArrayList<Ticket> toBook= new ArrayList<Ticket>();

    // do {
    // System.out.print("Confirm purchase of chosen seats? Y/N: ");
    // strInput= scan.nextLine();
    // } while ( !( (exit= strInput.equalsIgnoreCase("n")) ||
    // strInput.equalsIgnoreCase("y") ) );

    // if (exit) {
    // System.out.print("Continue choosing seats...");
    // return false;
    // }
    // else {
    // boolean weekday= day==MONDAY || day==TUESDAY || day==WEDNESDAY ||
    // day==THURSDAY;
    // for (String seat : seatInfo) {

    // // option for student/senior pricing
    // if (weekday && time[0]< 18) {
    // System.out.print(
    // "Seat " + seat +"\n"+
    // "1. Adult\n"+
    // "2. Student\n"+
    // "3. Senior Citizen\n"+
    // "Please select your type of ticket:\t"
    // );
    // intInput= Integer.valueOf(scan.nextLine());

    // do {
    // switch (intInput) {
    // case 1: age= ADULT;
    // exit= true;
    // break;
    // case 2: age= STUDENT;
    // exit= true;
    // break;
    // case 3: age= SENIOR;
    // exit= true;
    // break;
    // default:
    // exit= false;
    // System.out.print("Please select a valid option:\t");
    // intInput= Integer.valueOf(scan.nextLine());
    // break;
    // }
    // } while (!exit);
    // }

    // // show price before proceeding with purchase
    // System.out.println("Ticket Price: $" + Ticket.calculatePrice(cinemaClass,
    // movieType, age, day));

    // Ticket tempTix= new Ticket(movieName, movieType, cinemaClass, name, hp, day,
    // time, age, seat);
    // price+= tempTix.getPrice;
    // toBook.add(tempTix);
    // }

    // System.out.println("Total Price= $" + price);
    // do {
    // System.out.print("Confirm Purchase? Y/N: ");
    // strInput= scan.nextLine();
    // } while ( !( (exit= strInput.equalsIgnoreCase("n")) ||
    // strInput.equalsIgnoreCase("y") ) );

    // if (exit) return false;
    // else System.out.println(
    // "Payment Successful!\nTickets bought by " +name+
    // ".\nYou will receive a message to HP(" +hp+ ") and E-mail(" +email+ ")
    // confirming your booking.\n"
    // );

    // // print transactionID
    // transactionId= cinema.getCinemaCode() + new
    // SimpleDateFormat("yyMMddHHmm").format(new Date());
    // bookingHistory.put(transactionId, toBook);
    // System.out.println(transactionId + "\n");
    // return true;
    // }
    // }

    // public void viewBookingHistory() {
    // System.out.println("Bookings by " + name);
    // int i= 0;
    // for (Map.Entry transaction : bookingHistory.entrySet()) {
    // System.out.println("i: "transaction.getKey());
    // for (Ticket tix : bookingHistory.get(transaction.getKey())) {
    // printTicket(tix);
    // }
    // i++;
    // }
    // }

    // private void printTicket(Ticket tix) {
    // System.out.println(
    // "\t" + tix.getCinema() + "\n" +
    // "\t" + tix.getMovie() + "\n" +
    // "\t" + tix.getDate() + "\n" +
    // "\t" + tix.getTime() + "\n" +
    // "\t" + tix.getSeat() + "\n"
    // );
    // if (tix.clientType==STUDENT) System.out.println("\tFree 12oz Coke\n");
    // if (tix.clientType==SENIOR) System.out.println("\tFree Tea / Coffee\n");
    // }

    // void addBookingHistory(String[] bookingDetails) {
    // bookingHistory.add(
    // new Ticket(
    // bookingDetails[0],
    // TypeOfMovie.valueOf(bookingDetails[1]),
    // ClassOfCinema.valueOf(bookingDetails[2]),
    // name,
    // hp,
    // Day.valueOf(bookingDetails[3]),
    // Integer.valueOf(bookingDetails[4]),
    // AgeGroup.valueOf(bookingDetails[5]),
    // bookingDetails[6],
    // Integer.valueOf(bookingDetails[7])
    // )
    // );
    // }

    // void addTransactionHistory(String transactionId) {
    // transactionHistory.add(transactionId);
    // }

    // TODO
    public void listTop5Movies() {
        int ratingSort = InputHandling("Sort by Rating? Enter 1 for rating else sort by sales");
        if (ratingSort==1) {
            Movie[] movieList = MovieList.sortByRating();
            for (int i = 0; i < 5; i++) {
                System.out.println(movieList.get(i).getTitle);
            }
        }
        else{
            Movie[] movieList = MovieList.sortBySales();
            for (int i = 0; i < 5; i++) {
                System.out.println(movieList.get(i).getTitle);
            }
        }
    }
}
