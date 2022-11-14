package src;

import java.util.ArrayList;
import java.util.Date;

import src.cineplex.Cineplex;
import src.movlist.Movie;
import src.movlist.MovieList;
import src.ticket.PriceTable;
import src.usr.Accounts;
import src.usr.Admin;

/**
  Application system for admin users to use their features
  @author Jun Xiong
  @version 1.1
  @since 2022-11-13
 */
public class adminApp {
    /**
     * constant number of cineplexes required by assignment
     */
    final static int MAX_CINEPLEX= 3;

    /**
     * main method to run application interface of moviegoer
     * @param admin user account
     * @return Date clock for setting global application time
     */
    public static Date adminMain(Admin admin){
        // SETUP
        System.out.println(admin.getClock()+"\n");
        ArrayList<Cineplex> cineplex= new ArrayList<Cineplex>();
        Cineplex.setCineplexCount(0);
        String[] cineplexName={"DT","JE","WM"};
        
        PriceTable.initPriceTable();
        MovieList.initmovlist(admin.getClock());
        for (int i = 0; i < MAX_CINEPLEX; i++) {
            try {
                cineplex.add(new Cineplex(cineplexName[i]));
            } catch (Exception e) {
                e.getMessage();
            }
            for(int j=0;j<cineplex.get(i).getMovieCount();j++){
                Movie mov = MovieList.getMovieByTitle(cineplex.get(i).getListOfMovies().get(j));
                if(mov == null ) continue;
                else if(mov.getEndDate().before(admin.getClock())){
                    for(int k=0;k<cineplex.get(i).getNoOfCinema();k++){
                        cineplex.get(i).getCinemas().get(k).deleteMovieName(cineplex.get(i).getListOfMovies().get(j));
                    }
                    try {
                        cineplex.get(i).removeMovieCineplex(cineplex.get(i).getListOfMovies().get(j));
                    } catch (Exception e) {
                        e.printStackTrace();
                    };
                }
            }
            cineplex.get(i).deleteByTime(admin.getClock());
            
        }
        
        boolean flag= false;
        int intInput;
        while (true) {
            admin.banner();
            int max = 5;
            intInput = InputHandling.getInt("Enter a digit between 1 and "+max+": ", "Invalid input", 1, max);
            System.out.println();
            switch (intInput) {

                // Create/Update/Remove movie listing
                case 1:
                    System.out.println(
                        "\n1: Create new movie\n" + 
                        "2: Update movie \n" +
                        "3: Change end date\n" +
                        "4: Exit\n"
                        );
                    intInput = InputHandling.getInt("Enter a digit between 1 and 4: ","Invalid input",1, 4);
                    System.out.println();
                    if (intInput==4) break;

                    if (intInput == 1) {
                        admin.createMovieListing();
                    } else if (intInput == 2) {
                        Cineplex selectedCineplex = admin.selectCineplex(cineplex); 
                        admin.updateMovieListing(selectedCineplex);
                    } else if (intInput == 3) {
                        admin.extendMovieEndDate();
                    } else {
                        System.out.println("Invalid Input");
                    }
                    System.out.println();

                    break;

                // Create/Update/Remove cinema showtimes and the movies to be shown
                case 2:
                    System.out.println(
                        "1: Create cinema showtimes\n" +
                        "2: Update cinema showtimes\n" +
                        "3: Add movie to cineplex\n" +
                        "4: Remove cinema showtimes\n" +
                        "5: Exit"
                        );
                    intInput = InputHandling.getInt("Enter a digit between 1 and 5: ","Invalid intInput",1, 5);
                    System.out.println();
                    if (intInput==5) break;

                    if (intInput == 1) {
                        Cineplex selectedCineplex = admin.selectCineplex(cineplex); 
                        admin.createCinemaShowtimes(selectedCineplex);
                        try {
                            selectedCineplex.writeFile();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else if (intInput == 2) {
                        Cineplex selectedCineplex = admin.selectCineplex(cineplex); 
                        admin.updateCinemaShowtimes(selectedCineplex);
                        try {
                            selectedCineplex.writeFile();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else if (intInput == 3) {
                        Cineplex selectedCineplex = admin.selectCineplex(cineplex); 
                        admin.addMovieToCineplex(selectedCineplex);
                        try {
                            selectedCineplex.writeFile();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else if (intInput == 4) {
                        Cineplex selectedCineplex = admin.selectCineplex(cineplex); 
                        admin.deleteScreening(selectedCineplex);
                        try {
                            selectedCineplex.writeFile();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                    } else {
                        System.out.println("Invalid Input");
                    }
                    break;
                
                // View Top 5 Listings
                case 3: 
                    System.out.println("View Listing in Cineplex");
                    admin.listTop5Movies(admin.selectCineplex(cineplex));
                    break;

                // Configure System Settings
                case 4:
                    admin.configureSystemSettings();
                    break;

                // Logout
                case 5:
                    System.out.println("Admin Logging Out...\n----------------------");
                    flag = true;
                    MovieList.updateFiles();
                    break;
                default:
                    System.out.println("Invalid intInput");
                    break;
            }
            if (flag) break;
        }
        // cineplex store
        for(int i=0;i<Cineplex.getCineplexCount();i++){
            cineplex.get(i).writeFile();
        }
        MovieList.updateFiles();
        Accounts.adminStore();
        return admin.getClock();   
    }
}