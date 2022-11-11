package Cinema;
import movList.*;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		//Path path = Paths.get("C:\\Users\\yeozo\\OneDrive\\Documents\\GitHub\\SC2002-MOBLIMA\\src\\gg\\1\\3@2@6@ca@D3@.txt");
		//Files.delete(path);
		//File f = new File("C:\\Users\\yeozo\\OneDrive\\Documents\\GitHub\\SC2002-MOBLIMA\\src\\gg\\1\\3@200@211@ca@D3@.txt");
		//f.delete();
		Cinema c = new Cinema(1,"gg");
		/*Scanner sc = new Scanner(System.in);
		System.out.println("Enter name of movie");
		String s = sc.next();
		System.out.println("Enter showtime");
		int i = sc.nextInt();
		System.out.println("Enter date");
		int d = sc.nextInt();
		Movie movie = new Movie(s,sc);*/
		//Ticket ticket = new Ticket(s, TypeOfMovie.DIGITAL, ClassOfCinema.ATMOS, s, s, Day.FRIDAY, null,AgeGroup.ADULT, "1 A");
		//c.AddMovie(movie, i,d,TypeOfMovie.valueOf("D3"));
		c.listMovie();
		//c.delete(500000,30000);
		c.listVacancy(0);
		//c.updateVacancy( 0,"G1");
		//c.delete(500000000,10000000);
		//c.listMovie();
		//c.rename("monke",99999,300021,f);
	}

}
