package usr;

import movList.*;
import cinema.*;
import Cineplex.*;
import ticket.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import Cinema.Cinema;

// import MovieList.*;

public class Admin extends User {
    private boolean isAdmin= true;
    private String username, pw;

    private static Scanner scan= new Scanner(System.in);
    // private static movList = new MovieList(scan);

    public void banner(){
        System.out.println("1: Create/Update/Remove movie listing");
        System.out.println("2: Create/Update/Remove cinema showtimes and the movies to be shown");
        System.out.println("3: Configure system settings");
        System.out.println("4: Logout");
    }

    public void logout(ArrayList<Cineplex> cineplexList) {
        for (Cineplex cineplex : cineplexList)
            try {
                cineplex.updateCineplex();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        MovieList.updateFiles();
        Accounts.store();
    }
    
    public Admin(){}
    public Admin(String username, String pw) {
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

    public void createMovieListing() {
        MovieList.createMovie();

    }

    private String getthefuckingmovid(ArrayList<String> movielist, String message) {
        System.out.println(message);
        for(int i=0;i<movielist.size();i++) System.out.printf("%d. %s \n", i+1, movielist.get(i));
        System.out.println((movielist.size()+1) + ". Exit");
        int choice = inputHandling.getInt("", "Invalid input: ", 1, movielist.size()+1);
        if(choice==movielist.size()+1) return null;
        return movielist.get(choice-1);
    }

    public void updateMovieListing(Cineplex cineplex) {
        /**
        * Not implemented under movieList
        * To update a movie, it is necessary to delete
        * the target movie then create another
        * updates one of the 4 things in movie
        */
        String title = getthefuckingmovid(cineplex.getListOfMovies(), "Which movie would you like to update? ");
        if(title==null) return;
        MovieList.updateMovieAdmin(title);
    }

    public void deleteMovieListing(Cineplex cineplex) {
        String title = getthefuckingmovid(cineplex.getListOfMovies(), "Which movie would you like to delete? ");
        if(title==null) return;
        MovieList.setEndDate(MovieList.getMovieByTitle(title));
    }

    public void createCinemaShowtimes(Cineplex cineplex) {
        while(true){            
            String title = getthefuckingmovid(cineplex.getListOfMovies(), "Select movie to add: ");
            if(title==null) return;

            ArrayList<Cinema> cinemaList = cineplex.getCinemas();
            int noCinemas = cineplex.getCinemas().size();
            System.out.printf("Select cinema number (1-%d) \n", noCinemas);
            int choice = inputHandling.getInt("", "Invalid number: ", 1, noCinemas);
            if(choice==noCinemas+1) continue;
            System.out.println();

            Date screen = inputHandling.getDate();
            int type = inputHandling.getInt("Enter Type of Movie : \n 1) Digital \n 2) 3D\n","Invalid Number",1,2);
            TypeOfMovie typeOfMovie;
            if(type ==1){
                typeOfMovie=TypeOfMovie.DIGITAL;
            }
            else{
                typeOfMovie=TypeOfMovie.D3;
            }
            try {
                cineplex.addScreening(choice, title, screen, typeOfMovie);
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
            System.out.println();
        }       
    }
    public void addMovieToCineplex(Cineplex cineplex) {
        while(true){
            ArrayList<Movie> movieList = MovieList.getMovieList();
            ArrayList<String> cineplexMovieList = cineplex.getListOfMovies();
            ArrayList<String> validTitles = new ArrayList<String>();
            for(Movie mov : movieList) {
                if(mov.getStatus()==STATUS.END_OF_SHOWING) continue;
                if(cineplexMovieList.contains(mov.getTitle())) continue;
                validTitles.add(mov.getTitle());
            }
            
            String title = getthefuckingmovid(validTitles, "Select movie to add: ");
            if(title==null) return;

            boolean b;
            boolean coming_soon = MovieList.getMovieByTitle(title).getStatus()==STATUS.COMING_SOON ? true : false;
            if(coming_soon) MovieList.setStatus(title, STATUS.PREVIEW);
            try {
                b=cineplex.addCineplexList(title);
                if(!b && coming_soon) MovieList.setStatus(title, STATUS.COMING_SOON);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void updateCinemaShowtimes(Cineplex cineplex) {
        while(true){
            ArrayList<String> movieList = cineplex.getListOfMovies();
            String title = getthefuckingmovid(movieList, "Select movie to update: ");
            if(title==null) return;

            ArrayList<Date> screenList = cineplex.listOfScreeningByMovie(title);
            System.out.println(screenList.size()+"\n");
            System.out.println("Select screening to modify: ");
            for(int i=0; i<screenList.size(); i++)
                try {
                    System.out.println((i+1)+". "+new SimpleDateFormat("dd MMM yyyy HH:mm").format(screenList.get(i)));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            System.out.println((movieList.size()+1)+". Return");
            int choice = inputHandling.getInt("", "Invalid input: ", 1, movieList.size()+1);
            if(choice==movieList.size()+1) continue;
            
            Date newScreen = inputHandling.getDate();
            cineplex.updateScreeningShowtime(cineplex.cinemaFinder(title, screenList.get(choice-1)),
            title, screenList, newScreen);
        }  
    }

    public void configureSystemSettings() {
        // ticket prices
        // public holiday changes
        // top5list filter enable
    }
}