package Cineplex;

import Cinema.*;
import movList.*;
import Cineplex.Screenings.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Cineplex {
 
    final private static String route = Path.of("").toAbsolutePath().toString();
    final private String path;
    final private String cineplex;
    private static int CineplexCount=0;
    private static Choice choice=Choice.BOTH;
    private ArrayList<Cinema> cinemas = new ArrayList<Cinema>();
    private int noOfCinema;
    private ArrayList<String> listofMovies = new ArrayList<String>();
    private ArrayList<Screenings> ScreeningTimes = new ArrayList<Screenings>();
    private int MovieCount=0;
    public static ArrayList<String> CineplexNames = new ArrayList<String>();

    public Cineplex(String cineplex) throws Exception{
        try{
        boolean b;
        this.cineplex=cineplex;
        this.path=route+"\\"+cineplex;
        File f = new File(path+"\\");
        if (!f.exists()){
            b = f.mkdir();
            File j = new File(path+"\\MovieList");
            b = j.mkdir();
        }
        File[] listofFiles = new File(path).listFiles();
        noOfCinema=listofFiles.length-1;
        for(File it:listofFiles){
            if(it.getName()!="MovieList"){
                cinemas.add(new Cinema(Integer.valueOf(it.getName()),cineplex));
            }
        }
        File[] movFolder = new File(path+"\\MovieList").listFiles();
        if(movFolder.length>0){
            for(File it : movFolder) listofMovies.add(it.getName().substring(0,it.getName().length()-4));
            for(int i=0;i<listofMovies.size();i++){
                ScreeningTimes.add(new Screenings(listofMovies.get(i)));
            }
            MovieCount++;
        }
        }
        catch(Exception e){
            System.out.println("File not found");
            throw e;
        }
    }

    public int getMovieCount() {
        return MovieCount;
    }

    public static void cineplexList(){
        if(CineplexCount==0){
            System.out.println("There are no Cineplexes.");
            return;
        }
        System.out.println("There are currently "+CineplexCount+" Cineplexes.");  
    }

    public ArrayList<Cinema> getCinemas() {
        return cinemas;
    }

    public ArrayList<String> getListofMovies() {
        return listofMovies;
    }

    public void addScreening(int cinema,String MovieName, int startTime, int date)throws Exception{
        Movie mov= MovieList.getMovieByTitle(MovieName);
        if(mov.getEndDate().before(new SimpleDateFormat("yyyyMMdd").parse(String.valueOf(date)))){
            if(MovieList.titleExists(MovieName)){
                Cinema ScreeningCinema = cinemas.get(cinema-1);
                ScreeningCinema.AddMovie(mov,startTime,date);
            }
            else{
                MovieList.createMovie();
                Cinema ScreeningCinema = cinemas.get(i-1);
                ScreeningCinema.AddMovie(mov,startTime,date);
            }
            File g = new File(path+"\\MovieList\\"+MovieName+".txt");
            if(!g.exists()) {
                listofMovies.add(MovieName);
                ScreeningTimes.add(new Screenings(MovieName));
                MovieList.incMovieCounter(MovieName);
                MovieCount++;
            }
            int index=searchMovies(MovieName);
            ScreeningTimes.get(cinema).AddTiming(startTime, date);
        }
        else{
            System.out.println("Screening cannot be added after last date of showing.");
            return;
        }
            
    }

    private int searchMovies(String MovieName){
        for(int i=0;i<MovieCount;i++){
            if(MovieName==listofMovies.get(i)){
                return i;
            }
        }
        return -1;
    }

    public void listShowtimebyMovie(String MovieName){
        int index=searchMovies(MovieName);
        ScreeningTimes.get(index).ListTiming();
    }

    public String listShowtimeByMovie(int index){
        return listofMovies.get(index);
    }

    public void listMoviesByCinema(int Cinema){
        cinemas.get(Cinema-1).listMovie();
    }

    public void removeScreening(int cinema, int Option) throws Exception {
        int startTime=cinemas.get(cinema-1).mlist.get(Option-1).start;
        int date=cinemas.get(cinema-1).mlist.get(Option-1).date;
        String MovieName=cinemas.get(cinema-1).mlist.get(Option-1).movie;
        int index = searchMovies(MovieName);
        ScreeningTimes.get(index).RemoveTiming(startTime, date);
        cinemas.get(cinema-1).deleteSelect(cinemas.get(cinema-1).mlist.get(Option-1));
    }

    public void updateScreeningShowtime(int Cinema, String MovieName, int prevStartTime, int afterStartTime, int prevDate, int afterDate){
        cinemas.get(Cinema-1).updateScreening(MovieName, prevStartTime, afterStartTime, prevDate, afterDate);
        int index=searchMovies(MovieName);
        ScreeningTimes.get(index).update(prevStartTime, afterStartTime, prevDate, afterDate);
    }

    public ArrayList<Integer> listOfScreeningByMovie(String MovieName){
        int index=searchMovies(MovieName);
        return ScreeningTimes.get(index).getListofTimings();
    }

    public void removeMovieCineplex(String MovieName)throws Exception{
        if(searchMovies(MovieName)!=-1){
            int index=searchMovies(MovieName);
            listofMovies.remove(index);
            ScreeningTimes.remove(index);
            MovieCount--;
            File f=new File(path+"\\MovieList\\"+MovieName+".txt");
            f.delete();
        }
    }

    public void listAllMovies() throws Exception {
        System.out.println("Movies shown at Cineplex"+ cineplex+" is: \n");
        for(int i=0;i<MovieCount;i++){
            System.out.println(i+" "+listofMovies.get(i));
        }
    }

    public int cinemaFinder(String MovieName, int startTime, int Date){
        for(int i=0;i<noOfCinema;i++){
            if(cinemas.get(i).search(MovieName,startTime,Date)>0){
                return i+1;
            }
        }
        return -1;
    }

    public void listTimingsByCineplex(){
        for(int i=0;i<MovieCount;i++){
            System.out.print(listofMovies.get(i)+": ");
            ScreeningTimes.get(i).ListTimingbyLine();
            System.out.print("\n");
        }
    }

    public void listTopRating() throws Exception{
        String [] MovieListArray;
        File f = new File(path+"\\MovieList");
        MovieListArray = f.list();
        int size =MovieListArray.length;
        Movie [] MovieListMovies=new Movie[size],SortedMovie=new Movie[size];
        System.out.println(size);
        for(int i=0;i<size;i++){
            MovieListArray[i]=MovieListArray[i].substring(0,MovieListArray[i].length()-4);
            MovieListMovies[i]=MovieList.getMovieByTitle(MovieListArray[i]);
        }
        SortedMovie=MovieList.sortByRating(MovieListMovies);
        System.out.println("Top 5 Movies by rating: \n");
        if(size>=5){
            for(int i=0;i<5;i++){
                System.out.println(SortedMovie[i].getTitle()+"\n");
            }
        }
        else{
            for(int i=0;i<size;i++){
                System.out.println(SortedMovie[i].getTitle()+"\n");
            }
        }
    }

    public void listTopSales() throws Exception{
        String [] MovieListArray;
        File f = new File(path+"\\MovieList");
        MovieListArray = f.list();
        int size =MovieListArray.length;
        Movie [] MovieListMovies=new Movie[size],SortedMovie=new Movie[size];
        for(int i=0;i<size;i++){
            MovieListArray[i]=MovieListArray[i].substring(0,MovieListArray[i].length()-4);
            MovieListMovies[i]=MovieList.getMovieByTitle(MovieListArray[i]);
        }
        SortedMovie=MovieList.sortBySales(MovieListMovies);
        System.out.println("Top 5 Movies by sales: \n");
        if(size>=5){
            for(int i=0;i<5;i++){
                System.out.println(SortedMovie[i].getTitle()+"\n");
            }
        }
        else{
            for(int i=0;i<size;i++){
                System.out.println(SortedMovie[i].getTitle()+"\n");
            }
        }
    }

    public void listTop5()throws Exception{
        switch(choice){
            case RATINGTOP:
                listTopRating();break;
            case SALESTOP:
                listTopSales();break;
            case BOTH:
                listTopRating();
                listTopSales();
                break;
        }
    }

    public void setChoice(Choice AdminChoice){
        choice=AdminChoice;
        return;
    }

    public Choice getChoice(Choice AdminChoice){
        return choice;
    }

    public 

}