package ticket;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PriceTable {

    private static Path currentRelativePath = Paths.get("");
    private static  String absolutePath = currentRelativePath.toAbsolutePath().toString();
    private static File rPT = new File(absolutePath + "/src/ticket/regularPriceTable");
    private static File pPT = new File(absolutePath + "/src/ticket/platinumPriceTable");
    private static File aPT = new File(absolutePath + "/src/ticket/atmosPriceTable");

    private static Hashtable<Day, Hashtable<AgeGroup, Hashtable<SeatType,Hashtable<TypeOfMovie, Double>>>> regularPriceTable;
    private static Hashtable<Day, Hashtable<AgeGroup, Hashtable<SeatType,Hashtable<TypeOfMovie, Double>>>> platinumPriceTable;
    private static Hashtable<Day, Hashtable<AgeGroup, Hashtable<SeatType,Hashtable<TypeOfMovie, Double>>>> atmosPriceTable;

    public PriceTable() {

         regularPriceTable = new Hashtable<>();
         fileToTable( regularPriceTable,  rPT);

         platinumPriceTable = new Hashtable<>();
         fileToTable( platinumPriceTable,  pPT);

         atmosPriceTable = new Hashtable<>();
         fileToTable( atmosPriceTable,  aPT);
    }

    /**
     * Converting text file to a hashtable for corresponding parameter and price
     * @param table the hashtable
     * @param file text file containing the price
     */
    private static  void fileToTable( Hashtable<Day, Hashtable<AgeGroup, Hashtable<SeatType,Hashtable<TypeOfMovie, Double>>>> table, File file) {
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

                        for(SeatType k : SeatType.values()){
                            String temp3 = fr.nextLine();
                            SeatType seatType = SeatType.valueOf(temp2);
                            table.get(day).get(age).put(seatType, new Hashtable<>());

                            for (TypeOfMovie l : TypeOfMovie.values()) {
                                String temp4 = fr.nextLine();
                                TypeOfMovie type = TypeOfMovie.valueOf(temp3);
                                String m = fr.nextLine();
                                Double price = Double.valueOf(m);
                                fr.nextLine();
                                table.get(day).get(age).get(seatType).put(type, price);

                            }
                    }
                    }

                }
            }
            fr.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Saving any updates to a price table
     * @param table the pricetable
     * @param file text file 
     */
    private static void saveTable( Hashtable<Day, Hashtable<AgeGroup, Hashtable<SeatType,Hashtable<TypeOfMovie, Double>>>> table, File file){
        try {
            FileWriter fw = new FileWriter(file);
            for (Object i :  regularPriceTable.keySet()) {
                fw.append(i + "\n");
                for (Object j : regularPriceTable.get(i).keySet()) {
                    fw.append(j + "\n");
                    for(Object k:regularPriceTable.get(i).get(j).keySet()){
                        fw.append(j + "\n");
                        for (Map.Entry<TypeOfMovie, Double> l : regularPriceTable.get(i).get(j).get(k).entrySet()) {
                            fw.append(l.getKey() + "\n");
                            fw.append(l.getValue() + "\n");
                            fw.append("\n");
                        }
                    }
                }
            }
            fw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Change price of a ticket
     * @param classOfCinema 
     * @param day
     * @param ageGroup
     * @param typeOfMovie
     * @param price new price
     * @return boolean whether ticket has been successfully updated
     */
    public static boolean setPrice(ClassOfCinema classOfCinema,Day day, AgeGroup ageGroup, SeatType seatType, TypeOfMovie typeOfMovie, double price){
        try {
            switch (classOfCinema){
                case REGULAR:
                     regularPriceTable.get(day).get(ageGroup).get(seatType).put(typeOfMovie,price);
                    break;
                case PLATINUM:
                     platinumPriceTable.get(day).get(ageGroup).get(seatType).put(typeOfMovie,price);
                    break;
                case ATMOS:
                     atmosPriceTable.get(day).get(ageGroup).get(seatType).put(typeOfMovie,price);
                    break;
                default:
                    System.out.println("Invalid option");
                    return false;
            }
            return true;
            
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to check price given the parameters
     * @param classOfCinema Regular/ Atmos/ Platinum
     * @param day Mon-Sum/ PH
     * @param ageGroup Student/ Adult/ Senior
     * @param typeOfMovie 3D/ Digital
     * @return price of the ticket
     */
    public static double checkPrice(ClassOfCinema classOfCinema,Day day, AgeGroup ageGroup, SeatType seatType, TypeOfMovie typeOfMovie){
        double price=0;
        switch (classOfCinema){
            case REGULAR:
                price=regularPriceTable.get(day).get(ageGroup).get(seatType).get(typeOfMovie);
                break;
            case PLATINUM:
                price= platinumPriceTable.get(day).get(ageGroup).get(seatType).get(typeOfMovie);
                break;
            case ATMOS:
                price= atmosPriceTable.get(day).get(ageGroup).get(seatType).get(typeOfMovie);
                break;
            default:
                System.out.println("Invalid option");
                break;
        }
        return price;
    }
    public static void saveAllPriceTable() {
        
        saveTable( regularPriceTable, rPT);
        saveTable( platinumPriceTable,  pPT);
        saveTable( atmosPriceTable,  aPT);
    }

}