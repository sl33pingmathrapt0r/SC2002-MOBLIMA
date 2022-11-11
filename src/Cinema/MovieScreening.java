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

enum TypeOfMovie{
	D3,DIGITAL
}

public class MovieScreening {
    /*
     * Cinema hall that the screening will take place at.
     */

    int Cinema;
    String Cineplex;
    /*
     * Date of the movie screening.
     */
    public int date;
    /*
     * Starting time of the movie screening.
     */
    public int start;
    /*
     * Ending time of the movie screening.
     */
    public int end;
    /*
     * Title of the movie that will be screened.
     */
    public String movie;
    /*
     * An array of seats for this particular movie screening
     */
    public Seats seats[];
    /*
     * Shows whether or not the screening is over
     */

    boolean showing=true;
    TypeOfMovie typeOfMovie;
    /*
     * The path to the text file containing all information regarding the occupancy of this movie screening
     */
    String path = System.getProperty("user.dir");

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

    public MovieScreening(int cinema,int date,int start,int end,String movie,TypeOfMovie typeOfMovie,String cineplex) throws FileNotFoundException{
        this.Cinema = cinema;
        this.Cineplex = cineplex;
        this.date = date;
        this.start = start;
        this.end = end;
        this.movie = movie;
        this.typeOfMovie = typeOfMovie;
        this.path = System.getProperty("user.dir")+"\\src\\"+this.Cineplex+"\\"+this.Cinema+"\\"+date+"@"+start+"@"+end+"@"+movie+"@"+this.typeOfMovie+"@.txt";
        File f = new File(path);
        if(!f.exists())
            if(this.Cinema == 3)
                this.seats = Seats.createP(cinema);
            else
                this.seats = Seats.create(cinema);
        else if(this.Cinema == 3)
            this.seats = Seats.createP(cinema, f);
        else
            this.seats = Seats.create(cinema,f);
    }

    /*
     * Lists the vacancy available for this movie screening
     */
    public void listVacancy(){
        if (Cinema != 3){
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
        else{
            for(int i=0;i<20;i++){
                if(seats[i].getVacancy() == false)
                    System.out.print("O ");
                else
                    System.out.print("X ");
                if(i-i/10*10 == 9 || i-i/10*10 == 4)
                    System.out.print("\n");
            }
        }
    }

    /*
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
                buffer.append("X "+System.lineSeparator());
            else if (s.charAt(0) != 'A')
                buffer.append("X "+System.lineSeparator());
            else
                buffer.append("A "+System.lineSeparator());
            if(sc.hasNextLine())
                s = sc.nextLine();
            k++;
        }
        FileWriter wr = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(wr);
        bw.write(buffer.toString());
        bw.flush();
		bw.close();
		sc.close();
    }
}
