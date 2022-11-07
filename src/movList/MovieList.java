package movList;
import java.util.*;
import java.nio.file.Path;
import java.io.*;

public class MovieList {
	
	final private static String cwd = Path.of("").toAbsolutePath().toString() + "/Movies";
	private static Scanner sc = new Scanner(System.in);
	private static ArrayList<Movie> movieList = new ArrayList<Movie>();

	private static void updateMovieList(Movie mov) {
		for(int i=0; i < movieList.size(); i++){
			if(movieList.get(i).getTitle().equals(mov.getTitle())){
				movieList.set(i, mov);
				return;
			}
		}
		movieList.add(mov);
	}

	public static void initMovList() {
		new File(cwd).mkdirs();
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
		for(int i=0; i < movieList.size(); i++){
			if(movieList.get(i).getTitle().equals(title)){
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
					at = i;
					break;
				}
			}
		}

		// Writing to file
		Movie mov = new Movie(title, sc);
		mov.write(cwd);

		// Updating movieList
		if(at==-1) movieList.add(mov);
		else movieList.set(at, mov);
		System.out.println("Movie successfully created.");
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
		int n = 100;
		do {
			System.out.println("Select attribute to update: ");
			System.out.println("1. Director ");
			System.out.println("2. Synopsis ");
			System.out.println("3. Duration ");
			System.out.println("4. Cast ");
			System.out.println("5. Past Ratings and Reviews ");
			System.out.println("6. Status ");
			System.out.println("7. Exit ");

			while(true){
				String str = sc.nextLine();
				try{
					n = Integer.parseInt(str);
					if(n<1 || n>7){
						System.out.print("Invalid input. Reenter n: ");
						continue;
					}
					else break;
				}
				catch(NumberFormatException e){
					System.out.printf("Input %s not a valid integer. \n", str);
				}
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
		updateMovieList(mov);
	}
	
	public static void incMovieCounter(Movie mov) {
		mov.incCounter(cwd);
		updateMovieList(mov);
	}
	
	public static void decMovieCounter(Movie mov) {
		mov.decCounter(cwd);
		updateMovieList(mov);
	}
	
	public static void incTicketSales(Movie mov) {
		mov.incSales(cwd);
		updateMovieList(mov);
	}
	
	public static boolean titleExists(String Title) {
		String filepath = cwd + "/" + Title + ".txt";
		return new File(filepath).exists();
	}

	public static boolean fileExists(File filepath) {
		return filepath.exists();
	}

	public static Movie[] sortByRating(Movie[] movAr) {
		ArrayList<Movie> movList = new ArrayList<Movie>(Arrays.asList(movAr));
		movList.sort(new Comparator<Movie>(){
			public int compare(Movie mov1, Movie mov2) {
				return Float.compare(mov2.getRating(), mov1.getRating());
			}
		});
		return movList.toArray(new Movie[movAr.length]); 
	}

	public static Movie[] sortBySales(Movie[] movAr) {
		ArrayList<Movie> movList = new ArrayList<Movie>(Arrays.asList(movAr));
		movList.sort(new Comparator<Movie>(){
			public int compare(Movie mov1, Movie mov2) {
				return mov2.getSales() - mov1.getSales();
			}
		});
		return movList.toArray(new Movie[movAr.length]); 
	}

	public static String getCwd() {
		return cwd;
	}
}
