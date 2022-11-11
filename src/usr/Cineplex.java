package TEST2;
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
    private static ArrayList<Movie> cinemaList;
    private ArrayList<Cinema> cinemas = new ArrayList<Cinema>();
    private int noOfCinema;


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

    public void AddScreening(int i,Movie MovieName, int startTime, int date){
        Cinema ScreeningCinema = cinemas.get(i-1);
        if(MovieList.titleExists(MovieName)){
            ScreeningCinema.AddMovie(MovieName,startTime.date);
        }
        else{
            MovieList.Add
        }
    }


    public void AddMovie(String MovieName) throws Exception {
        if(MovieList.titleExists(MovieName)){
            File g = new File(path+"\\MovieList\\"+MovieName+".txt");
            if (g.exists()) {
                System.out.println("Movie already exists");
                return;
            }
            MovieList.incMovieCounter(MovieName);
        }
        else{
            FileWriter w = new FileWriter(path+"\\MovieList\\"+MovieName+".txt",true);
            MovieList.incMovieCounter(MovieName);
        }
    }

    public void RemoveMovie(String MovieName) throws Exception {
        boolean b;
        File g = new File(path+"\\MovieList\\"+MovieName+".txt");
        if (g.exists()) {
            b=g.delete();
            System.out.println("Movie has been deleted.");
            MovieList.decMovieCounter(MovieName);
            return;
        }
    }

    public void ListAllMovies() throws Exception {
        File[] movFolder = new File(path+"\\MovieList").listFiles();
        if(movFolder.length==0){
            System.out.println("There are currently no movies.");
        }
        else{
            System.out.println("List of movies showing in "+cineplex+":");
            for(File it : movFolder) System.out.println(it.getName().substring(0,it.getName().length()-4));
        }
    }

    public void ListTimings(String MovieName) throws Exception {
        String ShowTimesDate="";
        String ShowTimesLine="";
        String[] ShowTimes=null;
        File g = new File(path+"\\MovieList\\"+MovieName+".txt");
        if (!g.exists()) {
            System.out.println("Movie doesn't exists");
            return;
        }
        File MovFile = new File(path+"\\MovieList\\"+MovieName+".txt");
        FileReader fileReader = new FileReader(MovFile);
        BufferedReader buffReader= new BufferedReader(fileReader);
        for(int i=0;i<3;i++){
            ShowTimesDate=buffReader.readLine();
        }
        while(ShowTimesDate.indexOf("/")!=-1){
            ShowTimesLine=buffReader.readLine();
            ShowTimes=ShowTimesLine.split(",");
            for(String Showtime:ShowTimes) System.out.println(ShowTimesDate+" "+Showtime+"\n");
            ShowTimesDate=buffReader.readLine();
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
}