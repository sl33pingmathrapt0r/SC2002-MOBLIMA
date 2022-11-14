package src.cineplex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import src.cinema.Cinema;
import src.InputHandling;
import src.movlist.Movie;
import src.movlist.MovieList;
import src.ticket.SeatType;
import src.ticket.TypeOfMovie;

/**
 Represents a cineplex object that that also acts as a handler of the cinema object
 @author Tan Jun Hong
 @version 3.1
 @since 2022-11-13
*/
public class Cineplex {

    /**
     * Path to class directory
     */
    final private static String ROUTE = Path.of("").toAbsolutePath().toString() + "/src/Cineplex";

    /**
     * Path to instance directory
     */
    final private String PATH;

    /**
     * Location of Cineplex, denoted by cineplex code
     */
    final private String CINEPLEX;

    /**
     * Counter on total number of cineplexes
     */
    private static int cineplexCounter = 0;

    /**
     * enum field set by admins on moviegoers' available
     * options in viewing Top 5 Listings
     */
    private static Choice choice = Choice.BOTH;

    /**
     * cinema halls in the cineplex
     */
    private ArrayList<Cinema> cinemas = new ArrayList<Cinema>();

    /**
     * number of cinema halls
     */
    private int noOfCinema;

    /**
     * Movies screening in the cineplex
     */
    private ArrayList<String> listOfMovies = new ArrayList<String>();

    /**
     * Showtimes of screenings in the cineplex
     */
    private ArrayList<Screenings> screeningTimes = new ArrayList<Screenings>();

    /**
     * number of movies aired in cinema
     */
    private int movieCount = 0;

    /**
     * name of all cineplexes
     */
    public static ArrayList<String> cineplexNames = new ArrayList<String>();


    /**
     * Creates a new Cineplex with the given cineplex name.
     * The name should be in a string format when passed in.
     * @param cineplex The Cineplex name.
     */
    public Cineplex(String cineplex) throws Exception {
        this.CINEPLEX = cineplex;
        this.PATH = ROUTE + "\\" + cineplex;
    try{
        File f = new File(this.PATH);
        if (!f.exists()) {
            f.mkdir();
            File j = new File(PATH + "\\MovieList");
            j.mkdir();
        }
        File[] listofFiles = new File(PATH).listFiles();
        if (listofFiles.length == 1) {
            for (int i = 1; i <= 3; i++) {
                File MovieCreation = new File(PATH + "\\" + i);
                MovieCreation.mkdir();
            }
        }
        noOfCinema = listofFiles.length - 1;
        for (File it : listofFiles) {
            if (!it.getName().equals("MovieList")) {
                cinemas.add(new Cinema(Integer.valueOf(it.getName()), cineplex, Integer.valueOf(it.getName()) - 1));
            }
        }
        File[] movFolder = new File(PATH + "\\MovieList").listFiles();
        if (movFolder.length > 0) {
            for (File it : movFolder){
                listOfMovies.add(it.getName().substring(0, it.getName().length() - 4));
                movieCount++;
            }
            for (int i = 0; i < listOfMovies.size(); i++) {
                screeningTimes.add(new Screenings(listOfMovies.get(i), cineplex));
            }
            }
        cineplexCounter++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Prints the number of cineplexs as a static function.
     */
    public static void cineplexList() {
        if (cineplexCounter == 0) {
            System.out.println("There are no Cineplexes.");
            return;
        }
        System.out.println("There are currently " + cineplexCounter + " Cineplexes.");
    }

    /**
     * It creates a screeening showtime after a cineplex has been chosen by main
     * It passes in which cinema to put it in, the movie to screen, the date that it will show and the type of movie it is.
     * Uses cinema class to create a showtime to store in cinema side and it also add showtime on the cineplex side list too.
     * Provides as a checker to check if screening has been added too.
     * @param cinema cinema name passed in as an integer.
     * @param MovieName movie name passed in as a string.
     * @param showingDate date and time of showing to be created as a Date object.
     * @param typeOfMovie Type of movie whether 3D or digital passed in as a TypeOfMovie.
     */
    public void addScreening(int cinema, String MovieName, Date showingDate, TypeOfMovie typeOfMovie) throws Exception {
        boolean b = false;
        Movie mov = MovieList.getMovieByTitle(MovieName);
        if (mov.getEndDate().after(showingDate)) {
            b = cinemas.get(cinema - 1).addMovie(MovieName, showingDate, typeOfMovie);
            if (!b) {
                System.out.println("Movie Screening invalid to add.");
                return;
            }
            int index = searchMovies(MovieName);
            screeningTimes.get(index).AddTiming(showingDate);
        } else {
            System.out.println("Screening cannot be added after last date of showing.");
            return;
        }
    }

    /**
     * Adds movie in cineplex master list for what is currently being screened at the cineplex.
     * Adds to screenings to prepare to take in screening showtime too.
     * @param MovieName Movie name to be added into the cineplex master list for current screening as a string format.
     * @return A boolean of whether adding to the cineplex master list of movies is successful or not.
     */
    public boolean addCineplexList(String MovieName) throws Exception {
        if (searchMovies(MovieName) == -1) {
            File newMov = new File(PATH + "\\MovieList\\" + MovieName + ".txt");
            newMov.createNewFile();
            listOfMovies.add(MovieName);
            screeningTimes.add(new Screenings(MovieName, CINEPLEX));
            movieCount++;
            return true;
        }
        return false;
    }

    /**
     * Serves as an internal searcher for the index of the movie in the ArrayList
     * for movielist as it also serves as the index to search for the screening timing for 
     * the movie in ArrayList of Screening objects.
     * @param MovieName movie name to search for in cineplex movie showing list passed in as a string
     * @return returns index if it is found which is more than 0 if not it will return -1 if it is not inside.
     */
    private int searchMovies(String MovieName) {
        for (int i = 0; i < listOfMovies.size(); i++) {
            if (MovieName.equals(listOfMovies.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Takes in index after listing in admin and the movie name of screening to return the screening date chosen from screening class
     * @param Argument index for screening under the movie passed in as an int
     * @param MovieName movie name of movie to be found for showtime date and time passed in as a string
     * @return date of the screening chosen
     */
    public Date choiceOfListing(int Argument, String MovieName) {
        int index = searchMovies(MovieName);
        return screeningTimes.get(index).showScreening(Argument);
    }

    /**
     * prints all showtime for the movie selected and returns how many showtimes there are 
     * @param MovieName Movie name to find screening under passed in as a String
     * @return The number of showtimes under the cineplex of the movie.
     */
    public int listShowtimeByMovie(String MovieName) {
        int index = searchMovies(MovieName);
        return screeningTimes.get(index).listTiming();
    }

    /**
     * gets the movie chosen by the index.
     * @param index the index of the movie chosen.
     * @return the movie name as a String.
     */
    public String listShowtimeByMovie(int index) {
        return listOfMovies.get(index);
    }

    /**
     * Prints all movies showing at particular cinema by passing in the cinema name and searching the cinema object
     * @param Cinema cinema name passed in as an int to see the movies showing there
     */
    public void listMoviesByCinema(int Cinema) {
        cinemas.get(Cinema - 1).listMovie();
    }

    /**
     * Remove a movie screening in cinema object with cinema name the index in cinema
     * @param cinema cinema name to delete showtime from passed in as an int
     * @param Option index of movie screening in mlist of cinema object
     */
    public void removeScreening(int cinema, int Option){
        
        //Date startDate = cinemas.get(cinema - 1).getMlist().get(Option - 1).getStartDate();
        String MovieName = cinemas.get(cinema - 1).getMlist().get(Option - 1).getMovie();
        int index = searchMovies(MovieName);
        screeningTimes.get(index).removeTiming(index);
        cinemas.get(cinema - 1).deleteSelect(Option - 1);
    }

    /**
     * Update a showtime from a previous date and time to a new date and time
     * @param Cinema cinema name which it will change from passed in as an int
     * @param MovieName moviename of the screening to update passed in as a string
     * @param prevDate date and time of previous screening to be changes passed in as a Date object
     * @param newDate date and time of after screening to be changes passed in as a Date object
     */
    public void updateScreeningShowtime(int Cinema, String MovieName, Date prevDate, Date newDate){
        try {
            cinemas.get(Cinema - 1).updateScreening(MovieName, prevDate, newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int index = searchMovies(MovieName);
        screeningTimes.get(index).update(prevDate, newDate);
    }

    /**
     * function to get all screenings of a movie shown in a cineplex
     * @param MovieName movie name to get all screenings in cineplex for passed in as a String
     * @return returns an ArrayList of Date object which is the date and time of the screenings 
     * for the movie in the cineplex
     */
    public ArrayList<Date> listOfScreeningByMovie(String MovieName) {
        int index = searchMovies(MovieName);
        return screeningTimes.get(index).listofTimings;
    }

    /**
     * remove a movie from the master cineplex list of movie which holds all movies screening at that cineplex
     * @param MovieName movie name of movie to be removed pass in as a String
     */
    public void removeMovieCineplex(String MovieName) throws Exception {
        if (searchMovies(MovieName) != -1) {
            int index = searchMovies(MovieName);
            listOfMovies.remove(index);
            screeningTimes.remove(index);
            movieCount--;
            File f = new File(PATH + "\\MovieList\\" + MovieName + ".txt");
            f.delete();
        }
    }

    /**
     * prints a list of movies being shown at the cineplex.
     */
    public void listAllMovies() throws Exception {
        System.out.println("Movies shown at Cineplex" + CINEPLEX + " is: \n");
        for (int i = 0; i < movieCount; i++) {
            System.out.println(i + " " + listOfMovies.get(i));
        }
    }

    /**
     * find the cinema that is showing in the cineplex for a certain movie and screentime
     * there will not be an overlap with movie showing in other cinemas
     * @param MovieName movie name of movie to find cinema passed in as a String.
     * @param startDate date and time of the screening of the movie to find cinema for passed in as a Date object.
     * @return returns the cinema name as an integer or -1 if screening is not found.
     */
    public int cinemaFinder(String MovieName, Date startDate) {
        for (int i = 0; i < noOfCinema; i++) {
            if (cinemas.get(i).search(MovieName, startDate) >= 0) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * prints all movies and for each movie all timings of showtime that is currently there
     */
    public void listTimingsByCineplex() {
        for (int i = 0; i < movieCount; i++) {
            System.out.print(listOfMovies.get(i) + ": ");
            screeningTimes.get(i).listTimingByLine();
            System.out.print("\n");
        }
    }

    /**
     * print the top 5 movies currently being shown at the cineplex
     */
    public void listTopRating() throws Exception {
        System.out.println();
        String[] MovieListArray;
        File f = new File(PATH + "\\MovieList");
        MovieListArray = f.list();
        int size = MovieListArray.length;
        Movie[] MovieListMovies = new Movie[size], SortedMovie = new Movie[size];
        for (int i = 0; i < size; i++) {
            MovieListArray[i] = MovieListArray[i].substring(0, MovieListArray[i].length() - 4);
            MovieListMovies[i] = MovieList.getMovieByTitle(MovieListArray[i]);
        }
        SortedMovie = MovieList.sortByRating(MovieListMovies);
        System.out.println("Top 5 Movies by rating: \n");
        if (size >= 5) {
            for (int i = 0; i < 5; i++) {
                System.out.println(SortedMovie[i].getTitle());
            }
        } else {
            for (int i = 0; i < size; i++) {
                System.out.println(SortedMovie[i].getTitle());
            }
        }
        System.out.println();
    }

    /**
     * function to settle using date object to remove all screenings before the current date of the system. 
     * @param date current date and time passed in as a Date object.
     */
    public void deleteByTime(Date date){
        for(int i=0;i<noOfCinema;i++){
            cinemas.get(i).delete(date);
        }
        for(int j=0;j<screeningTimes.size();j++){
            int count =getListingCount(screeningTimes.get(j));
            for(int k=count-1;k>=0;k--){
                if(screeningTimes.get(j).showScreening(k).before(date)){
                    screeningTimes.get(j).removeTiming(k);
                }
            }
        }
    }

    /**
     * function to print the top 5 movies by sales that is currently being shown at the cineples.
     */
    public void listTopSales() throws Exception {
        System.out.println();
        String[] MovieListArray;
        File f = new File(PATH + "\\MovieList");
        MovieListArray = f.list();
        int size = MovieListArray.length;
        Movie[] MovieListMovies = new Movie[size], SortedMovie = new Movie[size];
        for (int i = 0; i < size; i++) {
            MovieListArray[i] = MovieListArray[i].substring(0, MovieListArray[i].length() - 4);
            MovieListMovies[i] = MovieList.getMovieByTitle(MovieListArray[i]);
        }
        SortedMovie = MovieList.sortBySales(MovieListMovies);
        System.out.println("Top 5 Movies by sales: \n");
        if (size >= 5) {
            for (int i = 0; i < 5; i++) {
                System.out.println(SortedMovie[i].getTitle());
            }
        } else {
            for (int i = 0; i < size; i++) {
                System.out.println(SortedMovie[i].getTitle());
            }
        }
        System.out.println();
    }

    /**
     * function to choose either to print the top 5 movie by rating, the top 5 movie by sales 
     * or both at the same time.
     * @param admin whether person calling it is a user or admin as a boolean
     */
    public void listTop5(boolean admin) throws Exception{
        if (!admin) {
            switch (choice) {
                case RATINGTOP:
                    listTopRating();
                    break;
                case SALESTOP:
                    listTopSales();
                    break;
                case BOTH:
                    System.out.println(
                    "\nView by\n" +
                    "1. Viewer's Rating\n"+
                    "2. Sales Ranking"
                    );
                    int intInput= InputHandling.getInt("Choose option: ", "Please select a valid input: ", 1, 2); 
                    if (intInput==1) listTopRating();
                    else listTopSales();
                    break;
                }
        } else {
            System.out.println(
                "View by\n" +
                "1. Viewer's Rating\n"+
                "2. Sales Ranking"
                );
            int intInput= InputHandling.getInt("Choose option: ", "Please select a valid input: ", 1, 2); 
            if (intInput==1) listTopRating();
            else listTopSales();

        }
    }

    /**
     * Function to update data currently help in objects and functions back into the database of txtfiles and folders
     */
    public void writeFile() {
        try {
            for (int i = 0; i < movieCount; i++) {
                String movie = listOfMovies.get(i);
                FileWriter reset = new FileWriter(PATH + "\\MovieList\\" + movie + ".txt", false);
                reset.append("");
                reset.close();
                FileWriter w = new FileWriter(PATH + "\\MovieList\\" + movie + ".txt", true);
                for (int j = 0; j < screeningTimes.get(i).ListingCount; j++) {
                    w.append(new SimpleDateFormat("yyyyMMddHHmm").format(screeningTimes.get(i).listofTimings.get(j))
                            + "\n");
                }
                w.close();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Function to return the type of movie whether 3D or digital given the cinema name and the index of the movie in cinema class.
     * @param Cinema cinema name passed as an int to search screening for
     * @param index index of the movie screenings in cinema object passed as an int
     * @return returns the type of movie of the screening required.
     */
    public TypeOfMovie getMovieType(int Cinema, int index){
        return cinemas.get(Cinema-1).getMlist().get(index).getTypeOfMovie();
    }

    /**
     * prints the vacancy of the screening by passing in index of movie screening list in the cinema object with cinema passed in
     * @param Cinema cinema name to check which cinema object to check passed in as an int
     * @param index the index of the movie screening in the cinema object.
     */
    public void listVacancy(int Cinema, int index) {
        try {
            cinemas.get(Cinema - 1).listVacancy(index);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the number of screenings for a movie after passing in its Screening object related to it
     * @param screening Screening object related to the movie
     * @return returns the number of screening for the movie by screenings
     */
    public int getListingCount(Screenings screening) {
        return screening.ListingCount;
    }

    /**
     * check the vacancy of a seat if it is vacant or not
     * @param Cinema cinema name passed in as an integer for the screening
     * @param index index of the movie in the cinema object to find the screening
     * @param SeatID seat name of the seat to find in the movie screening.
     * @return in boolean form if it is vacant it returns true else false.
     */
    public boolean checkVacancy(int Cinema, int index, String SeatID) {
        int seat = cinemas.get(Cinema - 1).seatConversion(SeatID);
        return cinemas.get(Cinema - 1).getMlist().get(index).getSeats()[seat].getVacancy();
    }

    /**
     * Change the vacancy of the seat
     * @param Cinema cinema name passed in as an int to go into the cinema object of
     * @param index get the movie screening by the index in the cinema object passed in as an int
     * @param SeatID seat name of the seat to find in the movie screening. 
     * @return in boolean form if it is updated successfully it is true else false
     */
    public boolean updateVacancy(int Cinema, int index, String SeatID) {
        try {
            return cinemas.get(Cinema - 1).updateVacancy(index, SeatID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * get the seat type of the seat whether couple or etc.
     * @param Cinema cinema name to search in passed in as an int
     * @param index index of the movie screening to find in passed in as an int
     * @param SeatID id of the seat to check for type passed in as a String.
     * @return the seat type of the seat.
     */
    public SeatType getTypeofSeat(int Cinema, int index, String SeatID){
        int seat = cinemas.get(Cinema - 1).seatConversion(SeatID);
        return cinemas.get(Cinema-1).getMlist().get(index).getSeats()[seat].getSeatType();
    }

    /**
     * get the name of the cineplex
     * @return cineplex name as a String
     */
    public String getCineplexName() {
        return CINEPLEX;
    }

    /**
     * Change the total count of cineplexes
     * @param cineplexCount number to change to of cineplex counts passed in as an int
     */
    public static void setCineplexCount(int cineplexCount) {
        cineplexCounter = cineplexCount;
    }

    /**
     * Get the number of cineplexes currently initialized or found in the database
     * @return number of cineplexes currently as an int
     */
    public static int getCineplexCount() {
        return cineplexCounter;
    }

    /**
     * get the choice of to print list by rating or list by sales or both
     * @return the current choice now as a Choice object
     */
    public static Choice getChoice() {
        return choice;
    }

    /**
     * set the choice of whether to print list by rating or list by sales or both
     * @param choice setting the choice of whether to print list by rating or list by sales or both
     */
    public static void setChoice(Choice choice) {
        Cineplex.choice = choice;
    }

    /**
     * get the list of cinemas under the current cineplex object
     * @return the cinemas under the current cineplex as an ArrayList of cinemas
     */
    public ArrayList<Cinema> getCinemas() {
        return cinemas;
    }

    /**
     * gets the number of cinemas held by the cineplex object
     * @return the number of cinemas as an int
     */
    public int getNoOfCinema() {
        return noOfCinema;
    }

    /**
     * gets the list of movies being shown at the cineplex in the cineplex master movie list
     * @return the list of movies currently showing at the cineplex as an ArrayList of String
     */
    public ArrayList<String> getListOfMovies() {
        return listOfMovies;
    }

    /**
     * gets the list of screening times of each movie by index from the listOfMovies
     * @return an array list of screening times of each movie matched by index from the listOfMovies.
     */
    public ArrayList<Screenings> getScreeningTimes() {
        return screeningTimes;
    }

    /**
     * get the number of movies currently being shown at the cineplex.
     * @return the number of movies being currently shown at the cineplex as an int.
     */
    public int getMovieCount() {
        return movieCount;
    }
}