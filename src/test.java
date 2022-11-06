package src;
import java.util.Scanner;
import java.io.IOException;

public class test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter name of movie");
		String s = sc.next();
		System.out.println("Enter showtime");
		int i = sc.nextInt();
		System.out.println("Enter endtime");
		int e = sc.nextInt();
		Cinema c = new Cinema(1);
		//c.AddMovie(s, i, e);
		//c.listMovie();
		//c.listVacancy(s,i,e);
		c.updateVacancy(s, i,"1 B");
	}

}
