package movList;
import java.util.*;
import java.nio.file.Path;
import java.io.*;

public class MovieList {
	
	final private static String cwd = Path.of("").toAbsolutePath().toString() + "/Movies";
	private static Scanner sc = new Scanner(System.in);
	private static ArrayList<Movie> movieList = new ArrayList<Movie>();
	
	public static void initMovList() {
		// Initialises movieList for ease of access
		File[] movFolder = new File(cwd).listFiles();
		if(movFolder == null) movieList = new ArrayList<Movie>();
		else{
			for(File mov : movFolder){
				try{
					movieList.add(new Movie(mov));
				}
				catch(FileNotFoundException e){
					movieList.add(new Movie());
					System.out.println(e.getMessage());
				}
			}
		}
	}

	public static Movie createMovie() {

		// Flag for overwriting use
		int at = -1;

		// Record movie title
		System.out.print("Enter movie title: ");
		String title = sc.nextLine().strip();

		// Checking for overwrites
		for(Movie mov : movieList){
			if(mov.getTitle().equals(title)){
				System.out.printf("Movie %s exists. Overwrite movie file? Y/N \n", title);
				String in = sc.nextLine().strip();
				while(!in.equals("Y") && !in.equals("N")){
					System.out.printf("%s not valid input. Enter Y/N \n", in);
					in = sc.nextLine().strip();
				}
				if(in.equals("N")){
					System.out.println("Movie creation aborted");
					return new Movie();
				}
				else{
					System.out.println("Overwriting movie...");
					at = movieList.indexOf(mov);
				}
			}
		}

		// Writing to file
		Movie mov = new Movie(title, sc);
		mov.write(cwd);

		// Updating movieList
		if(at==-1) movieList.add(mov);
		else movieList.add(at, mov);
		return mov;
	}

	public static ArrayList<Movie> getMovieList(){
		return movieList;
	}
	
	public static Movie getMovieByTitle(String Title) throws FileNotFoundException {
		try{
			// Returns Movie constructor
			return new Movie(new File(cwd + "/" + Title + ".txt"));		
		}
		catch(FileNotFoundException e){
			System.out.printf("Movie %s does not exist! \n", Title);
			throw e;
		}
	}
	
	public static Movie getMovieByIndex(int n) throws IndexOutOfBoundsException {
		try{
			// Queries movieList at index n
			return movieList.get(n);
		}
		catch(IndexOutOfBoundsException e){
			System.out.printf("Index %d out of range! \n", n);
			throw e;
		}
	}

	public static void updateMovieByTitle(String title) throws FileNotFoundException {
		try{
			// Calls updateMovie
			Movie mov = getMovieByTitle(title);
			updateMovie(mov);
		}
		catch(FileNotFoundException e){
			throw e;
		}
	}

	public static void updateMovie(Movie mov) {
		int n;
		do {
			System.out.println("Select attribute to update: ");
			System.out.println("1. Director ");
			System.out.println("2. Synopsis ");
			System.out.println("3. Duration ");
			System.out.println("4. Cast ");
			System.out.println("5. Past Ratings and Reviews ");
			System.out.println("6. Status ");
			System.out.println("7. Exit ");
			n = sc.nextInt();
			while(n<1 || n>7){
				System.out.print("Invalid input. Reenter n: ");
				n = sc.nextInt();
			}
			switch(n){
				case 1:
					mov.setDirector(sc);
					System.out.println("Director updated ");
					break;
				case 2:
					mov.setSynopsis(sc);
					System.out.println("Synopsis updated ");
					break;
				case 3:
					mov.setDuration(sc);
					System.out.println("Duration updated ");
					break;
				case 4:
					mov.setCast(sc);
					System.out.println("Cast updated ");
					break;
				case 5:
					mov.setPastRatings(sc);
					System.out.println("Past Ratings and Reviews updated ");
					break;
				case 6:
					mov.setStatus(sc);
					System.out.println("Status updated ");
					break;
				case 7:
					System.out.println("Exiting... ");
					break;
			}
		} while(n!=7);
		mov.write(cwd);
	}
	
	public static void incMovieCounter(Movie mov) {
		mov.inc(cwd);
	}
	
	public static void decMovieCounter(Movie mov) {
		mov.dec(cwd);
		if(mov.getStatus() == STATUS.END_OF_SHOWING){
			System.out.printf("Movie %s no longer showing \n", mov.getTitle());
		}
	}
	
	public boolean titleExists(String Title) {
		String filepath = cwd + "/" + Title + ".txt";
		return new File(filepath).exists();
	}

	public boolean fileExists(File filepath) {
		return filepath.exists();
	}

	public static String getCwd() {
		return cwd;
	}
}
