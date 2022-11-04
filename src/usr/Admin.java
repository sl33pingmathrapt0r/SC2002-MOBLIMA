package src.usr;

import java.util.*;
// import MovieList.*;

class Admin extends User {
    private boolean isAdmin= true;
    private String username;
    private String pw;

    private static Scanner scan= new Scanner(System.in);

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

    boolean checkPW(String pw) {
        return (this.pw.equals(pw)) ? true : false;
    }

    // TODO
    public void createMovieListing() {
        // return movList.createMovie();
    }

    public void updateMovieListing() {
         /* 
        * Not implemented under movieList
        * To update a movie, it is necessary to delete
        * the target movie then create another
        */
    }

    public void deleteMovieListing() {
        // System.out.println("Which movie would you like to delete? ");
        // String Title = scan.nextLine();
        // return movList.deleteMovie(Title);
    }

    public void createCinemaShowtimes() {

    }

    public void updateCinemaListing() {

    }

    public void deleteCinemaListing() {

    }

    public void configureSystemSettings() {
        // ???
    }
}