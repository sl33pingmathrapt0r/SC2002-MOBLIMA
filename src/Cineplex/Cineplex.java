package Cineplex;

import usr.*;
import movList.*;
import ticket.*;
import cinema.*;
import Cineplex.Screenings.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Cineplex {

    final private static String route = Path.of("").toAbsolutePath().toString() + "/src/Cineplex";
    final private String path;
    final private String cineplex;
    private static int CineplexCount = 0;
    private static Choice choice = Choice.BOTH;
    private ArrayList<Cinema> cinemas = new ArrayList<Cinema>();
    private int noOfCinema;
    private ArrayList<String> listofMovies = new ArrayList<String>();
    private ArrayList<Screenings> ScreeningTimes = new ArrayList<Screenings>();
    private int MovieCount = 0;
    public static ArrayList<String> CineplexNames = new ArrayList<String>();

    public Cineplex(String cineplex) throws Exception {
        this.cineplex = cineplex;
        this.path = route + "\\" + cineplex;
    try{
        File f = new File(this.path);
        if (!f.exists()) {
            f.mkdir();
            File j = new File(path + "\\MovieList");
            j.mkdir();
        }
        File[] listofFiles = new File(path).listFiles();
        if (listofFiles.length == 1) {
            for (int i = 1; i <= 3; i++) {
                File MovieCreation = new File(path + "\\" + i);
                MovieCreation.mkdir();
            }
        }
        noOfCinema = listofFiles.length - 1;
        for (File it : listofFiles) {
            if (!it.getName().equals("MovieList")) {
                cinemas.add(new Cinema(Integer.valueOf(it.getName()), cineplex, Integer.valueOf(it.getName()) - 1));
            }
        }
        File[] movFolder = new File(path + "\\MovieList").listFiles();
        if (movFolder.length > 0) {
            for (File it : movFolder)
                listofMovies.add(it.getName().substring(0, it.getName().length() - 4));
            for (int i = 0; i < listofMovies.size(); i++) {
                ScreeningTimes.add(new Screenings(listofMovies.get(i), cineplex));
            }
            MovieCount++;
            }
        } catch (Exception e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
    }

    public static int getCineplexCount() {
        return CineplexCount;
    }

    public int getMovieCount() {
        return MovieCount;
    }

    public static void cineplexList() {
        if (CineplexCount == 0) {
            System.out.println("There are no Cineplexes.");
            return;
        }
        System.out.println("There are currently " + CineplexCount + " Cineplexes.");
    }

    public ArrayList<Cinema> getCinemas() {
        return cinemas;
    }

    public ArrayList<String> getListOfMovies() {
        return listofMovies;
    }

    public String getCineplexName() {
        return cineplex;
    }

    public void addScreening(int cinema, String MovieName, Date showingDate, TypeOfMovie typeOfMovie) throws Exception {
        boolean b = false;
        Movie mov = MovieList.getMovieByTitle(MovieName);
        if (mov.getEndDate().after(showingDate)) {
            b = cinemas.get(cinema - 1).AddMovie(MovieName, showingDate, typeOfMovie);
            if (!b) {
                System.out.println("Movie Screening invalid to add.");
                return;
            }
            int index = searchMovies(MovieName);
            ScreeningTimes.get(index).AddTiming(showingDate);
        } else {
            System.out.println("Screening cannot be added after last date of showing.");
            return;
        }
    }

    public boolean addCineplexList(String MovieName) throws Exception {
        if (searchMovies(MovieName) == -1) {
            File newMov = new File(path + "\\MovieList\\" + MovieName + ".txt");
            newMov.createNewFile();
            listofMovies.add(MovieName);
            ScreeningTimes.add(new Screenings(MovieName, cineplex));
            MovieCount++;
            return true;
        }
        return false;
    }

    private int searchMovies(String MovieName) {
        for (int i = 0; i < listofMovies.size(); i++) {
            if (MovieName == listofMovies.get(i)) {
                return i;
            }
        }
        return -1;
    }

    public Date choiceOfListing(int Argument, String MovieName) {
        int index = searchMovies(MovieName);
        return ScreeningTimes.get(index).showScreening(Argument);
    }

    public int listShowtimeByMovie(String MovieName) {
        int index = searchMovies(MovieName);
        return ScreeningTimes.get(index).listTiming();
    }

    public String listShowtimeByMovie(int index) {
        return listofMovies.get(index);
    }

    public void listMoviesByCinema(int Cinema) {
        cinemas.get(Cinema - 1).listMovie();
    }

    public void removeScreening(int cinema, int Option) throws Exception {
        Date startDate = cinemas.get(cinema - 1).getMlist().get(Option - 1).getStartDate();
        String MovieName = cinemas.get(cinema - 1).getMlist().get(Option - 1).getMovie();
        int index = searchMovies(MovieName);
        ScreeningTimes.get(index).RemoveTiming(startDate);
        cinemas.get(cinema - 1).deleteSelect(Option - 1);
    }

    public void updateScreeningShowtime(int Cinema, String MovieName, Date prevDate, Date newDate) {
        try {
            cinemas.get(Cinema - 1).updateScreening(MovieName, prevDate, newDate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int index = searchMovies(MovieName);
        ScreeningTimes.get(index).update(prevDate, newDate);
    }

    public ArrayList<Date> listOfScreeningByMovie(String MovieName) {
        int index = searchMovies(MovieName);
        return ScreeningTimes.get(index).listofTimings;
    }

    public void removeMovieCineplex(String MovieName) throws Exception {
        if (searchMovies(MovieName) != -1) {
            int index = searchMovies(MovieName);
            listofMovies.remove(index);
            ScreeningTimes.remove(index);
            MovieCount--;
            File f = new File(path + "\\MovieList\\" + MovieName + ".txt");
            f.delete();
        }
    }

    public void listAllMovies() throws Exception {
        System.out.println("Movies shown at Cineplex" + cineplex + " is: \n");
        for (int i = 0; i < MovieCount; i++) {
            System.out.println(i + " " + listofMovies.get(i));
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
        for (int i = 0; i < MovieCount; i++) {
            System.out.print(listofMovies.get(i) + ": ");
            ScreeningTimes.get(i).listTimingByLine();
            System.out.print("\n");
        }
    }

    public void listTopRating() throws Exception {
        String[] MovieListArray;
        File f = new File(path + "\\MovieList");
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
                System.out.println(SortedMovie[i].getTitle() + "\n");
            }
        } else {
            for (int i = 0; i < size; i++) {
                System.out.println(SortedMovie[i].getTitle() + "\n");
            }
        }
    }

    public void listTopSales() throws Exception {
        String[] MovieListArray;
        File f = new File(path + "\\MovieList");
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
                System.out.println(SortedMovie[i].getTitle() + "\n");
            }
        } else {
            for (int i = 0; i < size; i++) {
                System.out.println(SortedMovie[i].getTitle() + "\n");
            }
        }
    }

    public void listTop5() throws Exception {
        switch (choice) {
            case RATINGTOP:
                listTopRating();
                break;
            case SALESTOP:
                listTopSales();
                break;
            case BOTH:
                listTopRating();
                listTopSales();
                break;
        }
    }

    public void setChoice(Choice AdminChoice) {
        choice = AdminChoice;
        return;
    }

    public Choice getChoice(Choice AdminChoice) {
        return choice;
    }

    public void writeFile() {
        try {
            for (int i = 0; i < MovieCount; i++) {
                String movie = listofMovies.get(i);
                FileWriter reset = new FileWriter(path + "\\MovieList\\" + movie + ".txt", false);
                reset.append("");
                reset.close();
                FileWriter w = new FileWriter(path + "\\MovieList\\" + movie + ".txt", true);
                for (int j = 0; j < ScreeningTimes.get(i).ListingCount; j++) {
                    w.append(new SimpleDateFormat("yyyyMMddHHmm").format(ScreeningTimes.get(i).listofTimings.get(j))
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean checkVacancy(int Cinema, int index, String SeatID) {
        int seat = cinemas.get(Cinema - 1).seatConversion(SeatID);
        return cinemas.get(Cinema - 1).getMlist().get(index).getSeats()[seat].getVacancy();
    }

    public boolean updateVacancy(int Cinema, int index, String SeatID) {
        try {
            return cinemas.get(Cinema - 1).updateVacancy(index, SeatID);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public SeatType getTypeofSeat(int Cinema, int index, String SeatID){
        int seat = cinemas.get(Cinema - 1).seatConversion(SeatID);
        return cinemas.get(Cinema-1).getMlist().get(index).getSeats()[seat].getSeatType();
    }

}