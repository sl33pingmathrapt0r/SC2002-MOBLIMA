package Cinema;
import movList.*;
import java.util.Scanner;
import java.io.IOException;

public class test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Cinema c = new Cinema(1);
		/*Scanner sc = new Scanner(System.in);
		System.out.println("Enter name of movie");
		String s = sc.next();
		System.out.println("Enter showtime");
		int i = sc.nextInt();
		System.out.println("Enter date");
		int d = sc.nextInt();
		Movie movie = new Movie(s,sc);
		//Ticket ticket = new Ticket(s, TypeOfMovie.DIGITAL, ClassOfCinema.ATMOS, s, s, Day.FRIDAY, null,AgeGroup.ADULT, "1 A");
		c.AddMovie(movie, i,d);*/
		c.listMovie();
		c.listVacancy(0);
		c.updateVacancy( 0,"A5");
	}

}
