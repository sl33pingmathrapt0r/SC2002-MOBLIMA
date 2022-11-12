package usr;

import movList.*;
import cinema.*;
import Cineplex.*;
import ticket.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        Accounts.adminStore();
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

    public void updateMovieListing(Cineplex cineplex) {
        /**
        * Not implemented under movieList
        * To update a movie, it is necessary to delete
        * the target movie then create another
        * updates one of the 4 things in movie
        */
        ArrayList <String> movieList = cineplex.getListOfMovies();
        System.out.println("Which movie would you like to update? ");
        for(int i=0;i<movieList.size();i++) System.out.println((i+1)+". " +movieList.get(i));
        System.out.println((movieList.size()+1) + ". Exit");
        int choice = inputHandling.getInt("", "Invalid input: ", 1, movieList.size()+1);
        if(choice==movieList.size()+1) return;
        String title = movieList.get(choice-1);
        MovieList.updateMovieAdmin(title);
    }

    public void deleteMovieListing(Cineplex cineplex) {
        ArrayList <String> movieList = cineplex.getListOfMovies();
        System.out.println("Which movie would you like to delete? ");
        for(int i=0;i<movieList.size();i++) System.out.println((i+1)+". " +movieList.get(i));
        System.out.println((movieList.size()+1) + ". Exit");
        int choice = inputHandling.getInt("", "Invalid input: ", 1, movieList.size()+1);
        if(choice==movieList.size()+1) return;
        //MovieList.setEndDate(MovieList.getMovieByTitle(movieList.get(choice)));
    }

    public void createCinemaShowtimes(Cineplex cineplex) {
        while(true){
            ArrayList<String> movieList = cineplex.getListOfMovies();
            System.out.println("Select movie to add ");
            for(int i=0; i<movieList.size(); i++) System.out.println((i+1)+". "+movieList.get(i));
            System.out.println((movieList.size()+1) + ". Exit");
            int choice = inputHandling.getInt("", "Invalid input: ", 1, movieList.size()+1);
            if(choice==movieList.size()+1) return;
            String title = movieList.get(choice-1);
            System.out.println();

            ArrayList<Cinema> cinemaList = cineplex.getCinemas();
            int noCinemas = cineplex.getCinemas().size();
            System.out.printf("Select cinema number (1-%d) \n", noCinemas);
            choice = inputHandling.getInt("", "Invalid number: ", 1, noCinemas);
            if(choice==noCinemas+1) continue;
            System.out.println();

            String screen = new SimpleDateFormat("HHmmyyyyMMdd").format(inputHandling.getDate());
            int type = inputHandling.getInt("Enter Type of Movie : \n 1) Digital \n 2) 3D\n","Invalid Number",1,2);
            TypeOfMovie typeOfMovie;
            if(type ==1){
                typeOfMovie=TypeOfMovie.DIGITAL;
            }
            else{
                typeOfMovie=TypeOfMovie.D3;
            }
            try {
                cineplex.addScreening(choice, title, Integer.valueOf(screen.substring(0, 4)), Integer.valueOf(screen.substring(4) ), typeOfMovie);
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

            System.out.println("Select movie to add: ");
            for(int i=0; i<validTitles.size(); i++) System.out.println((i+1)+". "+validTitles.get(i));
            System.out.println((validTitles.size()+1)+". Exit");
            int choice = inputHandling.getInt("", "Invalid input: ", 1, movieList.size()+1);
            if(choice==validTitles.size()) return;
            boolean b;
            String title = validTitles.get(choice);
            boolean coming_soon = MovieList.getMovieByTitle(title).getStatus()==STATUS.COMING_SOON ? true : false;
            if(coming_soon) MovieList.setStatus(title, STATUS.PREVIEW);
            try {
                b=cineplex.addCineplexList(validTitles.get(choice));
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
            System.out.println("Select movie to update ");
            for(int i=0; i<movieList.size(); i++) System.out.println((i+1)+". "+movieList.get(i));
            System.out.println((movieList.size()+1) + ". Exit");
            int choice = inputHandling.getInt("", "Invalid input: ", 1, movieList.size()+1);
            if(choice==movieList.size()+1) return;
            String title = movieList.get(choice-1);
            System.out.println();

            ArrayList<Integer> screenList = cineplex.listOfScreeningByMovie(title);
            System.out.println(screenList.size()+"\n");
            System.out.println("Select screening to modify: ");
            for(int i=0; i<screenList.size(); i++)
                try {
                    System.out.println((i+1)+". "+
                    new SimpleDateFormat("dd MMM yyyy HH:mm").format(new SimpleDateFormat("yyyyMMddHHmm").parse(String.valueOf(screenList.get(i)))));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            System.out.println((movieList.size()+1)+". Return");
            choice = inputHandling.getInt("", "Invalid input: ", 1, movieList.size()+1);
            if(choice==movieList.size()+1) continue;
            
            String newScreen = new SimpleDateFormat("HHmmyyyyMMdd").format(inputHandling.getDate());
            cineplex.updateScreeningShowtime(cineplex.cinemaFinder(title, screenList.get(choice-1)%1000, screenList.get(choice-1)/1000),
            title, screenList.get(choice-1)%1000, Integer.valueOf(newScreen.substring(0,5)), screenList.get(choice-1)/1000, Integer.valueOf(newScreen.substring(5)));
        }  
    }

    // public void configureSystemSettings() {
    //     // ticket prices
    //     // public holiday changes
    //     // top5list filter enable
    // }
}