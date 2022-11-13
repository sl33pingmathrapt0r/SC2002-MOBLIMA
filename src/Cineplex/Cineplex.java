package Cineplex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cinema.Cinema;
import movList.inputHandling;
import movList.Movie;
import movList.MovieList;
import ticket.SeatType;
import ticket.TypeOfMovie;

public class Cineplex {

    public static void setCineplexCount(int cineplexCount) {
        cineplexCounter = cineplexCount;
    }

    final private static String ROUTE = Path.of("").toAbsolutePath().toString() + "/src/Cineplex";
    final private String PATH;
    final private String CINEPLEX;
    private static int cineplexCounter = 0;
    private static Choice choice = Choice.BOTH;
    private ArrayList<Cinema> cinemas = new ArrayList<Cinema>();
    private int noOfCinema;
    private ArrayList<String> listOfMovies = new ArrayList<String>();
    private ArrayList<Screenings> screeningTimes = new ArrayList<Screenings>();
    private int movieCount = 0;
    public static ArrayList<String> cineplexNames = new ArrayList<String>();

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
        System.out.println("Cineplex count is "+ cineplexCounter);
        } catch (Exception e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
    }

    public static void cineplexList() {
        if (cineplexCounter == 0) {
            System.out.println("There are no Cineplexes.");
            return;
        }
        System.out.println("There are currently " + cineplexCounter + " Cineplexes.");
    }

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

    private int searchMovies(String MovieName) {
        for (int i = 0; i < listOfMovies.size(); i++) {
            if (MovieName.equals(listOfMovies.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public Date choiceOfListing(int Argument, String MovieName) {
        int index = searchMovies(MovieName);
        return screeningTimes.get(index).showScreening(Argument);
    }

    public int listShowtimeByMovie(String MovieName) {
        int index = searchMovies(MovieName);
        return screeningTimes.get(index).listTiming();
    }

    public String listShowtimeByMovie(int index) {
        return listOfMovies.get(index);
    }

    public void listMoviesByCinema(int Cinema) {
        cinemas.get(Cinema - 1).listMovie();
    }

    public void removeScreening(int cinema, int Option){
        Date startDate = cinemas.get(cinema - 1).getMlist().get(Option - 1).getStartDate();
        String MovieName = cinemas.get(cinema - 1).getMlist().get(Option - 1).getMovie();
        int index = searchMovies(MovieName);
        screeningTimes.get(index).removeTiming(startDate);
        cinemas.get(cinema - 1).deleteSelect(Option - 1);
    }

    public void updateScreeningShowtime(int Cinema, String MovieName, Date prevDate, Date newDate){
        try {
            cinemas.get(Cinema - 1).updateScreening(MovieName, prevDate, newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int index = searchMovies(MovieName);
        screeningTimes.get(index).update(prevDate, newDate);
    }

    public ArrayList<Date> listOfScreeningByMovie(String MovieName) {
        int index = searchMovies(MovieName);
        return screeningTimes.get(index).listofTimings;
    }

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

    public void listAllMovies() throws Exception {
        System.out.println("Movies shown at Cineplex" + CINEPLEX + " is: \n");
        for (int i = 0; i < movieCount; i++) {
            System.out.println(i + " " + listOfMovies.get(i));
        }
    }

    public int cinemaFinder(String MovieName, Date startDate) {
        for (int i = 0; i < noOfCinema; i++) {
            if (cinemas.get(i).search(MovieName, startDate) >= 0) {
                return i + 1;
            }
        }
        return -1;
    }

    public void listTimingsByCineplex() {
        for (int i = 0; i < movieCount; i++) {
            System.out.print(listOfMovies.get(i) + ": ");
            screeningTimes.get(i).listTimingByLine();
            System.out.print("\n");
        }
    }

    public void listTopRating() throws Exception {
        System.out.println();
        String[] MovieListArray;
        File f = new File(PATH + "\\MovieList");
        MovieListArray = f.list();
        int size = MovieListArray.length;
        Movie[] MovieListMovies = new Movie[size], SortedMovie = new Movie[size];
        System.out.println(size);
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
                    int intInput= inputHandling.getInt("Choose option: ", "Please select a valid input: ", 1, 2); 
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
            int intInput= inputHandling.getInt("Choose option: ", "Please select a valid input: ", 1, 2); 
            if (intInput==1) listTopRating();
            else listTopSales();
            listTopSales();

        }
    }

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

    public TypeOfMovie getMovieType(int Cinema, int index){
        return cinemas.get(Cinema-1).getMlist().get(index).getTypeOfMovie();
    }

    public void listVacancy(int Cinema, int index) {
        try {
            cinemas.get(Cinema - 1).listVacancy(index);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getListingCount(Screenings screening) {
        return screening.ListingCount;
    }

    public boolean checkVacancy(int Cinema, int index, String SeatID) {
        int seat = cinemas.get(Cinema - 1).seatConversion(SeatID);
        return cinemas.get(Cinema - 1).getMlist().get(index).getSeats()[seat].getVacancy();
    }

    public boolean updateVacancy(int Cinema, int index, String SeatID) {
        try {
            return cinemas.get(Cinema - 1).updateVacancy(index, SeatID);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public SeatType getTypeofSeat(int Cinema, int index, String SeatID){
        int seat = cinemas.get(Cinema - 1).seatConversion(SeatID);
        return cinemas.get(Cinema-1).getMlist().get(index).getSeats()[seat].getSeatType();
    }

    public String getCineplexName() {
        return CINEPLEX;
    }

    public static int getCineplexCount() {
        return cineplexCounter;
    }

    public static Choice getChoice() {
        return choice;
    }

    public static void setChoice(Choice choice) {
        Cineplex.choice = choice;
    }

    public ArrayList<Cinema> getCinemas() {
        return cinemas;
    }

    public int getNoOfCinema() {
        return noOfCinema;
    }

    public ArrayList<String> getListOfMovies() {
        return listOfMovies;
    }

    public ArrayList<Screenings> getScreeningTimes() {
        return screeningTimes;
    }

    public int getMovieCount() {
        return movieCount;
    }
}