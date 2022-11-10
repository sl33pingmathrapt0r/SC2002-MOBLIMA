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
					String T = mov.getName();
					System.out.printf("Movie %s not found \n", T.substring(0, T.length()-4));
				}
				catch(Exception e){
					String T = mov.getName();
					System.out.printf("Movie %s corrupted \n", T.substring(0, T.length()-4));
				}
			}
		}
	}

	public static void updateFiles() {
		for(Movie mov : movieList) mov.write(cwd);
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
					return null;
				}
				else{
					System.out.println("Overwriting movie...");
					at = i;
					break;
				}
			}
		}

		Movie mov = new Movie(title, sc);

		// Updating movieList
		if(at==-1) movieList.add(mov);
		else movieList.set(at, mov);
		System.out.println("Movie successfully created.");
		return mov;
	}

	public static ArrayList<Movie> getMovieList(){
		return movieList;
	}
	
	public static Movie getMovieByTitle(String Title) {
		for(Movie mov : movieList) if(mov.getTitle().equals(Title)) return mov;
		System.out.printf("Movie %s not found \n", Title);
		return null;
	}
	
	public static Movie getMovieByIndex(int n) {
		try{
			return movieList.get(n);
		}
		catch(IndexOutOfBoundsException e){
			System.out.printf("Index %d out of range \n", n);
			return null;
		}
	}

	public static void updateMovieByTitle(String title) {
		Movie mov = getMovieByTitle(title);
		if(mov == null) return;
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

			while(true){
				String str = sc.nextLine();
				System.out.println();
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
					System.out.println("Director updated \n");
					break;
				case 2:
					mov.setSynopsis(sc);
					System.out.println("Synopsis updated \n");
					break;
				case 3:
					mov.setDuration(sc);
					System.out.println("Duration updated \n");
					break;
				case 4:
					mov.setCast(sc);
					System.out.println("Cast updated \n");
					break;
				case 5:
					mov.setPastRatings(sc);
					System.out.println("Past Ratings and Reviews updated \n");
					break;
				case 6:
					mov.setStatus(sc);
					System.out.println("Status updated \n");
					break;
				case 7:
					System.out.println("Exiting... \n");
					break;
			}
		} while(n!=7);
		updateMovieList(mov);
	}
	
	public static void incMovieCounter(String title) {
		Movie mov = getMovieByTitle(title);
		if(mov==null) return;
		mov.incCounter();
		updateMovieList(mov);
	}
	
	public static void decMovieCounter(String title) {
		Movie mov = getMovieByTitle(title);
		if(mov==null) return;
		mov.decCounter();
		updateMovieList(mov);
	}
	
	public static void incTicketSales(String title) {
		Movie mov = getMovieByTitle(title);
		if(mov==null) return;
		mov.incSales();
		updateMovieList(mov);
	}
	
	public static boolean titleExists(String Title) {
		for(Movie mov : movieList) if(mov.getTitle().equals(Title)) return true;
		return false;
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
