package usr;

import movList.*;
import cinema.*;
import Cineplex.*;
import ticket.*;

import java.util.*;

//import Cineplex.Cineplex;
//import movList.inputHandling;

// import java.util.*;
// import cineplex.*;

public abstract class User {
    private boolean isAdmin;
    private String username, pw;
    private Date syncClock;

    public User(boolean isAdmin, String username, String pw) {
        this.isAdmin= isAdmin;
        this.username= username;
        this.pw= pw;
    }

    public abstract void banner();
    public abstract void logout();

    public Cineplex selectCineplex(ArrayList<Cineplex>cineplex){
        for(int i=0;i<cineplex.size();i++){
            System.out.println(String.valueOf(i+1)+": "+cineplex.get(i).getCineplexName());
        }
        int choice=inputHandling.getInt("Enter Cineplex index: ","Invalid Index",1,cineplex.size()+1);
        return cineplex.get(choice-1);
    }

    public String selectMovie() {
        ArrayList<Movie> movList = MovieList.getMovieList();
        int i = 1;
        System.out.println("Select movie: ");
        for(Movie mov : movList) System.out.printf("%d. %s \n", i++, mov.getTitle());
        int idx = inputHandling.getInt("", "Invalid input", 1, i-1);
        if(idx==i-1) return null;
        return movList.get(i).getTitle();
    }

    public String selectMovie(Cineplex cineplex) {
        ArrayList<String> movList= cineplex.getListOfMovies();
        int i=1;
        System.out.println("Select movie: ");
        for(String mov : movList) System.out.printf("%d. %s \n", i++, mov);
        int idx = inputHandling.getInt("", "Invalid input", 1, i-1);
        if(idx==i-1) return null;
        return movList.get(i);
    }

    public void listTop5Movies(Cineplex cineplex) {
        try {
            cineplex.listTop5(isAdmin);
        } catch (Exception e) {
            e.getMessage();
        }
    }
    /*
     * Search/ List Movie
     * View Movie details
     * Check seat availability
     * Book and purchase ticket
     * View booking history
     * List top 5 ranking by ticket sales
     */
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
        return this.pw.equals(pw) ? true : false;
    }
    
    public Date getClock() {
        return this.syncClock;
    }

    public void setClock(Date syncClock) {
        this.syncClock = syncClock;
    }

    // public Movie selectMovie(MovieList movList) {
    //     /* 
    //      * Cinema needs a listMovies function, void function that prints list
    //      * of movies, preferrably sorted lexicographically. The movie are 
    //      * numbered when printed, 
    //      */
    //     int choice;
    //     movList.listMovies();
    //     do {
    //         choice= scan.nextInt();
    //     } while (choice > movList.movieCount());
    //     Movie movie= movList.get(choice-1);  // accounts for zero-indexing
    //     return movie;
    // }
    
}