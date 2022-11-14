package src.cineplex;

import java.io.File;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 Represents all screening timing for a movie under a cineplex
 @author Tan Jun Hong
 @version 3.1
 @since 2022-11-13
*/

public class Screenings {

    /**
     * List of showtimes
     */
    ArrayList<Date> listofTimings = new ArrayList<Date>();

    /**
     * name of movie that is being aired
     */
    public String MovieName;

    /**
     * Path of directory to cineplex where movie is being aired
     */
    final private static String ROUTE = Path.of("").toAbsolutePath().toString() + "\\src\\Cineplex\\";

    /**
     * Path to screenings directory
     */
    private final String PATH;

    /** 
     * number of screenings 
     */
    int ListingCount=0;

    /**
     * Creates a screening of a movie in the cineplex
     * @param MovieName name of movie that has all its screening here as a String
     * @param cineplex cineplex name that this screening list is under
     */
    public Screenings(String MovieName,String cineplex) throws Exception{
        try{    
            this.MovieName=MovieName;
            PATH=ROUTE+cineplex+"\\MovieList\\"+MovieName+".txt";
            File MovFile = new File(PATH);
            Scanner sc= new Scanner(MovFile);
            while(sc.hasNextLine()){
                if(sc.hasNextLine() == false)
                    break;
                Date date = new SimpleDateFormat("yyyyMMddHHmm").parse(sc.nextLine());
                listofTimings.add(date);
                ListingCount++;
            }
            sc.close();
        }
        catch(Exception e){
            throw e;
        }
    }

    /**
     * add timing into the list of timings
     * @param date Date and time of screening to add.
     */
    void AddTiming(Date date){
        int i=0;
        for(i=0;i<ListingCount;i++){
            if(listofTimings.get(i).after(date)){
                listofTimings.add(i,date);
                break;
            }
        }
        if(ListingCount==0||i==ListingCount){
            listofTimings.add(date);
        }
        ListingCount++;
    }

    /**
     * remove the timing of the screening from the list by date
     * @param date date and time of the Screening to be removed passed in as a Date object.
     */
    void removeTiming(Date date){
        for(int i=ListingCount;i>=0;i--){
            if(date.equals(listofTimings.get(i))){
                listofTimings.remove(i);
                ListingCount--;
                break;
            }
        }
    }

    /**
     * overloaded function to remove the timing of the screening from the list by index
     * @param index index of the list to remove the screening from the arraylisy passed in as an int
     */
    void removeTiming(int index){
        listofTimings.remove(index);
        ListingCount--;
    }

    /**
     * print the timing of everything inside the list in new lines and get the listing count
     * @return the number of screenings for the movie under this cineplex as an int
     */
    int listTiming(){
        System.out.println("List of Timings for "+MovieName+" :\n");
        for(int i=0;i<ListingCount;i++){
            System.out.printf("%d. %s\n", i+1, new SimpleDateFormat("dd MMM yyyy HH:mm").format(listofTimings.get(i)));
        }
        System.out.printf("%d. Exit \n",ListingCount+1);
        return ListingCount;
    }

    /**
     * get the date and timing of the screening in the list by index
     * @param index index in the array list by int
     * @return the date and time at that index in the screening as a Date object
     */
    public Date showScreening(int index){
        if(index==ListingCount){
            return null;
        }
        else{
            return listofTimings.get(index);
        }
    }

    /**
     * print the timing of everything inside the list in a single line
     */
    void listTimingByLine(){
        System.out.print("List of Timings for "+MovieName+" :");
        for(int i=0;i<ListingCount;i++){
            System.out.printf("%d. %s\n", i+1, new SimpleDateFormat("dd MMM yyyy HH:mm").format(listofTimings.get(i)));
        }
        System.out.printf("%d. Exit \n",ListingCount+1);
    }

    /**
     * update the screening in the list by finding its prev date and time and replacing it with a new one
     * @param prevDate date and time of previous screening in a Date object
     * @param newDate date and time of new screeening in a Date object
     */
    void update(Date prevDate, Date newDate){
        for(int i=0;i<ListingCount;i++){
            if(prevDate.equals(listofTimings.get(i))){
                listofTimings.set(i,newDate);
            }
        }
    }
}