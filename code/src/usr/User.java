package src.usr;

import java.util.ArrayList;
import java.util.Date;

import src.cineplex.Cineplex;
import src.InputHandling;
import src.movlist.Movie;
import src.movlist.MovieList;

/**
  An abstract class User representing a user account, 
  holds common features of both admin and moviegoers
  @author Jun Xiong
  @version 1.3
  @since 2022-11-13
 */
public abstract class User {

    /**
     * boolean stating is user is an admin
     */
    private boolean isAdmin;

    /**
     * String of username for verification
     */
    private String username;
    
    /**
     * String of password for verification
     */
    private String pw;

    /**
     * Internal clock synced with programme
     */
    private Date syncClock=new Date();

    /**
     * Constructor method of User object
     * @param isAdmin updates 
     * @param username username for login
     * @param pw password for login
     */
    public User(boolean isAdmin, String username, String pw) {
        this.isAdmin= isAdmin;
        this.username= username;
        this.pw= pw;
    }

    /**
     * Abstract method for printing main menu page for user
     */
    public abstract void banner();

    /**
     * Abstract method for logout for users to exit programme
     */
    public abstract void logout();

    /**
     * A password checker
     * @param pw String password given by user trying to login
     * @return boolean representing if the provided password is correct
     */
    boolean checkPW(String pw) {
        return this.pw.equals(pw) ? true : false;
    }

    /**
     * Returns a Cineplex as chosen by the user
     * @param cineplex a list of available cineplex location
     * @return a Cineplex object
     */
    public Cineplex selectCineplex(ArrayList<Cineplex>cineplex){
        System.out.println();
        for(int i=0;i<cineplex.size();i++){
            System.out.println(String.valueOf(i+1)+": "+cineplex.get(i).getCineplexName());
        }
        int choice=InputHandling.getInt("Enter Cineplex index: ","Invalid Index",1,cineplex.size()+1);
        System.out.println();
        return cineplex.get(choice-1);
    }

    /**
     * Returns a movie title as chosen by the user
     * @return a String representing movie title
     */
    public String selectMovie() {
        System.out.println();
        ArrayList<Movie> movlist = MovieList.getMovieList();
        int i = 1;
        System.out.println("Select movie: ");
        for(Movie mov : movlist) System.out.printf("%d. %s \n", i++, mov.getTitle());
        int idx = InputHandling.getInt("", "Invalid input", 1, i);
        System.out.println();
        if(idx==i) return null;
        return movlist.get(idx-1).getTitle();
    }

    /**
     * Overload on selectMovie(), to choose movies available in a given cineplex
     * @param cineplex Cineplex location
     * @return a String representing movie title
     */
    public String selectMovie(Cineplex cineplex) {
        System.out.println();
        ArrayList<String> movlist= cineplex.getListOfMovies();
        int i=1;
        System.out.println("Select movie: ");
        for(String mov : movlist) System.out.printf("%d. %s \n", i++, mov);
        int idx = InputHandling.getInt("", "Invalid input", 1, i);
        System.out.println();
        if(idx==i) return null;
        return movlist.get(idx-1);
    }

    /**
     * List the top 5 movies in a cineplex, sorted either by ratings or sales rankings
     * @param cineplex Cineplex location
     */
    public void listTop5Movies(Cineplex cineplex) {
        try {
            cineplex.listTop5(isAdmin);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Checks if user is an admin
     * @return boolean if an admin
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Get username of account
     * @return String of username
     */
    public String getUser() {
        return username;
    }

    /**
     * Get password of account
     * @return String of password
     */
    String getPW() {
        return pw;
    }
    
    /**
     * Get the clock used to sync the users' account with the programme
     * @return Date local clock
     */
    public Date getClock() {
        return this.syncClock;
    }

    /**
     * Set the clock used to sync account with programme
     * @param syncClock global clock
     */
    public void setClock(Date syncClock) {
        this.syncClock = syncClock;
    }
}