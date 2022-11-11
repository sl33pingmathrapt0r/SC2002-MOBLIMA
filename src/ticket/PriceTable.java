package ticket;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PriceTable {

    private Path currentRelativePath = Paths.get("");
    private String absolutePath = currentRelativePath.toAbsolutePath().toString();
    private File rPT = new File(absolutePath + "/src/ticket/regularPriceTable");
    private File pPT = new File(absolutePath + "/src/ticket/platinumPriceTable");
    private File aPT = new File(absolutePath + "/src/ticket/atmosPriceTable");

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

    /**
     * Converting text file to a hashtable for corresponding parameter and price
     * @param table the hashtable
     * @param file text file containing the price
     */
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
    private void saveTable(Hashtable<Day, Hashtable<AgeGroup, Hashtable<TypeOfMovie, Double>>> table, File file){
        try {
            FileWriter fw = new FileWriter(file);
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

    /**
     * Change price of a ticket
     * @param classOfCinema 
     * @param day
     * @param ageGroup
     * @param typeOfMovie
     * @param price new price
     * @return boolean whether ticket has been successfully updated
     */
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
    public double checkPrice(ClassOfCinema classOfCinema,Day day, AgeGroup ageGroup, TypeOfMovie typeOfMovie){
        double price=0;
        switch (classOfCinema){
            case REGULAR:
                price=regularPriceTable.get(day).get(ageGroup).get(typeOfMovie);
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

}