package usr;

import java.util.*;
// import cineplex.*;

class User {
    private boolean isAdmin= false;
    private String username;
    private String pw;

    private static Scanner scan= new Scanner(System.in);

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getUser() {
        return username;
    }

    boolean checkPW(String pw) {
        return this.pw.equals(pw) ? true : false;
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

    // public Movie selectMovie(Cinema cinema) {
    //     /* 
    //      * Cinema needs a listMovies function, void function that prints list
    //      * of movies, preferrably sorted lexicographically. The movie are 
    //      * numbered when printed, 
    //      */
    //     int choice;
    //     cinema.listMovies();
    //     do {
    //         choice= scan.nextInt();
    //     } while (choice > cinema.movieCount());
    //     Movie movie= cinema.get(choice-1);  // accounts for zero-indexing
    //     return movie;
    // }
}