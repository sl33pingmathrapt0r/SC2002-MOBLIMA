package src.ticket;
import java.io.*;
import java.util.*;

public class PriceTable{

    private ClassOfCinema j;
    private Hashtable<ClassOfCinema, 
            Hashtable<AgeGroup, 
            Hashtable<TypeOfMovie, 
            Hashtable<Day,Double> > > > priceTable;

    public PriceTable(){
        
        priceTable = new Hashtable<>();
        for(ClassOfCinema i : ClassOfCinema.values()){
            priceTable.put(i,new Hashtable<>());
            for(AgeGroup j : AgeGroup.values()){
                priceTable.get(i).put(j,new Hashtable<>());
                for(TypeOfMovie k: TypeOfMovie.values()){
                    priceTable.get(i).get(j).put(k,new Hashtable<>());
                    for(Day l: Day.values()){
                        priceTable.get(i).get(j).get(k).put(l,0.0);
                    }
                }
            }
        }
    }

    public static void main(String[] args){

        PriceTable p = new PriceTable();
        for(Object i : p.priceTable.keySet()){
            System.out.println(i);
            for(Object j :p.priceTable.get(i).keySet()){
                System.out.println(j);
                for(Object k:p.priceTable.get(i).get(j).entrySet()){
                    System.out.println(k);
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}