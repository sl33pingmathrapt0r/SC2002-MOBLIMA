package src.ticket;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PriceTable {

    private Path currentRelativePath = Paths.get("");
    private String s = currentRelativePath.toAbsolutePath().toString();
    File rPT = new File(s + "/src/ticket/regularPriceTable");
    File pPT = new File(s + "/src/ticket/platinumPriceTable");
    File aPT = new File(s + "/src/ticket/atmosPriceTable");
    private Hashtable<Day, Hashtable<AgeGroup, Hashtable<TypeOfMovie, Double>>> regularPriceTable;
    private Hashtable<Day, Hashtable<AgeGroup, Hashtable<TypeOfMovie, Double>>> platinumPriceTable;
    private Hashtable<Day, Hashtable<AgeGroup, Hashtable<TypeOfMovie, Double>>> atmosPriceTable;

    public PriceTable() {

        this.regularPriceTable = new Hashtable<>();
        this.fileToTable(this.regularPriceTable, this.rPT);

        this.platinumPriceTable = new Hashtable<>();
        this.fileToTable(this.platinumPriceTable, this.pPT);

        this.atmosPriceTable = new Hashtable<>();
        this.fileToTable(this.atmosPriceTable, this.aPT);
    }

    private void fileToTable(Hashtable<Day, Hashtable<AgeGroup, Hashtable<TypeOfMovie, Double>>> table, File file) {
        try {
            Scanner fr = new Scanner(file);
            while (fr.hasNextLine()) {
                for (Day i : Day.values()) {
                    String temp = fr.nextLine();
                    Day day = Day.valueOf(temp);

                    table.put(day, new Hashtable<>());
                    for (AgeGroup j : AgeGroup.values()) {
                        String temp2 = fr.nextLine();
                        AgeGroup age = AgeGroup.valueOf(temp2);

                        table.get(day).put(age, new Hashtable<>());
                        for (TypeOfMovie k : TypeOfMovie.values()) {
                            String temp3 = fr.nextLine();
                            TypeOfMovie type = TypeOfMovie.valueOf(temp3);
                            String l = fr.nextLine();
                            Double price = Double.valueOf(l);
                            fr.nextLine();
                            table.get(day).get(age).put(type, price);

                        }
                    }

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveTable(Hashtable<Day, Hashtable<AgeGroup, Hashtable<TypeOfMovie, Double>>> table, File file){
        try {
            FileWriter fw = new FileWriter(rPT);
            for (Object i : this.regularPriceTable.keySet()) {
                fw.append(i + "\n");
                for (Object j : regularPriceTable.get(i).keySet()) {
                    fw.append(j + "\n");
                    for (Map.Entry<TypeOfMovie, Double> k : regularPriceTable.get(i).get(j).entrySet()) {
                        fw.append(k.getKey() + "\n");
                        fw.append(k.getValue() + "\n");
                        fw.append("\n");
                    }
                }
            }
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean setPrice(ClassOfCinema classOfCinema,Day day, AgeGroup ageGroup, TypeOfMovie typeOfMovie, double price){
        try {
            switch (classOfCinema){
                case REGULAR:
                    this.regularPriceTable.get(day).get(ageGroup).put(typeOfMovie,price);
                    break;
                case PLATINUM:
                    this.platinumPriceTable.get(day).get(ageGroup).put(typeOfMovie,price);
                    break;
                case ATMOS:
                    this.atmosPriceTable.get(day).get(ageGroup).put(typeOfMovie,price);
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
            return true;
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
    }
    public double checkPrice(ClassOfCinema classOfCinema,Day day, AgeGroup ageGroup, TypeOfMovie typeOfMovie){
        double price=0;
        switch (classOfCinema){
            case REGULAR:
                price=this.regularPriceTable.get(day).get(ageGroup).get(typeOfMovie);
                break;
            case PLATINUM:
                price=this.platinumPriceTable.get(day).get(ageGroup).get(typeOfMovie);
                break;
            case ATMOS:
                price=this.atmosPriceTable.get(day).get(ageGroup).get(typeOfMovie);
                break;
            default:
                System.out.println("Invalid option");
                break;
        }
        return price;
    }
    public void saveAllPriceTable() {
        
        saveTable(this.regularPriceTable,this.rPT);
        saveTable(this.platinumPriceTable, this.pPT);
        saveTable(this.atmosPriceTable, this.aPT);
    }

    //for me to test stuff cuz mans lazy to make a test app
    public static void main(String[] args) {

        PriceTable p = new PriceTable();
        System.out.println(p.regularPriceTable);
        System.out.println();
        System.out.println(p.platinumPriceTable);
        System.out.println();
        System.out.println(p.atmosPriceTable);

        /*
         * PriceTable p = new PriceTable();
         * for(Object i : p.regularPriceTable.keySet()){
         *      for(Object j :p.regularPriceTable.get(i).keySet()){
         *          for(Object k:p.regularPriceTable.get(i).get(j).keySet()){
         *              for(Map.Entry<Day,Double> l:p.regularPriceTable.get(i).get(j).get(k).entrySet()){
         *                  System.out.println(i);
         *                  System.out.println(j);
         *                  System.out.println(k);
         *                  System.out.println(l.getKey());
         *                  System.out.println(l.getValue());
         *                  System.out.println();
         *              }
         *          }
         *      }
         * }
         */
    }
}