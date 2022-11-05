package src.usr;

import java.util.*;
// import cineplex.*;

abstract class User {
    private boolean isAdmin= false;
    private String username, pw;

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

    public Movie selectMovie(MovieList movList) {
        /* 
         * Cinema needs a listMovies function, void function that prints list
         * of movies, preferrably sorted lexicographically. The movie are 
         * numbered when printed, 
         */
        int choice;
        movList.listMovies();
        do {
            choice= scan.nextInt();
        } while (choice > movList.movieCount());
        Movie movie= movList.get(choice-1);  // accounts for zero-indexing
        return movie;
    }
}