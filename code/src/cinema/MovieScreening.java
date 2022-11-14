package src.cinema;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import src.ticket.TypeOfMovie;

/**
   An instance of a movie screening.
   Typically created within a cinema hall
 */



public class MovieScreening {
    /**
     * Cinema hall that the screening will take place at.
     */
    int Cinema;
    /**
     * The name of the cineplex that this screening will take place at
     */
    String Cineplex;
    /**
     * Date and time of the movie screening.
     */
    Date startDate;
    /**
     * End date and time of the movie screening
     */
    Date endDate;
    /**
     * Title of the movie that will be screened.
     */
    String movie;
    /**
     * An array of seats for this particular movie screening
     */
    Seats seats[];
    /**
     * Shows whether or not the screening is over
     */
    boolean showing=true;
    /**
     * The type of screening that this will be
     */
    TypeOfMovie typeOfMovie;
    /**
     * The path to the text file containing all information regarding the occupancy of this movie screening
     */
    String path = System.getProperty("user.dir");
    /**
     * The date format to be used throughout the class
     */
    DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

    /**
     * Loads an instance of a movie screening.
     * Loads up any relevant information regarding this movie screening if it had been created before.
     * Otherwise create a new text file with an empty cinema
     * @param cinema The cinema hall that the screening will take place at
     * @param startDate The start time and date of the screening
     * @param endDate The end time and date of the screening
     * @param movie The movie title that will be screened
     * @param typeOfMovie The type of movie screening that this will be
     * @param cineplex the type of Cineplex this is
     */

    public MovieScreening(int cinema,Date startDate,Date endDate,String movie,TypeOfMovie typeOfMovie,String cineplex) throws FileNotFoundException{
        this.Cinema = cinema;
        this.Cineplex = cineplex;
        this.startDate = startDate;
        this.endDate = endDate;
        this.movie = movie;
        this.typeOfMovie = typeOfMovie;
        String startString = df.format(startDate);
        String endString = df.format(endDate);
        this.path = System.getProperty("user.dir")+"\\src\\Cineplex\\"+this.Cineplex+"\\"+this.Cinema+"\\"+startString+"@"+endString+"@"+movie+"@"+this.typeOfMovie+"@.txt";
        File f = new File(path);
        if(!f.exists())
            this.seats = Seats.create(cinema);
        else
            this.seats = Seats.create(cinema,f);
    }

    /**
     * Lists the vacancy available for this movie screening
     */
    public void listVacancy(){
        int alphabet = 0;
        if (Cinema != 3){
            System.out.print("   0 1 2 3 4 5 6 7 8 9");
            for(int i=0;i<60;i++){
                if(i%10 == 0){
                    System.out.print("\n"+(char)(alphabet+65)+ "| ");
                    alphabet++;
                }
                if(seats[i].getVacancy() == false)
                    System.out.print("O ");
                else
                    System.out.print("X ");
            }
            System.out.print("\n   0   1   2   3   4");
            for (int i=60;i<70;i++){
                if(i%10 == 0 || i%10 == 5){
                    System.out.print("\n"+(char)(alphabet+65)+ "| ");
                    alphabet++;
                }
                if(seats[i].getVacancy() == false)
                    System.out.print("CCC ");
                else
                System.out.print("XXX ");
            }
            for (int i=70;i<75;i++){
                if(i%10 == 0 || i%10 == 5){
                    System.out.print("\n"+(char)(alphabet+65)+ "| ");
                    alphabet++;
                }
                if(seats[i].getVacancy() == false)
                    System.out.print("UUU ");
                else
                System.out.print("XXX ");
            }
            for (int i=75;i<80;i++){
                if(i%10 == 0 || i%10 == 5){
                    System.out.print("\n"+(char)(alphabet+65)+ "| ");
                    alphabet++;
                }
                if(seats[i].getVacancy() == false)
                    System.out.print("EEE ");
                else
                System.out.print("XXX ");
            }
        }
        else{
            System.out.print("   0 1 2 3 4");
            for(int i=0;i<20;i++){
                if(i%10 == 0 || i%10 == 5){
                    System.out.print("\n"+(char)(alphabet+65)+ "| ");
                    alphabet++;
                }
                if(seats[i].getVacancy() == false){
                    if (i<15)
                        System.out.print("O ");
                    else
                        System.out.print("E ");
                }
                else
                    System.out.print("X ");
            }
        }
    }

    /**
     * Used to change the occupancy of a seat from false to true
     * Typically used when buying tickets
     * @param SeatID The seat that will be booked by the guest
     */
    public void updateVacancy(int SeatID) throws IOException{
        File f = new File(this.path);
        Scanner sc = new Scanner(f);
        StringBuffer buffer = new StringBuffer();
        String s = sc.nextLine();
        int k =0;
        while(sc.hasNextLine()){
            if (k == SeatID)
                buffer.append("XXX "+System.lineSeparator());
            else if (s.charAt(0) != 'A')
                buffer.append("XXX "+System.lineSeparator());
            else
                buffer.append("AAA "+System.lineSeparator());
            if(sc.hasNextLine())
                s = sc.nextLine();
            k++;
        }
        FileWriter wr = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(wr);
        bw.write(buffer.toString());
		bw.close();
		sc.close();
    }

    /**
     * Returns the cinema hall that this screening will be at
     * @return Returns the cinema hall that this screening will be at 
     */
    public int getCinema() {
        return this.Cinema;
    }

    /**
     * Sets a new cinema hall for this screening
     * @param Cinema cinema hall number
     */
    public void setCinema(int Cinema) {
        this.Cinema = Cinema;
    }

    /**
     * Returns the name of this cineplex
     * @return String cineplex name
     */
    public String getCineplex() {
        return this.Cineplex;
    }

    /**
     * Sets a new cineplex for this movie screening
     * @param Cineplex The new cineplex to host this screening
     */
    public void setCineplex(String Cineplex) {
        this.Cineplex = Cineplex;
    }

    /**
     * Returns the start date and time for this screening
     * @return Returns the start date and time for this screening
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * Sets a new date and time for this screening
     * @param startDate The new date and time for this movie screening
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the end date and time of this movie screening
     * @return The end date and time of this movie screening
     */
    public Date getEndDate() {
        return this.endDate;
    }

    /**
     * Sets a new end date and time for this movie screening
     * @param endDate The new end date and time for this movie screening
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns the movie title that will be screened
     * @return The tile of the movie that will be screened
     */
    public String getMovie() {
        return this.movie;
    }

    /**
     * Sets a new movie to be screened
     * @param movie The title of the movie to be screened
     */
    public void setMovie(String movie) {
        this.movie = movie;
    }

    /**
     * Returns the array of seats for this movie screening
     * @return Seat[] of seats
     */
    public Seats[] getSeats() {
        return this.seats;
    }

    /**
     * Assigns a new array of seats for this movie screening
     * @param seats The array of seats for this movie screening
     */
    public void setSeats(Seats seats[]) {
        this.seats = seats;
    }

    /**
     * Returns whether or not the movie is still screening
     * @return True if movie is still showing and false otherwise
     */
    public boolean isShowing() {
        return this.showing;
    }

    /**
     * Returns whether or not the movie is still screening
     * @return True if movie is still screening and false otherwise
     */
    public boolean getShowing() {
        return this.showing;
    }

    /**
     * Sets a new boolean variable to indicate whether the screening is going to happen
     * @param showing The new boolean to indicate state of this screening
     */
    public void setShowing(boolean showing) {
        this.showing = showing;
    }

    /**
     * Returns the type of movie screening that this will be
     * @return Returns the type of movie screening that this will be
     */
    public TypeOfMovie getTypeOfMovie() {
        return this.typeOfMovie;
    }

    /**
     * Sets the new type of movie screening this will be
     * @param typeOfMovie The new type of movie screening
     */
    public void setTypeOfMovie(TypeOfMovie typeOfMovie) {
        this.typeOfMovie = typeOfMovie;
    }

    /**
     * Returns the file directory of this movie screening
     * @return The file directory of this movie screening
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Sets a new directory for this movie screening
     * @param path String directory path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Returns the date format used throughout this class
     * @return the date format used throughout this class
     */
    public DateFormat getDf() {
        return this.df;
    }

    /**
     * Sets a new date format to be used in this class
     * @param df the new date format to be used in this class
     */
    public void setDf(DateFormat df) {
        this.df = df;
    }
    
}