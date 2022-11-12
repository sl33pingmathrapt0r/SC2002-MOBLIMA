/**
 * Represents the price table for different days of the week, classes of cinema, type of movies etc
 * Regular, Platinum and Atmos all have nested hashtables 
 * a bigger hashtable to contain them was avoided for slight improvement in readibility
 * @author Pock Xuan
 * @version 1.3
 * @since 2022-11-13
 */
package ticket;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PriceTable {


    /**
     * The current relative path of the program being run in
     */
    private static Path currentRelativePath = Paths.get("");

    /**
     * The absolute path in which is the program being run in
     */
    private static  String absolutePath = currentRelativePath.toAbsolutePath().toString();

    /**
     * The file path in which the regular price table is being stored in
     */
    private static File rPT = new File(absolutePath + "/src/ticket/regularPriceTable");

    /**
     * The file path in which the platinum price table is being stored in
     */
    private static File pPT = new File(absolutePath + "/src/ticket/platinumPriceTable");

    /**
     * The file path in which the atmos price table is being stored in
     */
    private static File aPT = new File(absolutePath + "/src/ticket/atmosPriceTable");

    /**
     * The data structure to store the regular price table
     */
    private static Hashtable<Day, Hashtable<AgeGroup, Hashtable<SeatType,Hashtable<TypeOfMovie, Double>>>> regularPriceTable;
    
    /**
     * The data structure to store the platinum price table
     */
    private static Hashtable<Day, Hashtable<AgeGroup, Hashtable<SeatType,Hashtable<TypeOfMovie, Double>>>> platinumPriceTable;
    
    /**
     * The data structure to store the atmos price table
     */
    private static Hashtable<Day, Hashtable<AgeGroup, Hashtable<SeatType,Hashtable<TypeOfMovie, Double>>>> atmosPriceTable;

    /**
     * Constructor for price table, loads text file into hash tables
     */
    public PriceTable() {

         regularPriceTable = new Hashtable<>();
         platinumPriceTable = new Hashtable<>();
         atmosPriceTable = new Hashtable<>();
         readFile();
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
            e.printStackTrace();
        }
    }

    /**
     * Change price of a ticket
     * @param classOfCinema Regular/ Atmos/ Platinum
     * @param day Mon-Sun/ PH
     * @param ageGroup Student/ Adult/ Senior
     * @param typeOfMovie 3D/ Digital
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

            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to check price given the parameters
     * @param classOfCinema Regular/ Atmos/ Platinum
     * @param day Mon-Sun/ PH
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
    
    /**
     * Saving any updates to the text files
     */
    public static void writeFile() {
        
        saveTable(regularPriceTable, rPT);
        saveTable(platinumPriceTable,  pPT);
        saveTable(atmosPriceTable,  aPT);
    }

    /**
     * Reading text files to create text objects
     */
    public static void readFile() {
        fileToTable(regularPriceTable,  rPT);
        fileToTable(platinumPriceTable,  pPT);
        fileToTable(atmosPriceTable,  aPT);
    }

}