package src.usr;

import java.util.*;
// import MovieList.*;

class Admin extends User {
    private boolean isAdmin= true;
    private String username, pw;

    // private static Scanner scan= new Scanner(System.in);
    // private static movList = new MovieList(scan);

    Admin(String username, String pw) {
        this.username= username;
        this.pw= pw;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getUser() {
        return username;
    }

    String getPW() {
        return pw;
    }

    boolean checkPW(String pw) {
        return (this.pw.equals(pw)) ? true : false;
    }

    // public Cinema selectCinema(Cineplex cineplex) {
    //     /* 
    //      * Cineplex needs a listCinemas function, void function that prints list
    //      * of cinemas, preferrably sorted lexicographically. The cinemas are 
    //      * numbered when printed. User selects cinema by input choice in integer. 
    //      */
    //     int choice;
    //     cineplex.listCinemas();
    //     do {
    //         int choice= scan.nextInt();
    //     } while ( choice > (int) cineplex.cinemaCount());
    //     Cinema cinema= Cineplex.get(choice-1);  // accounts for zero-indexing
    //     return cinema;
    // }

    // public void createMovieListing() {
    //     return movList.createMovie();
    // }

    // public void updateMovieListing() {
    //      /* 
    //     * Not implemented under movieList
    //     * To update a movie, it is necessary to delete
    //     * the target movie then create another
    //     */
    // }

    // public void deleteMovieListing() {
    //     System.out.println("Which movie would you like to delete? ");
    //     String Title = scan.nextLine();
    //     return movList.deleteMovie(Title);
    // }

    // public void createCinemaShowtimes() {

    // }

    // public void updateCinemaListing() {

    // }

    // public void deleteCinemaListing() {

    // }

    // public void configureSystemSettings() {
    //     // ticket prices
    //     // public holiday changes
    //     // top5list filter enable
    // }
}