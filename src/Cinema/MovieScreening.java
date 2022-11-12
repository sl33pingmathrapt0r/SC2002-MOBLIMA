package Cinema;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    String Cineplex;
    /*
     * Date of the movie screening.
     */
    Date startDate;
    Date endDate;

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
    TypeOfMovie typeOfMovie;
    /*
     * The path to the text file containing all information regarding the occupancy of this movie screening
     */
    String path = System.getProperty("user.dir");
    DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");

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

    public MovieScreening(int cinema,Date startDate,Date endDate,String movie,TypeOfMovie typeOfMovie,String cineplex) throws FileNotFoundException{
        this.Cinema = cinema;
        this.Cineplex = cineplex;
        this.startDate = startDate;
        this.endDate = endDate;
        this.movie = movie;
        this.typeOfMovie = typeOfMovie;
        String startString = df.format(startDate);
        String endString = df.format(endDate);
        this.path = System.getProperty("user.dir")+"\\src\\"+this.Cineplex+"\\"+this.Cinema+"\\"+startString+"@"+endString+"@"+movie+"@"+this.typeOfMovie+"@.txt";
        File f = new File(path);
        if(!f.exists())
            this.seats = Seats.create(cinema);
            /*if(this.Cinema == 3)
                this.seats = Seats.createP(cinema);
            else
                this.seats = Seats.create(cinema);
        else if(this.Cinema == 3)
            this.seats = Seats.createP(cinema, f);*/
        else
            this.seats = Seats.create(cinema,f);
    }

    /*
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

    /*
     * Used to change the occupancy of a seat from false to true
     * Typically used when buying tickets
     * @param SeatID The seat that will be booked by the guest
     */
    public void updateVacancy(int SeatID) throws IOException{
        System.out.println("updating");
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
}
