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

    // private static movList = new MovieList(scan);

    public Admin(String username, String pw) {
        super(true, username, pw);
    }

    @Override
    public void logout() {
        Accounts.adminStore();
    }

    // Overload method
    public void logout(ArrayList<Cineplex> cineplexList) {
        for (Cineplex cineplex : cineplexList)
            try {
                cineplex.writeFile();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        MovieList.updateFiles();
        Accounts.adminStore();
    }


    public void banner() {
        System.out.println("1: Create/Update/Remove movie listing");
        System.out.println("2: Create/Update/Remove cinema showtimes and the movies to be shown");
        System.out.println("3: Configure system settings");
        System.out.println("4: Logout");
    }

    // public Cinema selectCinema(Cineplex cineplex) {
    // /*
    // * Cineplex needs a listCinemas function, void function that prints list
    // * of cinemas, preferrably sorted lexicographically. The cinemas are
    // * numbered when printed. User selects cinema by input choice in integer.
    // */
    // int choice;
    // cineplex.listCinemas();
    // do {
    // int choice= scan.nextInt();
    // } while ( choice > (int) cineplex.cinemaCount());
    // Cinema cinema= Cineplex.get(choice-1); // accounts for zero-indexing
    // return cinema;
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
        ArrayList<String> movieList = cineplex.getListOfMovies();
        System.out.println("Which movie would you like to update? ");
        for (int i = 0; i < movieList.size(); i++)
            System.out.println((i + 1) + ". " + movieList.get(i));
        System.out.println((movieList.size() + 1) + ". Exit");
        System.out.println();
        int choice = inputHandling.getInt("", "Invalid input: ", 1, movieList.size() + 1);
        
        if (choice == movieList.size() + 1)
            return;
        String title = movieList.get(choice - 1);
        MovieList.updateMovieAdmin(title);
    }

    public void deleteMovieListing(Cineplex cineplex) {
        ArrayList<String> movieList = cineplex.getListOfMovies();
        System.out.println("Which movie would you like to delete? ");
        for (int i = 0; i < movieList.size(); i++)
            System.out.println((i + 1) + ". " + movieList.get(i));
        System.out.println((movieList.size() + 1) + ". Exit");
        int choice = inputHandling.getInt("", "Invalid input: ", 1, movieList.size() + 1);
        if (choice == movieList.size() + 1)
            return;
        MovieList.setEndDate(movieList.get(choice-1));
    }

    public void deleteScreening(Cineplex cineplex) {
        
    }

    public void createCinemaShowtimes(Cineplex cineplex) {
        while (true) {
            ArrayList<String> movieList = cineplex.getListOfMovies();
            System.out.println(movieList.size());
            System.out.println("Select movie to add ");
            for (int i = 0; i < movieList.size(); i++)
                System.out.println((i + 1) + ". " + movieList.get(i));
            System.out.println((movieList.size() + 1) + ". Exit");
            int choice = inputHandling.getInt("", "Invalid input: ", 1, movieList.size() + 1);
            if (choice == movieList.size() + 1)
                return;
            String title = movieList.get(choice - 1);
            System.out.println();

            ArrayList<Cinema> cinemaList = cineplex.getCinemas();
            int noCinemas = cineplex.getCinemas().size();
            System.out.printf("Select cinema number (1-%d) \n", noCinemas);
            choice = inputHandling.getInt("", "Invalid number: ", 1, noCinemas);
            if (choice == noCinemas + 1)
                continue;
            System.out.println();

            Date screen = inputHandling.getDate();
            int type = inputHandling.getInt("Enter Type of Movie : \n 1) Digital \n 2) 3D\n", "Invalid Number", 1, 2);
            TypeOfMovie typeOfMovie;
            if (type == 1) {
                typeOfMovie = TypeOfMovie.DIGITAL;
            } else {
                typeOfMovie = TypeOfMovie.D3;
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
        while (true) {
            ArrayList<Movie> movieList = MovieList.getMovieList();
            ArrayList<String> cineplexMovieList = cineplex.getListOfMovies();
            ArrayList<String> validTitles = new ArrayList<String>();
            for (Movie mov : movieList) {
                if (mov.getStatus() == STATUS.END_OF_SHOWING)
                    continue;
                if (cineplexMovieList.contains(mov.getTitle()))
                    continue;
                validTitles.add(mov.getTitle());
            }

            System.out.println("Select movie to add: ");
            for (int i = 0; i < validTitles.size(); i++)
                System.out.println((i + 1) + ". " + validTitles.get(i));
            System.out.println((validTitles.size() + 1) + ". Exit");
            int choice = inputHandling.getInt("", "Invalid input: ", 1, movieList.size() + 1);
            if (choice == validTitles.size()+1)
                return;
            boolean b;
            String title = validTitles.get(choice-1);
            boolean coming_soon = MovieList.getMovieByTitle(title).getStatus() == STATUS.COMING_SOON ? true : false;
            if (coming_soon)
                MovieList.setStatus(title, STATUS.PREVIEW);
            try {
                b = cineplex.addCineplexList(validTitles.get(choice-1));
                if (!b && coming_soon)
                    MovieList.setStatus(title, STATUS.COMING_SOON);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void updateCinemaShowtimes(Cineplex cineplex) {
        while (true) {
            ArrayList<String> movieList = cineplex.getListOfMovies();
            System.out.println("Select movie to update ");
            for (int i = 0; i < movieList.size(); i++)
                System.out.println((i + 1) + ". " + movieList.get(i));
            System.out.println((movieList.size() + 1) + ". Exit");
            int choice = inputHandling.getInt("", "Invalid input: ", 1, movieList.size() + 1);
            if (choice == movieList.size() + 1)
                return;
            String title = movieList.get(choice - 1);

            ArrayList<Date> screenList = cineplex.listOfScreeningByMovie(title);
            System.out.println("Select screening to modify: ");
            for (int i = 0; i < screenList.size(); i++)
                try {
                    System.out.println((i + 1) + ". " +
                            new SimpleDateFormat("dd MMM yyyy HH:mm").format(screenList.get(i)));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            System.out.println((screenList.size() + 1) + ". Return");
            choice = inputHandling.getInt("", "Invalid input: ", 1, screenList.size() + 1);
            if (choice == screenList.size() + 1)
                continue;

            Date newScreen = inputHandling.getDate();
            int idx = cineplex.cinemaFinder(title, screenList.get(choice-1));
            if(idx==-1){
                System.out.println("Error fetching movie");
                return;
            }
            cineplex.updateScreeningShowtime(idx-1,
                    title, screenList.get(choice), newScreen);
        }
    }

    public void configureSystemSettings() {

        System.out.println("1: Change Ticket Pricing");
        System.out.println("2: Edit Public Holiday");
        System.out.println("3: Edit Top 5 Filter");
        System.out.println("4: Exit");
        int choice;
        Scanner sc = new Scanner(System.in);
        choice=inputHandling.getInt("Enter Option","Invalid Option",1,4);
        switch(choice){
            //public static boolean setPrice(ClassOfCinema classOfCinema, Date date, AgeGroup ageGroup, SeatType seatType,TypeOfMovie typeOfMovie, double price
            case 1:
                ClassOfCinema classOfCinema=ClassOfCinema.ATMOS;
                System.out.printf("1: Regular\n2: Atmos\n3: Platinum\n");
                int option = inputHandling.getInt("Enter a number between 1 to 3", "Invalid Index",1,3);
                if(option==1) classOfCinema=ClassOfCinema.REGULAR;
                else if(option==2) classOfCinema=ClassOfCinema.ATMOS;
                else if(option==3) classOfCinema=ClassOfCinema.PLATINUM;
                Date date = inputHandling.getDate();

                AgeGroup ageGroup=AgeGroup.ADULT;
                System.out.printf("1: Adult\n2: Senior\n3: Student\n");
                option = inputHandling.getInt("Enter a number between 1 to 3", "Invalid Index",1,3);
                if(option==1) ageGroup=AgeGroup.ADULT;
                else if(option==2) ageGroup=AgeGroup.SENIOR;
                else if(option==3) ageGroup=AgeGroup.STUDENT;
                SeatType seatType=SeatType.COUPLE;
                System.out.printf("1: Normal\n2: Elite\n3: Couple\n4: Ultima\n");
                option = inputHandling.getInt("Enter a number between 1 to 4", "Invalid Index",1,4);
                if(option==1) seatType=SeatType.NORMAL;
                else if(option==2) seatType=SeatType.ELITE;
                else if(option==3) seatType=SeatType.COUPLE;
                else if(option==4) seatType=SeatType.ULTIMA;

                TypeOfMovie typeOfMovie=TypeOfMovie.D3;
                System.out.printf("1: Digital\n2: 3D\n");
                option = inputHandling.getInt("Enter a number between 1 to 2", "Invalid Index",1,2);
                if(option==1) typeOfMovie=TypeOfMovie.DIGITAL;
                else if(option==2) typeOfMovie=TypeOfMovie.D3;

                double price=PriceTable.checkPrice(classOfCinema, date, ageGroup, seatType, typeOfMovie);
                System.out.println("Current Price: "+price);
                while(true){
                    System.out.print("Enter Price to change to: ");
                    String str = sc.nextLine();
                    try{
                        price = Double.valueOf(str);
                        if(price>0) break;
                    }
                    catch(NumberFormatException e){
                        System.out.printf("Input %s not a valid number. \n", str);
                    }
                }
                PriceTable.setPrice(classOfCinema, date, ageGroup, seatType, typeOfMovie, price);
                break;
            case 2:
            PriceTable.editPH();
            break;
            case 3:
            //WIP
            break;
            case 4:
            System.out.println("Exiting");
            break;
            default:
            System.out.println("Should never reach here");
            break;
        }
    }
}