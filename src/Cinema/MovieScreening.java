package Cinema;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/*
   An instance of a movie screening.
   Typically created within a cinema hall
 */

public class MovieScreening {
    /*
     * Cinema hall that the screening will take place at.
     */
    int Cinema;
    /*
     * Date of the movie screening.
     */
    int date;
    /*
     * Starting time of the movie screening.
     */
    int start;
    /*
     * Ending time of the movie screening.
     */
    int end;
    /*
     * Title of the movie that will be screened.
     */
    String movie;
    /*
     * An array of seats for this particular movie screening
     */
    Seats seats[];
    /*
     * Shows whether or not the screening is over
     */
    boolean showing=true;
    /*
     * The path to the text file containing all information regarding the occupancy of this movie screening
     */
    String path = System.getProperty("user.dir") + "\\src\\Cinema\\";

    /*
     * Loads an instance of a movie screening.
     * Loads up any relevant information regarding this movie screening if it had been created before.
     * Otherwise create a new text file with an empty cinema
     * @param cinema The cinema hall that the screening will take place at
     * @param date The date of screening
     * @param start The start time of the screening
     * @param end The end time of the screening
     * @param movie The movie title that will be screened
     */

    public MovieScreening(int cinema,int date,int start,int end,String movie) throws FileNotFoundException{
        this.Cinema = cinema;
        this.date = date;
        this.start = start;
        this.end = end;
        this.movie = movie;
        File f = new File(this.path+this.Cinema+"\\"+date+"@"+start+"@"+end+"@"+movie+"@.txt");
        if(!f.exists())
            this.seats = Seats.create(cinema);
        else
            this.seats = Seats.create(cinema, f);
    }

    /*
     * Lists the vacancy available for this movie screening
     */
    public void listVacancy(){
        for(int i=0;i<60;i++){
            if(seats[i].getVacancy() == false)
                System.out.print("O ");
            else
                System.out.print("X ");
            if(i-i/10*10 == 9)
                System.out.print("\n");
        }
        for (int i=60;i<seats.length;i++){
            if(seats[i].getVacancy() == false)
                System.out.print("OOO ");
            else
                System.out.print("XXX ");
            if(i-i/10*10 == 9 || i-i/10*10==4)
                System.out.print("\n");
        }
    }

    /*
     * Used to change the occupancy of a seat from false to true
     * Typically used when buying tickets
     * @param SeatID The seat that will be booked by the guest
     */
    public void updateVacancy(int SeatID) throws IOException{
        File f = new File(this.path+this.Cinema+"\\"+date+"@"+start+"@"+end+"@"+movie+"@.txt");
        Scanner sc = new Scanner(f);
        StringBuffer buffer = new StringBuffer();
        String s = sc.nextLine();
        for (int k=0;k<80;k++){
            if (k == SeatID)
                buffer.append("X "+System.lineSeparator());
            else if (s.charAt(0) != 'A')
                buffer.append("X "+System.lineSeparator());
            else
                buffer.append("A "+System.lineSeparator());
            if(sc.hasNextLine())
                s = sc.nextLine();
        }
        FileWriter wr = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(wr);
        bw.write(buffer.toString());
        bw.flush();
		bw.close();
		sc.close();
    }
}
