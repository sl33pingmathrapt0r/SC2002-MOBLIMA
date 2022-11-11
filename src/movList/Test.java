package movList;
import java.util.Scanner;

public class Test {
	
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		MovieList.initMovList();

		MovieList.createMovie();

		MovieList.updateFiles();
		sc.close();
	}
	
}
