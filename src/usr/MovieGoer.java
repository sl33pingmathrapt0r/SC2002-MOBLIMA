package src.usr;

import java.util.*;
import ticket.*;

public class MovieGoer extends User {
    private String username, pw, name, hp, email;
    private static Scanner scan= new Scanner(System.in);
    private ArrayList<Ticket> bookingHistory= new ArrayList<Ticket>();


    MovieGoer(String username, String pw, String name, String hp, String email) {
        this.username= username;
        this.pw= pw;
        this.name= name;
        this.hp= hp;
        this.email= email;
    }


    public String getUser() {
        return username;
    }

    boolean checkPW(String pw) {
        return (this.pw.equals(pw)) ? true : false;
    }

    public void viewMovieDetails(Movie movie) {
        /* 
         * prints details of movies offered by CINEPLEX, not
         * cinema, i.e. without showtimes, screening hall, 
         * seat number. 
         */
        movie.print();
    }

    // NO LONGER NEEDED, TO IMPLEMENT DIRECTLY INTO MAIN
    // public void selectSeat(ArrayList<String> seats) {
    //     /* 
    //      * choose a seat from CINEMA, w.r.t movie, 
    //      * showtime, and no single-seat gaps. 
    //      * Requires Cinema to check for validity (correct 
    //      * gap, available). 
    //      * Stores selected Seats temporarily before booking
    //      */
    // }
    
    public boolean bookTicket(
        String movieName, 
        ClassOfCinema cinemaClass, 
        TypeOfMovie movieType, 
        int[] time, 
        Day day, 
        Set<String> seatInfo
        ) {

        if (seatInfo.isEmpty()) {
            System.out.println("No seats have been selected yet. ");
            return false;
        }

        String strInput;
        boolean exit;
        AgeGroup age= ADULT;
        double price= 0;
        int intInput;
        ArrayList<Ticket> toBook= new ArrayList<Ticket>();

        do {
            System.out.print("Confirm purchase of chosen seats? Y/N: ");
            strInput= scan.nextLine();
        } while ( !( (exit= strInput.equalsIgnoreCase("n")) || strInput.equalsIgnoreCase("y") ) );
        
        if (exit) {
            System.out.print("Continue choosing seats...");
            return false;
        } 
        else {
            boolean weekday= day==MONDAY || day==TUESDAY || day==WEDNESDAY || day==THURSDAY;
            for (String seat : seatInfo) {

                // option for student/senior pricing
                if (weekday && time[0]< 18) {
                    System.out.print(
                        "Seat " + seat +"\n"+
                        "1. Adult\n"+
                        "2. Student\n"+
                        "3. Senior Citizen\n"+
                        "Please select your type of ticket:\t"
                    );
                    intInput= Integer.valueOf(scan.nextLine());
    
                    do {
                        switch (intInput) {
                            case 1: age= ADULT; 
                                    exit= true;
                                    break;
                            case 2: age= STUDENT;
                                    exit= true;
                                    break;
                            case 3: age= SENIOR;
                                    exit= true;
                                    break;
                            default: 
                                exit= false;
                                System.out.print("Please select a valid option:\t");
                                intInput= Integer.valueOf(scan.nextLine());
                                break;
                        }
                    } while (!exit);
                }

                // show price before proceeding with purchase
                System.out.println("Ticket Price: $" + Ticket.calculatePrice(cinemaClass, movieType, age, day));
                // do {
                //     System.out.print("Confirm purchase of chosen seats? Y/N: ");
                //     strInput= scan.nextLine();
                // } while ( !( (exit= strInput.equalsIgnoreCase("n")) || strInput.equalsIgnoreCase("y") ) );
                // if (exit) return false;

                Ticket tempTix= new Ticket(movieName, movieType, cinemaClass, name, hp, day, time, age, seat);
                price+= tempTix.getPrice;
                toBook.add(tempTix);
            }
            
            System.out.println("Total Price= $" + price);
            do {
                System.out.print("Confirm Purchase? Y/N: ");
                strInput= scan.nextLine();
            } while ( !( (exit= strInput.equalsIgnoreCase("n")) || strInput.equalsIgnoreCase("y") ) );

            if (exit) return false;
            else System.out.println("Payment Successful!");

            for (Ticket tix: toBook) {
                bookingHistory.add(tix);
            }
            // print transactionID
            return true;
        }
    }

    public void viewBookingHistory() {
        for (int i=0; i<bookingHistory.size(); i++) {
            Ticket tix= bookingHistory.get(i);
            System.out.println( 
                (i+1) + ":\n" + 
                "\t" + tix.getCinema() + "\n" +
                "\t" + tix.getMovie() + "\n" +
                "\t" + tix.getTime() + "\n" +
                "\t" + tix.getSeat() + "\n"
                );
             if (tix.clientType==STUDENT) System.out.println("\tFree 12oz Coke\n");
             if (tix.clientType==SENIOR) System.out.println("\tFree Tea / Coffee\n");
        }
    }

    //TODO 
    public void listTop5Movies(boolean ratingSort) {
        
    }
}
