package Cinema;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class MovieScreening {
    int Cinema;
    int date;
    int start;
    int end;
    String movie;
    Seats seats[];
    String path = System.getProperty("user.dir") + "\\src\\Cinema\\";

    public MovieScreening(int c,int d,int s,int e,String m) throws FileNotFoundException{
        this.Cinema = c;
        this.date = d;
        this.start = s;
        this.end = e;
        this.movie = m;
        File f = new File(this.path+this.Cinema+"\\"+date+"@"+start+"@"+end+"@"+movie+"@.txt");
        if(!f.exists())
            this.seats = Seats.create(c);
        else
            this.seats = Seats.create(c, f);
        
    }

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

    public void updateVacancy(int SeatID) throws IOException{
        File f = new File(this.path+this.Cinema+"\\"+date+"@"+start+"@"+end+"@"+movie+"@.txt");
        FileWriter wr = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(wr);
        Scanner sc = new Scanner(f);
        StringBuffer buffer = new StringBuffer();
        for (int k=0;k<80;k++){
            if (k == SeatID)
                buffer.append("X "+System.lineSeparator());
            else
                buffer.append("A "+System.lineSeparator());
        }
        bw.write(buffer.toString());
        bw.flush();
		bw.close();
		sc.close();
    }
}
