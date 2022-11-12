package cinema;

import usr.*;
import movList.*;
import cinema.*;

import cineplex.*; 
import ticket.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


/*
   An instance of a seat specific to an instance of movie screening
 */

public class Seats {
    /*
     * The cinema hall in which this seat belongs to
     */
    int Cinema;
    /*
     * The unique identifier for this seat within the cinema hall
     */
    int seatID;
    /*
     * Type of seat such as normal,couple,etc
     */
    SeatType  seatType;
    /*
     * A boolean variable to indicate whether or not the seat has been booked by a guest
     */
    boolean taken;

    /*
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

    /*
     * A function typically invoked by the movie screening class to generate an array of empty seats
     * @param Cinema The cinema hall that the movie screening would be taking place in
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

    public static Seats[] createP(int Cinema){
        Seats s[] = new Seats[20];
        SeatType seatType = SeatType.NORMAL;
        for (int k=0;k<20;k++){
            if(k<15)
            seatType = SeatType.NORMAL;
            else
            seatType = SeatType.COUPLE;
            s[k] = new Seats(Cinema,k,seatType);
        }
        return s;
    }

    /*
     * A function used to load the pre-existing seat layout for a movie screening on opening the app
     * @param Cinema The cinema hall that this movie screening would be taking place in
     * @param f The text file containing all the relevant seat information
     */
    public static Seats[] create(int Cinema, File f) throws FileNotFoundException{
        Seats s[] = new Seats[80];
        SeatType seatType = SeatType.NORMAL;
        Scanner sc = new Scanner(f);
        String p = "";
        for (int k=0;k<80;k++){
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

    public static Seats[] createP(int Cinema, File f) throws FileNotFoundException{
        Seats s[] = new Seats[20];
        SeatType seatType = SeatType.NORMAL;
        Scanner sc = new Scanner(f);
        String p = "";
        for (int k=0;k<80;k++){
            p = sc.nextLine();
            if(k<15)
            seatType = SeatType.NORMAL;
            else
            seatType = SeatType.COUPLE;
            s[k] = new Seats(Cinema,k,seatType);
            if (p.charAt(0) != 'A')
                s[k].taken = true;
        }
        sc.close();
        return s;
    }

    /*
     * Returns the value of whether seat is taken or not
     */
    public boolean getVacancy(){
        return this.taken;
    }
}