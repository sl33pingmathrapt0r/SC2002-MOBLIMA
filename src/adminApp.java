// package src;

import java.util.*;
import usr.*;
import Cineplex.*;
import movList.*;

public class adminApp {
    final static int MAX_CINEPLEX= 3;
    
    public static Date adminMain(Admin admin) {
        // SETUP
        ArrayList<Cineplex> cineplex= new ArrayList<Cineplex>();
        String cineplexName="AA";
        StringBuilder strBuilder = new StringBuilder(cineplexName);
        MovieList.initMovList();
        for (int i = 0; i < MAX_CINEPLEX; i++) {
            try {
                cineplex.add(new Cineplex(strBuilder.toString()));
            } catch (Exception e) {
                e.getMessage();
            }
            char digit = strBuilder.charAt(1);
            digit++;
            strBuilder.setCharAt(1, digit);
        }
        
        boolean flag= false;
        int intInput;
        while (true) {
            admin.banner();
            int max = 4;
            intInput = inputHandling.getInt("Enter a digit between 1 and "+max+": ", "Invalid input", 1, max);
            switch (intInput) {

                // Create/Update/Remove movie listing
                case 1:
                    System.out.println(
                        "1: Create new movie\n" + 
                        "2: Update movie \n" +
                        "3: Change end date\n" +
                        "4: Exit"
                        );
                    intInput = inputHandling.getInt("Enter a digit between 1 and 4: ","Invalid input",1, 4);
                    
                    if (intInput==4) break;

                    if (intInput == 1) {
                        admin.createMovieListing();
                    } else if (intInput == 2) {
                        Cineplex selectedCineplex = admin.selectCineplex(cineplex); 
                        admin.updateMovieListing(selectedCineplex);
                    } else if (intInput == 3) {
                        Cineplex selectedCineplex = admin.selectCineplex(cineplex); 
                        admin.deleteMovieListing(selectedCineplex);
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
                    intInput = inputHandling.getInt("Enter a digit between 1 and 5: ","Invalid intInput",1, 5);

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

                // Configure System Settings
                case 3:
                    admin.configureSystemSettings();
                    break;
                case 4:
                    System.out.println("Admin Logging Out");
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
        return admin.getClock();   
    }
}