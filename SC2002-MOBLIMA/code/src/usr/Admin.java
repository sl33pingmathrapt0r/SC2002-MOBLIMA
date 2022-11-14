package src.usr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import src.cineplex.Choice;
import src.cineplex.Cineplex;
import src.cinema.Cinema;
import src.cinema.MovieScreening;
import src.InputHandling;
import src.movlist.Movie;
import src.movlist.MovieList;
import src.movlist.Status;
import src.ticket.AgeGroup;
import src.ticket.ClassOfCinema;
import src.ticket.Day;
import src.ticket.PriceTable;
import src.ticket.SeatType;
import src.ticket.TypeOfMovie;

/**
  This class represents an Admin account, 
  a type of User account, the features 
  Admin can call upon.
  @author Jun Xiong
  @version 1.3
  @since 2022-11-13
 */
public class Admin extends User {

    /**
     * Constructor for an admin account
     * @param username username for login
     * @param pw password for login
     */
    public Admin(String username, String pw) {
        super(true, username, pw);
    }

    /**
     * Overrides User method
     * Logs out the admin and saves all changes made
     */
    @Override
    public void logout() {
        Accounts.adminStore();
    }

    /**
     * Overloads logout() with list of cineplexes accessible in the programme
     * @param cineplexList Lists of cineplexes available for choosing
     */
    public void logout(ArrayList<Cineplex> cineplexList) {
        for (Cineplex cineplex : cineplexList)
            try {
                cineplex.writeFile();
            } catch (Exception e) {
                e.getMessage();
            }
        MovieList.updateFiles();
        Accounts.adminStore();
    }

    /**
     * Prints menu available to admin
     */
    public void banner() {
        System.out.println(
            "1: Create/Update Movie Listing\n" +
            "2: Create/Update/Remove cinema showtimes and the movies to be shown\n" +
            "3: View Top 5 Listings\n" +
            "4: Configure system settings\n" +
            "5: Logout"
            );
    }

    /**
     * Allows admin to add a new movie
     */
    public void createMovieListing() {
        MovieList.createMovie();
    }

    /**
     * Allows admin to update a movie in a Cineplex
     * @param cineplex Cineplex location
     */
    public void updateMovieListing(Cineplex cineplex) {
        ArrayList<String> movieList = cineplex.getListOfMovies();
        System.out.println("Which movie would you like to update? ");
        for (int i = 0; i < movieList.size(); i++)
            System.out.println((i + 1) + ". " + movieList.get(i));
        System.out.println((movieList.size() + 1) + ". Exit");
        System.out.println();
        int choice = InputHandling.getInt("", "Invalid input: ", 1, movieList.size() + 1);
        
        if (choice == movieList.size() + 1)
            return;
        String title = movieList.get(choice - 1);
        MovieList.updateMovieAdmin(title);
    }

    /**
     * Allows admin to extend the end-date of a movie
     */
    public void extendMovieEndDate() {
        ArrayList<Movie> movieList = MovieList.getMovieList();
        System.out.println("Which movie would you like to extend? ");
        for (int i = 0; i < movieList.size(); i++)
            System.out.println((i + 1) + ". " + movieList.get(i).getTitle());
        System.out.println((movieList.size() + 1) + ". Exit");
        int choice = InputHandling.getInt("", "Invalid input: ", 1, movieList.size() + 1);
        if (choice == movieList.size() + 1)
            return;
        MovieList.setEndDate(movieList.get(choice-1).getTitle());
    }

    /**
     * Allows admin to remove a showtime in a Cineplex
     * @param cineplex Cineplex location
     */
    public void deleteScreening(Cineplex cineplex){
        try {
            int cinemaNumber = InputHandling.getInt("Enter Cinema number (1-3): ","Invalid option",1,3);
            System.out.println();
            cineplex.listMoviesByCinema(cinemaNumber);
            Cinema cinema = cineplex.getCinemas().get(cinemaNumber-1);
            if(cinema.getMlist().isEmpty()){
                System.out.println("No screenings available to delete");
                return;
            }
            int i=1;
            for(MovieScreening screen : cinema.getMlist()) System.out.printf("%d. %s \n", i++, new SimpleDateFormat("dd MMM yyyy HH:mm").format(screen.getStartDate()));
            System.out.println(i + ". Exit");
            int deleteChoice = InputHandling.getInt("Select screening to remove: ","Invalid option",1,i);
            System.out.println();
            cineplex.removeScreening(cinemaNumber, deleteChoice);
            System.out.println("Deletion complete");
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Allows admin to add a showtime in a Cineplex
     * @param cineplex Cineplex location
     */
    public void createCinemaShowtimes(Cineplex cineplex) {
        while (true) {
            ArrayList<String> movieList = cineplex.getListOfMovies();
            System.out.println("Select movie to add ");
            for (int i = 0; i < movieList.size(); i++)
                System.out.println((i + 1) + ". " + movieList.get(i));
            System.out.println((movieList.size() + 1) + ". Exit");
            int choice = InputHandling.getInt("", "Invalid input: ", 1, movieList.size() + 1);
            if (choice == movieList.size() + 1)
                return;
            String title = movieList.get(choice - 1);
            System.out.println();

            int noCinemas = cineplex.getCinemas().size();
            System.out.printf("Select cinema number (1-%d) \n", noCinemas);
            choice = InputHandling.getInt("", "Invalid number: ", 1, noCinemas);
            if (choice == noCinemas + 1)
                continue;
            System.out.println();

            Date screen = InputHandling.getDate();
            int type = InputHandling.getInt("Enter Type of Movie : \n 1) Digital \n 2) 3D\n", "Invalid Number", 1, 2);
            TypeOfMovie typeOfMovie;
            if (type == 1) {
                typeOfMovie = TypeOfMovie.DIGITAL;
            } else {
                typeOfMovie = TypeOfMovie.D3;
            }
            try {
                cineplex.addScreening(choice, title, screen, typeOfMovie);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    }

    /**
     * Allows admin to add a movie for screening to a Cineplex
     * @param cineplex Cineplex location
     */
    public void addMovieToCineplex(Cineplex cineplex) {
        while (true) {
            ArrayList<Movie> movieList = MovieList.getMovieList();
            ArrayList<String> cineplexMovieList = cineplex.getListOfMovies();
            ArrayList<String> validTitles = new ArrayList<String>();
            for (Movie mov : movieList) {
                if (mov.getStatus() == Status.END_OF_SHOWING)
                    continue;
                if (cineplexMovieList.contains(mov.getTitle()))
                    continue;
                validTitles.add(mov.getTitle());
            }

            for (int i = 0; i < validTitles.size(); i++)
                System.out.println((i + 1) + ". " + validTitles.get(i));
            System.out.println((validTitles.size() + 1) + ". Exit");
            int choice = InputHandling.getInt("", "Invalid input: ", 1, validTitles.size() + 1);
            System.out.println();
            if (choice == validTitles.size()+1)
                return;
            boolean b;
            String title = validTitles.get(choice-1);
            boolean coming_soon = MovieList.getMovieByTitle(title).getStatus() == Status.COMING_SOON ? true : false;
            if (coming_soon)
                MovieList.setStatus(title, Status.PREVIEW);
            try {
                b = cineplex.addCineplexList(validTitles.get(choice-1));
                if (!b && coming_soon)
                    MovieList.setStatus(title, Status.COMING_SOON);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Allows admin to update a showtime in a Cineplex
     * @param cineplex Cineplex location
     */
    public void updateCinemaShowtimes(Cineplex cineplex) {
        while (true) {
            ArrayList<String> movieList = cineplex.getListOfMovies();
            System.out.println("Select movie to update ");
            for (int i = 0; i < movieList.size(); i++)
                System.out.println((i + 1) + ". " + movieList.get(i));
            System.out.println((movieList.size() + 1) + ". Exit");
            int choice = InputHandling.getInt("", "Invalid input: ", 1, movieList.size() + 1);
            System.out.println();
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
                    e.printStackTrace();
                }
            System.out.println((screenList.size() + 1) + ". Return");
            choice = InputHandling.getInt("", "Invalid input: ", 1, screenList.size() + 1);
            System.out.println();
            if (choice == screenList.size() + 1)
                continue;

            Date newScreen = InputHandling.getDate();
            System.out.println();
            int idx = cineplex.cinemaFinder(title, screenList.get(choice-1));
            if(idx==-1){
                System.out.println("Error fetching movie");
                return;
            }
            cineplex.updateScreeningShowtime(idx,
                    title, screenList.get(choice-1), newScreen);
        }
    }

    /**
     * Allows admin to set how a moviegoer can see the top 5 movies,
     * whether by sales ranking or viewer rating
     */
    private void setListingOption() {
        System.out.println("Current Available User Options:");
        switch (Cineplex.getChoice()) {
          case RATINGTOP: 
            System.out.println("List by Viewer Ratings");
            break;
          case SALESTOP:
            System.out.println("List by Sales Rankings");
            break;
          case BOTH:
            System.out.println("List by Viewer Ratings\nList by Sales Rankings");
            break;
        }
        System.out.println(
          "What will the new options be?\n"+
          "1. List by Viewer Ratings\n" +
          "2. List by Sales Rankings\n" +
          "3. Both options available"
          );
        int intInput= InputHandling.getInt("Choose an option: ", "Please select a valid option: ", 1, 3);
        System.out.println();
        Cineplex.setChoice(Choice.values()[intInput-1]);
      }

    /**
     * Allows admin to configure various system settings
     */
    public void configureSystemSettings() {
        System.out.println(
            "1: Change Ticket Pricing\n" +
            "2: Edit Public Holiday\n" +
            "3: Edit Top 5 Filter\n" +
            "4: Set Clock\n" + 
            "5: Exit"
            );
        int choice;
        Scanner sc = new Scanner(System.in);
        choice=InputHandling.getInt("Enter Option: ","Invalid Option",1,5);
        System.out.println();
        switch(choice){
            //public static boolean setPrice(ClassOfCinema classOfCinema, Date date, AgeGroup ageGroup, SeatType seatType,TypeOfMovie typeOfMovie, double price
            case 1:
                ClassOfCinema classOfCinema=ClassOfCinema.ATMOS;
                System.out.printf("1: Regular\n2: Atmos\n3: Platinum\n");
                int option = InputHandling.getInt("Enter option: ", "Invalid Index",1,3);
                System.out.println();
                if(option==1) classOfCinema=ClassOfCinema.REGULAR;
                else if(option==2) classOfCinema=ClassOfCinema.ATMOS;
                else if(option==3) classOfCinema=ClassOfCinema.PLATINUM;

                int i = 1;
                for(Day str : Day.values()) System.out.printf("%d. %s \n", i++, String.valueOf(str));
                Day day = Day.values()[InputHandling.getInt("Enter option: ", "Invalid Index ", 1, i-1)];
                System.out.println();

                AgeGroup ageGroup=AgeGroup.ADULT;
                System.out.printf("1: Adult\n2: Senior\n3: Student\n");
                option = InputHandling.getInt("Enter option: ", "Invalid Index",1,3);
                System.out.println();
                if(option==1) ageGroup=AgeGroup.ADULT;
                else if(option==2) ageGroup=AgeGroup.SENIOR;
                else if(option==3) ageGroup=AgeGroup.STUDENT;
                
                SeatType seatType=SeatType.COUPLE;
                System.out.printf("1: Normal\n2: Elite\n3: Couple\n4: Ultima\n");
                option = InputHandling.getInt("Enter option: ", "Invalid Index",1,4);
                System.out.println();
                if(option==1) seatType=SeatType.NORMAL;
                else if(option==2) seatType=SeatType.ELITE;
                else if(option==3) seatType=SeatType.COUPLE;
                else if(option==4) seatType=SeatType.ULTIMA;

                TypeOfMovie typeOfMovie=TypeOfMovie.D3;
                System.out.printf("1: Digital\n2: 3D\n");
                option = InputHandling.getInt("Enter option: ", "Invalid Index",1,2);
                System.out.println();
                if(option==1) typeOfMovie=TypeOfMovie.DIGITAL;
                else if(option==2) typeOfMovie=TypeOfMovie.D3;

                double price=PriceTable.checkPrice(classOfCinema, day, ageGroup, seatType, typeOfMovie);
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
                System.out.println();
                PriceTable.setPrice(classOfCinema, day, ageGroup, seatType, typeOfMovie, price);
                break;
            case 2:
            PriceTable.editPH();
            break;
            case 3: 
                setListingOption();
            break;
            case 4:
              SimpleDateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy, HH:mm");
              System.out.println("Programme Clock currently set to " + dateFormat.format(getClock()));
              boolean exit;
              String strInput;
              do {
                System.out.print("Set Clock? Please input Y/N: ");
                strInput= sc.nextLine();
              } while ( !( (exit= strInput.equalsIgnoreCase("n")) || strInput.equalsIgnoreCase("y") ) );
              
              if (exit) break;
              setClock(InputHandling.getDate());
              System.out.println();
              break;
            default:
            break;
        }
    }
}