package Cinema;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

enum type {normal, couple, elite, ultima}

public class Seats {
    int Cinema;
    int seatID;
    type  seatType;
    boolean taken;

    public Seats(int c,int ID, type type){
        this.Cinema = c;
        this.seatID = ID;
        this.seatType = type;
        this.taken = false;
    };

    public static Seats[] create(int Cinema){
        Seats s[] = new Seats[80];
        type t = type.normal;
        for (int k=0;k<80;k++){
            if(k<60)
                t = type.normal;
            else if (k<70)
                t = type.couple;
            else if (k<75)
                t = type.elite;
            else
                t = type.ultima;
            s[k] = new Seats(Cinema,k,t);
        }
        return s;
    }

    public static Seats[] create(int Cinema, File f) throws FileNotFoundException{
        Seats s[] = new Seats[80];
        type t = type.normal;
        Scanner sc = new Scanner(f);
        boolean b = true;
        String p = "";
        for (int k=0;k<80;k++){
            p = sc.nextLine();
            if(k<60)
                t = type.normal;
            else if (k<70)
                t = type.couple;
            else if (k<75)
                t = type.elite;
            else
                t = type.ultima;
            s[k] = new Seats(Cinema,k,t);
            if (p.charAt(0) != 'A')
                s[k].taken = true;
        }
        return s;
    }

    public boolean getVacancy(){
        return this.taken;
    }
}
