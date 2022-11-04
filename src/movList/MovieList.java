package movList;
import java.util.Scanner;
import java.nio.file.Path;
import java.io.*;

public class MovieList {
	
	final private static String cwd = Path.of("").toAbsolutePath().toString()  + "/Movies";
	private static Scanner sc = new Scanner(System.in);
	
	public static void createMovie() {
		String movie = "";
		System.out.print("Enter movie title: ");
		String title = sc.nextLine();
		movie += title + "\n";
		System.out.print("Enter director's name: ");
		movie += sc.nextLine() + "\n";
		System.out.println("Enter synopsis: ");
		movie += sc.nextLine() + "\n";
		
		System.out.print("Enter number of cast members: ");
		int n = sc.nextInt();
		movie += n + "\n";
		sc.nextLine();
		for(int i=0; i<n; i++) {
			System.out.printf("Enter cast member %d: ", i+1);
			movie += sc.nextLine() + "\n";
		}

		System.out.print("Enter number of reviews: ");
		n = sc.nextInt();
		movie += n + "\n";
		sc.nextLine();
		int[] ratings = new int[n];
		float rating = 0;
		for(int i=0; i<n; i++) {
			System.out.printf("Enter rating %d: ", i+1);
			ratings[i] = sc.nextInt();
			sc.nextLine();
			rating += ratings[i];
			movie += ratings[i] + " ";
			System.out.printf("Enter review %d: ", i+1);
			movie += sc.nextLine() + "\n";
		}	
		
		movie += rating/n;
		
		String filepath = cwd + "/" + title + ".txt";
		try {
			FileWriter fwriter = new FileWriter(filepath);
			fwriter.write(movie);
			fwriter.close();
            System.out.println("Movie successfully created.");
		}
        catch (IOException e) {
            System.out.print(e.getMessage());
        }
	}
	
	public static Movie getMovie(String Title) {
		return new Movie(cwd + "/" + Title + ".txt");		
	}
	
	public static Movie[] listMovies() {
		File[] movFolder = new File(cwd).listFiles();
		if(movFolder == null) return new Movie[0];
		Movie [] movList = new Movie[movFolder.length];
		int i = 0;
		for(File mov : movFolder) {
			movList[i++] = new Movie(cwd + "/" + mov.getName());
		}
		return movList;
	}
	
	public static void deleteMovie(String Title) {
		File f = new File(cwd + "/" + Title + ".txt");
		if(f.delete()) {
			System.out.printf("Movie %s deleted successfully \n", Title);
		}
		else {
			System.out.printf("Movie %s does not exist \n", Title);
		}
	}
	
	public static String getCwd() {
		return cwd;
	}
}
