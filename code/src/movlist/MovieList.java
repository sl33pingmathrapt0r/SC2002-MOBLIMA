package src.movlist;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Scanner;

import src.InputHandling;

/**
  Method class that interfaces with movie database
  @author Wesley Low
  @version 1.0
  @since 2022-11-11
 */
public class MovieList {
	
	/**
	 * Directory of movie folder
	 */
	final private static String CWD = Path.of("").toAbsolutePath().toString() + "/Movies";
	
	/**
	 * Scanner to read user input
	 */
	private static Scanner sc = new Scanner(System.in);
	
	/**
	 * Temporary storage of movie data
	 */
	private static ArrayList<Movie> movieList = new ArrayList<Movie>();

	/**
	 * Utility function to initialise the movieList ArrayList
	 * @param clock clock for checking if some movies are still screening
	 */
	public static void initmovlist(Date clock) {
		//clear array list of Movie first
		while(!movieList.isEmpty()) movieList.remove(0);
		new File(CWD).mkdirs();
		// Initialises movieList for ease of access
		File[] movFolder = new File(CWD).listFiles();
		if(movFolder == null) movieList = new ArrayList<Movie>();
		else{
			for(File mov : movFolder){
				try{
					movieList.add(new Movie(mov));
				}
				catch(FileNotFoundException e){}
				catch(Exception e){
					
					System.out.printf("Failed to load movie %s \n", mov.getName());
				}
			}
		}
		for(Movie mov : movieList){
			if(mov.getEndDate().before(clock)){
				mov.setStatus(Status.END_OF_SHOWING);
			}
		}
		updateFiles();
	}

	/**
	 * Utility function to update movieList with updated metadata for Movie mov
	 * @param mov Movie which information is to be updated
	 */
	private static void updateMovieList(Movie mov) {
		for(int i=0; i < movieList.size(); i++){
			if(movieList.get(i).getTitle().equals(mov.getTitle())){
				movieList.set(i, mov);
				return;
			}
		}
		movieList.add(mov);
	}

	/**
	 * Utility function to write data to text files in movie database
	 */
	public static void updateFiles() {
		for(Movie mov : movieList) mov.write(CWD);
	}

	/**
	 * Creates a new movie by reading metadata from user input
	 * If new movie already exists, provides option to overwrite
	 * data or abort the process
	 * @return Newly created Movie object
	 */
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
	
	/**
	 * Retrieves most updated image of the Movie object with
	 * the same title as the input String
	 * @param Title Desired movie's title
	 * @return Desired movie if found, else null
	 */
	public static Movie getMovieByTitle(String Title) {
		for(Movie mov : movieList) if(mov.getTitle().equals(Title)) return mov;

		return null;
	}

	/**
	 * Updating movie details not related to administrative metadata
	 * @param title Title of the movie to be updated
	 */
	public static void updateMovieAdmin(String title) {
		Movie mov = getMovieByTitle(title);
		if(mov == null) return;
		int n=0;
		do {
			System.out.println("Select attribute to update: ");
			System.out.println("1. Director ");
			System.out.println("2. Synopsis ");
			System.out.println("3. Duration ");
			System.out.println("4. Cast ");
			System.out.println("5. Change end date ");
			System.out.println("6. Exit ");

			while(true){
				n = InputHandling.getInt("");
				if(n<1 || n>6) System.out.print("Invalid input. Reenter n: ");
				else break;
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
					mov.setEndDate();
					System.out.println("End date updated \n");
					break;
				case 6:
					System.out.println("Exiting... \n");
					break;
			}
		} while(n!=6);
		updateMovieList(mov);
	}

	/**
	 * Updates selected movie's end date
	 * @param title Required movie' title
	 * @param end New end date
	 */
	public static void updateMovieEndDate(String title, Date end) {
		Movie mov = getMovieByTitle(title);
		if(mov==null) return;
		mov.setEndDate(end);
		updateMovieList(mov);
	}

	/**
	 * Extends movie end date
	 * End date cannot be shortened as it may clash with screenings
	 * @param title Title of movie to be edited
	 */
	public static void setEndDate(String title){
		Movie mov = getMovieByTitle(title);
		if(mov==null) return;
		mov.setEndDate();
	}
	
	/**
	 * Updates the review of the desired movie associated with a ticket ID
	 * @param tixId Ticket ID of user
	 * @param title Movie title whose review to be edited
	 * @return Updated rating and review
	 */
	public static String updateMovieReviews(String tixId, String title) {
		Movie mov = getMovieByTitle(title);
		if(mov == null) return null;
		String newReview = mov.editReview(tixId, sc);
		updateMovieList(mov);
		return newReview;
	}

	/**
	 * Increment cineplex counter of a movie
	 * @param title Title of movie to increment counter of
	 */
	public static void incMovieCounter(String title) {
		Movie mov = getMovieByTitle(title);
		if(mov==null) return;
		mov.incCounter();
		updateMovieList(mov);
	}

	/**
	 * Decrement cineplex counter of a movie
	 * @param title Title of movie to decrement counter of
	 */
	public static void decMovieCounter(String title) {
		Movie mov = getMovieByTitle(title);
		if(mov==null) return;
		mov.decCounter();
		updateMovieList(mov);
	}
	
	/**
	 * Increment ticket sales of a movie
	 * @param title Title of movie to increment ticket sales of
	 */
	public static void incTicketSales(String title) {
		Movie mov = getMovieByTitle(title);
		if(mov==null) return;
		mov.incSales();
		updateMovieList(mov);
	}

	/**
	 * Sets movie status from PREVIEW to NOW_SHOWING
	 * @param title Title of movie to be edited
	 */
	public static void setNowShowing(String title) {
		Movie mov = getMovieByTitle(title);
		if(mov==null) return;
		mov.setNowShowing();
		updateMovieList(mov);
	}

	/**
	 * Sets movie's status, subject to logical constraints
	 * A movie that is not showing cannot be PREVIEW or NOW_SHOWING
	 * A movie that is showing cannot be COMING_SOON or END_OF_SHOWING
	 * @param title Title of movie to be edited
	 * @param newStatus New status to switch to
	 */
	public static void setStatus(String title, Status newStatus) {
		Movie mov = getMovieByTitle(title);
		if(mov==null) return;
		mov.setStatus(newStatus);
		updateMovieList(mov);
	}
	
	/**
	 * Check if movie of given title exists
	 * @param Title Movie title to check
	 * @return true if exists, else false
	 */
	public static boolean titleExists(String Title) {
		for(Movie mov : movieList) if(mov.getTitle().equals(Title)) return true;
		return false;
	}

	/**
	 * Sort given movie array by overall rating
	 * @param movAr Movie array to be sorted
	 * @return Sorted array
	 */
	public static Movie[] sortByRating(Movie[] movAr) {
		ArrayList<Movie> movlist = new ArrayList<Movie>(Arrays.asList(movAr));
		movlist.sort(new Comparator<Movie>(){
			public int compare(Movie mov1, Movie mov2) {
				return Double.compare(mov2.getTotalRating(), mov1.getTotalRating());
			}
		});
		return movlist.toArray(new Movie[movAr.length]); 
	}

	/**
	 * Sort given movie array by ticket sales
	 * @param movAr Movie array to be sorted
	 * @return Sorted array
	 */
	public static Movie[] sortBySales(Movie[] movAr) {
		ArrayList<Movie> movlist = new ArrayList<Movie>(Arrays.asList(movAr));
		movlist.sort(new Comparator<Movie>(){
			public int compare(Movie mov1, Movie mov2) {
				return mov2.getSales() - mov1.getSales();
			}
		});
		return movlist.toArray(new Movie[movAr.length]); 
	}

	/**
	 * Getter for temporary movieList ArrayList
	 * @return Temporarily stored Movie data
	 */
	public static ArrayList<Movie> getMovieList(){
		return movieList;
	}

	/**
	 * Getter for movie folder
	 * @return Movie folder filepath
	 */
	public static String getCWD() {
		return CWD;
	}
}
