
package src.usr;

// import java.util.*;
// import cineplex.*;

abstract class User {
    private boolean isAdmin= false;
    private String username, pw;
    private String name, hp, email;

    // private static Scanner scan= new Scanner(System.in);

    public abstract void banner();

    public abstract void logout();

    public Cineplex selectCineplex(ArrayList<Cineplex> cineplex){
        for(int i=0;i<cineplex.size();i++){
            System.out.println(i+": "+cineplex.get(i).getName());
        }
        int choice=InputHandler.getInt("Enter Cineplex index","Invalid index",0,cineplex.size()--);
        return cineplex.get(i);
    }
    /*
     * Search/ List Movie
     * View Movie details
     * Check seat availability
     * Book and purchase ticket
     * View booking history
     * List top 5 ranking by ticket sales
     */
    
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

    public String getName() {
        return name;
    }

    public String getHp() {
        return hp;
    }

    public String getEmail() {
        return email;
    }

    String getPW() {
        return pw;
    }

    boolean checkPW(String pw) {
        return this.pw.equals(pw) ? true : false;
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