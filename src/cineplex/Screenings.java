package Cineplex;

import java.io.File;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Screenings {
    ArrayList<Date> listofTimings = new ArrayList<Date>();
    public String MovieName;
    final private static String route = Path.of("").toAbsolutePath().toString() + "\\src\\Cineplex\\";
    private final String path;
    int ListingCount=0;

    public Screenings(String MovieName,String cineplex) throws Exception{
        try{    
            this.MovieName=MovieName;
            path=route+cineplex+"\\MovieList\\"+MovieName+".txt";
            File MovFile = new File(path);
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

    void removeTiming(Date date){
        for(int i=ListingCount;i>=0;i--){
            if(date.equals(listofTimings.get(i))){
                listofTimings.remove(i);
                ListingCount--;
                break;
            }
        }
    }

    void removeTiming(int index){
        listofTimings.remove(index);
        ListingCount--;
    }

    int listTiming(){
        System.out.println("List of Timings for "+MovieName+" :\n");
        for(int i=0;i<ListingCount;i++){
            System.out.printf("%d. %s\n", i+1, new SimpleDateFormat("dd MMM yyyy HH:mm").format(listofTimings.get(i)));
        }
        System.out.printf("%d. Exit \n",ListingCount+1);
        return ListingCount;
    }

    public Date showScreening(int index){
        if(index==ListingCount){
            return null;
        }
        else{
            return listofTimings.get(index);
        }
    }

    void listTimingByLine(){
        System.out.print("List of Timings for "+MovieName+" :");
        for(int i=0;i<ListingCount;i++){
            System.out.printf("%d. %s\n", i+1, new SimpleDateFormat("dd MMM yyyy HH:mm").format(listofTimings.get(i)));
        }
        System.out.printf("%d. Exit \n",ListingCount+1);
    }

    void update(Date prevDate, Date newDate){
        for(int i=0;i<ListingCount;i++){
            if(prevDate.equals(listofTimings.get(i))){
                listofTimings.set(i,newDate);
            }
        }
    }
}