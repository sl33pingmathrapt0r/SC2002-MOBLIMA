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
import java.text.SimpleDateFormat;
import java.util.*;

import movList.inputHandling;

public class PriceTable {

    /**
     * The current relative path of the program being run in
     */
    private static Path currentRelativePath = Paths.get("");

    /**
     * The absolute path in which is the program being run in
     */
    private static String absolutePath = currentRelativePath.toAbsolutePath().toString();

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
    private static Hashtable<Day, Hashtable<AgeGroup, Hashtable<SeatType, Hashtable<TypeOfMovie, Double>>>> regularPriceTable;

    /**
     * The data structure to store the platinum price table
     */
    private static Hashtable<Day, Hashtable<AgeGroup, Hashtable<SeatType, Hashtable<TypeOfMovie, Double>>>> platinumPriceTable;

    /**
     * The data structure to store the atmos price table
     */
    private static Hashtable<Day, Hashtable<AgeGroup, Hashtable<SeatType, Hashtable<TypeOfMovie, Double>>>> atmosPriceTable;

    private static ArrayList<Date> publicHolidays=new ArrayList<Date>();

    /**
     * Constructor for price table, loads text file into hash tables
     */
    public static void initPriceTable() {

        regularPriceTable = new Hashtable<>();
        platinumPriceTable = new Hashtable<>();
        atmosPriceTable = new Hashtable<>();
        readFile();
        loadPH();
    }

    /**
     * Load all public holidays which is stored in a text file
     * Public holidays are calculated in long due to Date constructor
     */
    private static void loadPH() {
        File file = new File(absolutePath + "/src/ticket/PH.txt");
        Scanner fr;
        try {
            fr = new Scanner(file);
            while (fr.hasNextLine()) {
                PriceTable.publicHolidays.add(new Date(Long.valueOf(fr.nextLine())));
            }
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Saving all public holidays in the list to a text file
     */
    private static void savePH() {
        try {
            File file = new File(absolutePath + "/src/ticket/PH.txt");
            FileWriter fw;
            fw = new FileWriter(file, false);
            for (Date date : publicHolidays) {
                fw.append(Long.valueOf(date.getTime())+ "\n");
            } 
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * A checker to check if a given date is in the list of public holidays
     * 
     * @param date date to be checked
     * @return boolean on whether the date is a public holiday
     */
    public static boolean isPH(Date date) {

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);
        if(PriceTable.publicHolidays.size()==0) return false; //no public holidays
    
        for (Date d : PriceTable.publicHolidays) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(d);
            if (calendarDate.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                    calendarDate.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                    calendarDate.get(Calendar.DATE) == calendar.get(Calendar.DATE)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adding a date into the list of public holidays
     * 
     * @param date date to be added
     * @return boolean on whether the add is successful
     */
    private static boolean addPH(Date date) {

        if (PriceTable.isPH(date)) {
            System.out.println("Date already in list of Public Holidays");
            return false;
        } else {
            PriceTable.publicHolidays.add(date);
            return true;
        }
    }

    /**
     * Removing a date into the list of public holidays
     * 
     * @param date date to be removed
     * @return boolean on whether the removal is successful
     */
    private static boolean removePH(Date date) {

        if (PriceTable.isPH(date)) {
            PriceTable.publicHolidays.remove(date);
            System.out.println("Date successfully removed");
            return true;
        } else {
            System.out.println("Date not in list of public holidays");
            return false;
        }
    }

    /**
     * To add or remove a public holiday
     * Used by admin in configure system settings
     */
    public static void editPH(){
        while(true){
            int choice = inputHandling.getInt("1: Add public holidays\n2: Remove public holidays\n3: Print list of public holidays\n4: Exit\n","Invalid index",1,4);
            System.out.println();
            if(choice==1){
                System.out.println("Enter date to be added");
                Date date = inputHandling.getDateOnly();
                if(PriceTable.addPH(date)) savePH();
            }
            else if(choice==2){
                System.out.println("Enter date to be removed");
                Date date = inputHandling.getDateOnly();
                PriceTable.removePH(date);
                savePH();
            }
            else if(choice==3){
            if(publicHolidays.size()!=0){
                for(Date d : publicHolidays){
                    System.out.println(d);
                }
            }
            else{
                System.out.println("No public holidays");
            }
            }
            else if(choice==4){
                System.out.println("Exiting edit public holiday");
                return;
            }
            else{
                System.out.println("Should never reached here");
            }
            System.out.println();;
        }
    }
    /**
     * Converting text file to a hashtable for corresponding parameter and price
     * 
     * @param table the hashtable
     * @param file  text file containing the price
     */
    private static void fileToTable(
            Hashtable<Day, Hashtable<AgeGroup, Hashtable<SeatType, Hashtable<TypeOfMovie, Double>>>> table, File file) {
        int count=0;
        String r = "";
        try {
            Scanner fr = new Scanner(file);
            while (fr.hasNextLine()) {
                for (Day i : Day.values()) {
                    String temp = fr.nextLine();
                    count++;
                    Day day = Day.valueOf(temp);
                    table.put(day, new Hashtable<>());

                    for (AgeGroup j : AgeGroup.values()) {
                        String temp2 = fr.nextLine();
                        count++;
                        AgeGroup age = AgeGroup.valueOf(temp2);
                        table.get(day).put(age, new Hashtable<>());

                        for (SeatType k : SeatType.values()) {
                            String temp3 = fr.nextLine();
                            count++;
                            SeatType seatType = SeatType.valueOf(temp3);
                            table.get(day).get(age).put(seatType, new Hashtable<>());

                            for (TypeOfMovie l : TypeOfMovie.values()) {
                                String temp4 = fr.nextLine();
                                count++;
                                TypeOfMovie type = TypeOfMovie.valueOf(temp4);
                                String m = fr.nextLine();
                                count++;
                                Double price = Double.valueOf(m);
                                fr.nextLine();
                                r = m;
                                count++;
                                table.get(day).get(age).get(seatType).put(type, price);

                            }
                        }
                    }

                }
            }
            fr.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("count = " + count);
            System.out.println("r = " + r);
            System.out.println(file.getName());
            System.out.println();
            e.printStackTrace();
        }
    }

    /**
     * Saving any updates to a price table
     * 
     * @param table the pricetable
     * @param file  text file
     */
    private static void saveTable(
            Hashtable<Day, Hashtable<AgeGroup, Hashtable<SeatType, Hashtable<TypeOfMovie, Double>>>> table, File file) {
        try {
            FileWriter fw = new FileWriter(file);
            for (Object i : regularPriceTable.keySet()) {
                fw.append(i + "\n");
                for (Object j : regularPriceTable.get(i).keySet()) {
                    fw.append(j + "\n");
                    for (Object k : regularPriceTable.get(i).get(j).keySet()) {
                        fw.append(k + "\n");
                        for (Map.Entry<TypeOfMovie, Double> l : regularPriceTable.get(i).get(j).get(k).entrySet()) {
                            fw.append(l.getKey() + "\n");
                            fw.append(l.getValue() + "\n");
                            fw.append(" \n");
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
     * 
     * @param classOfCinema Regular/ Atmos/ Platinum
     * @param day           Mon-Sun/ PH
     * @param ageGroup      Student/ Adult/ Senior
     * @param typeOfMovie   3D/ Digital
     * @param price         new price
     * @return boolean whether ticket has been successfully updated
     */
    public static boolean setPrice(ClassOfCinema classOfCinema, Date date, AgeGroup ageGroup, SeatType seatType,
            TypeOfMovie typeOfMovie, double price) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int d= calendar.get(Calendar.DAY_OF_WEEK);
        Day day = Day.values()[d];
        return setPrice(classOfCinema, day, ageGroup, seatType, typeOfMovie, price);
    }

    public static boolean setPrice(ClassOfCinema classOfCinema, Day day, AgeGroup ageGroup, SeatType seatType,
            TypeOfMovie typeOfMovie, double price){
                try {
                    switch (classOfCinema) {
                        case REGULAR:
                            regularPriceTable.get(day).get(ageGroup).get(seatType).put(typeOfMovie, price);
                            break;
                        case PLATINUM:
                            platinumPriceTable.get(day).get(ageGroup).get(seatType).put(typeOfMovie, price);
                            break;
                        case ATMOS:
                            atmosPriceTable.get(day).get(ageGroup).get(seatType).put(typeOfMovie, price);
                            break;
                        default:
                            System.out.println("Invalid option");
                            return false;
                    }
                    PriceTable.writeFile();
                    return true;
        
                } catch (Exception e) {
        
                    e.printStackTrace();
                    return false;
                }
            }

    /**
     * Method to check price given the parameters
     * 
     * @param classOfCinema Regular/ Atmos/ Platinum
     * @param day           Mon-Sun/ PH
     * @param ageGroup      Student/ Adult/ Senior
     * @param typeOfMovie   3D/ Digital
     * @return price of the ticket
     */
    public static double checkPrice(ClassOfCinema classOfCinema, Date date, AgeGroup ageGroup, SeatType seatType,
            TypeOfMovie typeOfMovie) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int d= calendar.get(Calendar.DAY_OF_WEEK);
        Day day = Day.values()[d];
        double price = 0;
        switch (classOfCinema) {
            case REGULAR:
                price = regularPriceTable.get(day).get(ageGroup).get(seatType).get(typeOfMovie);
                break;
            case PLATINUM:
                price = platinumPriceTable.get(day).get(ageGroup).get(seatType).get(typeOfMovie);
                break;
            case ATMOS:
                price = atmosPriceTable.get(day).get(ageGroup).get(seatType).get(typeOfMovie);
                break;
            default:
                System.out.println("Invalid option");
                break;
        }
        return price;
    }

    public static double checkPrice(ClassOfCinema classOfCinema, Day day, AgeGroup ageGroup, SeatType seatType,
            TypeOfMovie typeOfMovie) {
      
        double price = 0;
        switch (classOfCinema) {
            case REGULAR:
                price = regularPriceTable.get(day).get(ageGroup).get(seatType).get(typeOfMovie);
                break;
            case PLATINUM:
                price = platinumPriceTable.get(day).get(ageGroup).get(seatType).get(typeOfMovie);
                break;
            case ATMOS:
                price = atmosPriceTable.get(day).get(ageGroup).get(seatType).get(typeOfMovie);
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
        saveTable(platinumPriceTable, pPT);
        saveTable(atmosPriceTable, aPT);
    }

    /**
     * Reading text files to create text objects
     */
    public static void readFile() {
        fileToTable(regularPriceTable, rPT);
        fileToTable(platinumPriceTable, pPT);
        fileToTable(atmosPriceTable, aPT);
    }

}