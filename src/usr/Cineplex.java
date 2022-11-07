package TEST1;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;


public class Cineplex {
  
    private static String path;
    public static String cineplex;
    private static int CineplexCount=0;

    public Cineplex(String Cineplex) throws Exception{
		path = new java.io.File(".").getCanonicalPath();
        boolean b;
        cineplex=Cineplex;
		File f = new File(path+"\\"+cineplex);
		if (!f.exists()){
			b = f.mkdir();
            File j = new File(path+"\\"+cineplex+"\\MovieList");
            System.out.println(path);
            b = j.mkdir();
            CineplexCount++;
        }
	}

    private static void CineplexList(){
        if(CineplexCount==0){
            System.out.println("There are no Cineplexes.");
            return;
        }
        System.out.println("There are currently "+CineplexCount+" Cineplexes.");   
    }


    public static void AddMovie(String MovieName,String Type,String MovieRating) throws Exception {
        if(MovieList.titleExists(MovieName)){
            Movie mov = MovieList.getMovieByTitle(MovieName);
            File g = new File(path+"\\"+cineplex+"\\MovieList\\"+MovieName+".txt");
            if (g.exists()) {
                System.out.println("Movie already exists");
                return;
            }
            FileWriter w = new FileWriter(path+"\\"+cineplex+"\\MovieList\\"+MovieName+".txt",true);
            w.append( Type + "\n" + MovieRating + "\n" );
            w.close();
            MovieList.incMovieCounter(mov);
        }
        else{
            Movie mov = MovieList.createMovie();
            FileWriter w = new FileWriter(path+"\\"+cineplex+"\\MovieList\\"+MovieName+".txt",true);
            w.append( Type + "\n" + MovieRating + "\n" );
            w.close();
            MovieList.incMovieCounter(mov);
        }
	}

    public static void RemoveMovie(String MovieName) throws Exception {
		boolean b;
        File g = new File(path+"\\"+cineplex+"\\MovieList\\"+MovieName+".txt");
		if (g.exists()) {
            b=g.delete();
			System.out.println("Movie has been deleted.");
            Movie mov = MovieList.getMovieByTitle(MovieName);
            MovieList.decMovieCounter(mov);
            return;
		}
    }

    public static void ListAllMovies() throws Exception {
        File[] movFolder = new File(path+"\\"+cineplex+"\\MovieList").listFiles();
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
        File g = new File(path+"\\"+cineplex+"\\MovieList\\"+MovieName+".txt");
		if (!g.exists()) {
			System.out.println("Movie doesn't exists");
            return;
		}
        File MovFile = new File(path+"\\"+cineplex+"\\MovieList\\"+MovieName+".txt");
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

    public static void ListTopRating() throws Exception{
        String [] MovieListArray;
        File f = new File(path+"\\"+cineplex+"\\MovieList");
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
        for(int i=0;i<5;i++){
            System.out.println(SortedMovie[i].getTitle()+"\n");
        }
    }

    public static void ListTopSales() throws Exception{
        String [] MovieListArray;
        File f = new File(path+"\\"+cineplex+"\\MovieList");
        MovieListArray = f.list();
        int size =MovieListArray.length;
        Movie [] MovieListMovies=new Movie[size],SortedMovie=new Movie[size];
        for(int i=0;i<size;i++){
            MovieListArray[i]=MovieListArray[i].substring(0,MovieListArray[i].length()-4);
            MovieListMovies[i]=MovieList.getMovieByTitle(MovieListArray[i]);
        }
        SortedMovie=MovieList.sortBySales(MovieListMovies);
        System.out.println("Top 5 Movies by sales: \n");
        for(int i=0;i<5;i++){
            System.out.println(SortedMovie[i].getTitle()+"\n");
        }
    }
}