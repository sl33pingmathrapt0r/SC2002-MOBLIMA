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
import java.util.Scanner;

import java.util.ArrayList;
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

    public Cineplex(String cineplex,int noOfCinema) throws Exception{
        try{
        boolean b;
        this.noOfCinema=noOfCinema;
        this.cineplex=cineplex;
        this.path=route+"\\"+cineplex;
        File f = new File(path+"\\");
        if (!f.exists()){
            b = f.mkdir();
            File j = new File(path+"\\MovieList");
            b = j.mkdir();
            CineplexCount++;
            for(int i=1;i<=noOfCinema;i++){
                cinemas.add(new Cinema(i,cineplex));
            }
        }
        else{
            File[] movFolder = new File(path+"\\MovieList").listFiles();
            if(movFolder.length>0){
                for(File it : movFolder) listofMovies.add(it.getName().substring(0,it.getName().length()-4));
                for(int i=0;i<listofMovies.size();i++){
                    ScreeningTimes.add(new Screenings(listofMovies.get(i)));
                }
                MovieCount++;
            }
        }
        }
        catch(Exception e){
            System.out.println("File not found");
            throw e;
        }
    }

    public static void CineplexList(){
        if(CineplexCount==0){
            System.out.println("There are no Cineplexes.");
            return;
        }
        System.out.println("There are currently "+CineplexCount+" Cineplexes.");  
    }

    public void AddScreening(int i,String MovieName, int startTime, int date)throws Exception{
        if(MovieList.titleExists(MovieName)){
            Movie mov= new MovieList.getMovieByTitle(MovieName);
            Cinema ScreeningCinema = cinemas.get(i-1);
            ScreeningCinema.AddMovie(mov,startTime,date);
        }
        else{
            Movie mov = MovieList.createMovie();
            Cinema ScreeningCinema = cinemas.get(i-1);
            ScreeningCinema.AddMovie(mov,startTime,date);
        }
        File g = new File(path+"\\MovieList\\"+MovieName+".txt");
        if(!g.exists()) {
            g.createNewFile();
            listofMovies.add(MovieName);
            ScreeningTimes.add(new Screenings(MovieName));
            MovieList.incMovieCounter(MovieName);
            MovieCount++;
        }
        int index=SearchMovies(MovieName);
        ScreeningTimes.get(i).AddTiming(startTime, date);
        
    }

    public int SearchMovies(String MovieName){
        for(int i=0;i<MovieCount;i++){
            if(MovieName==listofMovies.get(i)){
                return i;
            }
        }
        return -1;
    }

    public void ListShowtimebyMovie(String MovieName){
        int index=SearchMovies(MovieName);
        ScreeningTimes.get(index).ListTiming();
    }

    public void ListMoviesbyCinema(int Cinema){
        cinemas.get(Cinema-1).listMovie();
    }

    public void RemoveScreening(int cinema, int Option) throws Exception {
        int startTime=cinemas.get(cinema-1).mlist.get(Option-1).start;
        int date=cinemas.get(cinema-1).mlist.get(Option-1).date;
        String MovieName=cinemas.get(cinema-1).mlist.get(Option-1).movie;
        int index = SearchMovies(MovieName);
        ScreeningTimes.get(index).RemoveTiming(startTime, date);
        cinemas.get(cinema-1).deleteSelect(cinemas.get(cinema-1).mlist.get(Option-1));
    }

    public void ListAllMovies() throws Exception {
        System.out.println("Movies shown at Cineplex"+ cineplex+" is: \n");
        for(int i=0;i<MovieCount;i++){
            System.out.println(listofMovies.get(i));
        }
    }

    public int CinemaFinder(String MovieName, int startTime, int Date){
        for(int i=0;i<noOfCinema;i++){
            if(cinemas.get(i).search(MovieName,startTime,Date)>0){
                return i+1;
            }
        }
        return -1;
    }

    public void ListTimingsbyCineplex(){
        for(int i=0;i<MovieCount;i++){
            System.out.print(listofMovies.get(i)+": ");
            ScreeningTimes.get(i).ListTimingbyLine();
            System.out.print("\n");
        }
    }

    public void ListTopRating() throws Exception{
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

    public void ListTopSales() throws Exception{
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

    public void ListTop5()throws Exception{
        switch(choice){
            case RATINGTOP:
                ListTopRating();break;
            case SALESTOP:
                ListTopSales();break;
            case BOTH:
                ListTopRating();
                ListTopSales();
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

    public static void updateCineplex(){
        for(int i=0;i<CineplexCount;i++){
            File f =new 
        }
    }
}