package src.cinema;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import src.ticket.SeatType;


/**
   An instance of a seat specific to a movie screening
 */

public class Seats {
    /**
     * The cinema hall in which this seat belongs to
     */
    int Cinema;
    /**
     * The unique identifier for this seat within the cinema hall
     */
    int seatID;
    /**
     * Type of seat such as normal,couple,etc
     */
    SeatType seatType;
    /**
     * A boolean variable to indicate whether or not the seat has been booked by a guest
     */
    boolean taken;

    /**
     * Creates an instance of a seat class
     * @param c The cinema hall that this seat will be located in.
     * @param ID The unique identifier for this seat
     * @param type The type of seat this is such as normal,couple,etc
     */
    public Seats(int c,int ID, SeatType type){
        this.Cinema = c;
        this.seatID = ID;
        this.seatType = type;
        this.taken = false;
    };

    /**
     * A function typically invoked by the movie screening class to generate an array of empty seats
     * @param Cinema The cinema hall that the movie screening would be taking place in
     * @return the array of empty seats
     */
    public static Seats[] create(int Cinema){
        Seats s[] = new Seats[80];
        SeatType seatType = SeatType.NORMAL;
        for (int k=0;k<80;k++){
            if(k<60)
                seatType = SeatType.NORMAL;
            else if (k<70)
                seatType = SeatType.COUPLE;
            else if (k<75)
                seatType = SeatType.ELITE;
            else
                seatType = SeatType.ULTIMA;
                s[k] = new Seats(Cinema,k,seatType);
        }
        return s;
    }

    /**
     * A function used to load the pre-existing seat layout for a movie screening on opening the app
     * @param Cinema The cinema hall that this movie screening would be taking place in
     * @param f The text file containing all the relevant seat information
     * @return the array of seats
     */
    public static Seats[] create(int Cinema, File f) throws FileNotFoundException{
        Seats s[] = new Seats[80];
        SeatType seatType = SeatType.NORMAL;
        Scanner sc = new Scanner(f);
        String p = "";
        for (int k=0;k<80;k++){
            if(sc.hasNextLine())
            p = sc.nextLine();
            if(k<60)
            seatType = SeatType.NORMAL;
            else if (k<70)
            seatType = SeatType.COUPLE;
            else if (k<75)
            seatType = SeatType.ELITE;
            else
            seatType = SeatType.ULTIMA;
            s[k] = new Seats(Cinema,k,seatType);
            if (p.charAt(0) != 'A')
                s[k].taken = true;
        }
        sc.close();
        return s;
    }

    /**
     * Returns the value of whether seat is taken or not
     * @return truth value of vacancy of seat
     */
    public boolean getVacancy(){
        return this.taken;
    }

    /**
     * Returns the name of the cinema that this seat is located at
     * @return int cinema hall number
     */
    public int getCinema() {
        return this.Cinema;
    }

    /**
     * Sets a new cinema for this seat
     * @param Cinema The new cinema for this seat
     */
    public void setCinema(int Cinema) {
        this.Cinema = Cinema;
    }

    /**
     * Returns the unique seat identifier for this seat
     * @return int of seat ID
     */
    public int getSeatID() {
        return this.seatID;
    }

    /**
     * Sets a new indentifier for this seat
     * @param seatID the new seat identifier
     */
    public void setSeatID(int seatID) {
        this.seatID = seatID;
    }

    /**
     * Returns the type of seat 
     * @return enum of seat type
     */
    public SeatType getSeatType() {
        return this.seatType;
    }

    /**
     * Sets a new seat type for this seat
     * @param seatType enum of seat type
     */
    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    /**
     * Returns whether or not this seat is already taken
     * @return truth value on availability of seat
     */
    public boolean isTaken() {
        return this.taken;
    }

    /**
     * Sets whether or not this seat is currently taken
     * @param taken a boolean parameter to indicate whether the seat will be taken or not
     */
    public void setTaken(boolean taken) {
        this.taken = taken;
    }
}