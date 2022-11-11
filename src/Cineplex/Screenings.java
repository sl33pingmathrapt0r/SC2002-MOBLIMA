package Cineplex;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;


public class Screenings {
    public ArrayList<Integer> listofTimings = new ArrayList<Integer>();
    public String MovieName;
    final private static String route = Path.of("").toAbsolutePath().toString();
    private final String path;
    private int ListingCount=0;

    public Screenings(String MovieName) throws Exception{
        try{    
            this.MovieName=MovieName;
            path=route+"\\"+MovieName+".txt";
            File MovFile = new File(path);
            Scanner sc= new Scanner(MovFile);
            while(sc.hasNextLine()){
                listofTimings.get(Integer.valueOf(sc.nextLine()));
                ListingCount++;
            }
            sc.close();
        }
        catch(Exception e){
            throw e;
        }
    }

    public void AddTiming(int startTime,int date){
        int DateandTime=date*10000+startTime,i;
        for(i=0;i<ListingCount;i++){
            if(DateandTime<listofTimings.get(i)){
                listofTimings.add(i,DateandTime);
                break;
            }
        }
        if(ListingCount==0||i==ListingCount){
            listofTimings.add(DateandTime);
        }
        ListingCount++;
    }

    public void RemoveTiming(int startTime, int date){
        int DateandTime=date*10000+startTime;
        for(int i=0;i<ListingCount;i++){
            if(DateandTime==listofTimings.get(i)){
                listofTimings.remove(i);
                ListingCount--;
                break;
            }
        }
    }

    public void ListTiming(){
        System.out.println("List of Timings for "+MovieName+" :\n");
        for(int i=0;i<ListingCount;i++){
            int[] time =new int[5];
            int temp= listofTimings.get(i);
            time[0]=temp%100;
            temp=temp/100;
            time[1]=temp%100;
            temp=temp/100;
            time[2]=temp%100;
            temp=temp/100;
            time[3]=temp%100;
            temp=temp/100;
            time[4]=temp;
            System.out.println(time[2]+"/"+time[3]+"/"+time[4]+" "+time[1]+":"+time[0]);
        }
    }

    public void ListTimingbyLine(){
        for(int i=0;i<ListingCount;i++){
            int[] time =new int[5];
            int temp= listofTimings.get(i);
            time[0]=temp%100;
            temp=temp/100;
            time[1]=temp%100;
            temp=temp/100;
            time[2]=temp%100;
            temp=temp/100;
            time[3]=temp%100;
            temp=temp/100;
            time[4]=temp;
            System.out.print(time[2]+"/"+time[3]+"/"+time[4]+" "+time[1]+":"+time[0]+" ");
        }
    }
}
