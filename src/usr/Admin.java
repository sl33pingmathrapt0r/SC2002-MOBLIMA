package usr;

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
        ArrayList <Movie> movieList = cineplex.getMovieList();
        System.out.println("Which movie would you like to update? ");
        for(int i=0;i<movieList.size();i++) System.out.println(i+". " +movieList.get(i).getTitle());
        System.out.println(movieList.size() + ". Exit");
        int choice = InputHandling.getInt("", "Invalid input: ", 0, movieList.size());
        if(choice==movieList.size()) return;
        String title = movieList.get(choice).getTitle();
        MovieList.updateMovieAdmin(title);
        cineplex.updateMovieAdmin(title);
    }

    public void deleteMovieListing(Cineplex cineplex) {
        ArrayList <Movie> movieList = cineplex.getMovieList();
        System.out.println("Which movie would you like to delete? ");
        for(int i=0;i<movieList.size();i++) System.out.println(i+". " +movieList.get(i).getTitle());
        System.out.println(movieList.size() + ". Exit");
        int choice = InputHandling.getInt("", "Invalid input: ", 0, movieList.size());
        if(choice==movieList.size()) return;
        MovieList.setEndDate(movieList.get(x).getTitle());
    }

    public void createCinemaShowtimes() {
        ArrayList<Movie> movieList = MovieList.getMovieList();
        for(int i=0;i<movieList.size();i++){
            System.out.println(i+" " +movieList.get(i));
        }   
        System.out.println("Enter Movie Title to add");
        String title=scan.nextLine();
        if(MovieList.titleExists(title)){

        }
        else{
            String strInput;
            System.out.println("Movie Not in Movie List");
            System.out.println("Create new movie? Y/N");
            do {
                System.out.print("Invalid Input! Please input Y/N: ");
                strInput= scan.nextLine();
            } while (! ( (admin= strInput.charat(0).equalsIgnoreCase("y")) || strInput.charat(0).equalsIgnoreCase("n")) );
            if(strInput.charat(0).equalsIgnoreCase("y")){
                Movie newMovie = MovieList.createMovie(); //adds into master list
                
            }
        }
        
    }

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