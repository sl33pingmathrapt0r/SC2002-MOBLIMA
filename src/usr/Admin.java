package usr;

import java.text.SimpleDateFormat;
import java.util.*;

// import MovieList.*;

class Admin extends User {
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

    public void logout(){

    }

    

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
        ArrayList <Movie> movieList = cineplex.getListOfMovies();
        System.out.println("Which movie would you like to update? ");
        for(int i=0;i<movieList.size();i++) System.out.println((i+1)+". " +movieList.get(i).getTitle());
        System.out.println((movieList.size()+1) + ". Exit");
        int choice = InputHandling.getInt("", "Invalid input: ", 1, movieList.size()+1)-1;
        if(choice==movieList.size()) return;
        String title = movieList.get(choice).getTitle();
        MovieList.updateMovieAdmin(title);
        cineplex.updateMovieAdmin(title);
    }

    public void deleteMovieListing(Cineplex cineplex) {
        ArrayList <Movie> movieList = cineplex.getListOfMovies();
        System.out.println("Which movie would you like to delete? ");
        for(int i=0;i<movieList.size();i++) System.out.println((i+1)+". " +movieList.get(i).getTitle());
        System.out.println((movieList.size()+1) + ". Exit");
        int choice = InputHandling.getInt("", "Invalid input: ", 1, movieList.size()+1)-1;
        if(choice==movieList.size()) return;
        MovieList.setEndDate(movieList.get(x).getTitle());
    }

    public void createCinemaShowtimes(Cineplex cineplex) {
        while(true){
            ArrayList<Movie> movieList = cineplex.getListOfMovies();
            System.out.println("Select movie to add ");
            for(int i=0; i<movieList.size(); i++) System.out.println((i+1)+". "+movieList.get(i).getTitle());
            System.out.println((movieList.size()+1) + ". Exit");
            int choice = InputHandling.getInt("", "Invalid input: ", 1, movieList.size()+1)-1;
            if(choice==movieList.size()) return;
            String title = movieList.get(choice).getTitle();
            System.out.println();

            ArrayList<Cinema> cinemaList = cineplex.getCinemas();
            int noCinemas = cineplex.getCinemas().size();
            System.out.printf("Select cinema number (1-%d) \n", noCinemas);
            choice = InputHandling.getInt("", "Invalid number: ", 1, noCinemas)-1;
            if(choice==movieList.size()) continue;
            System.out.println();

            String screen = new SimpleDateFormat("HHmmyyyyMMdd").format(InputHandler.getDate());
            cineplex.addScreening(choice, title, Integer.valueOf(screen.substring(0, 4)), Integer.valueOf(screen.substring(4))); 
            System.out.println();
        }       
    }

    public void updateCinemaShowtimes(Cineplex cineplex) {
        while(true){
            ArrayList<Movie> movieList = cineplex.getListOfMovies();
            System.out.println("Select movie to update ");
            for(int i=0; i<movieList.size(); i++) System.out.println((i+1)+". "+movieList.get(i).getTitle());
            System.out.println((movieList.size()+1) + ". Exit");
            int choice = InputHandling.getInt("", "Invalid input: ", 1, movieList.size()+1)-1;
            if(choice==movieList.size()) return;
            String title = movieList.get(choice).getTitle();
            System.out.println();

            ArrayList<Integer> screenList = cineplex.listOfScreeningByMovie(title);
            System.out.println("Select screening to modify: ");
            for(int i=0; i<movieList.size(); i++) System.out.println((i+1)+". "+
            new SimpleDateFormat("dd MMM yyyy HH:mm").format(new SimpleDateFormat("yyyyMMddHHmm").parse(String.valueOf(screenList.get(i)))));
            System.out.println((movieList.size()+1)+". Return");
            choice = InputHandler.getInt("", "Invalid input: ", 1, movieList.size()+1)-1;
            if(choice==movieList.size()) continue;
            
            String newScreen = new SimpleDateFormat("HHmmyyyyMMdd").format(InputHandler.getDate());
            cineplex.updateScreeningShowtime(cineplex.cinemaFinder(title, screenList.get(choice)%1000, screenList.get(choice)/1000),
            title, screenList.get(choice)%1000, Integer.valueOf(newScreen.substring(0,5)), screenList.get(choice)/1000, Integer.valueOf(newScreen.substring(5)));
        }  
    }

    public void addMovieToCineplex(Cineplex cineplex) {
        while(true){
            ArrayList<Movie> movieList = MovieList.getMovieList();
            ArrayList<Movie> cineplexMovieList = cineplex.getListOfMovies();
            ArrayList<String> validTitles = new ArrayList<String>();
            for(Movie mov : movieList) if(!cineplexMovieList.contains(mov)) validTitles.add(mov.getTitle());

            System.out.println("Select movie to add: ");
            for(int i=0; i<validTitles.size(); i++) System.out.println((i+1)+". "+validTitles.get(i));
            System.out.println((validTitles.size()+1)+". Exit");
            int choice = InputHandling.getInt("", "Invalid input: ", 1, movieList.size()+1)-1;
            if(choice==validTitles.size()) return;

            cineplex.addCineplexList(validTitles.get(choice));
        }  

    }

    // public void configureSystemSettings() {
    //     // ticket prices
    //     // public holiday changes
    //     // top5list filter enable
    // }
}