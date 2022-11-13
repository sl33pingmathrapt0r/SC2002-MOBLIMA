package movList;

import movList.inputHandling;
import java.time.*;
import java.util.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;

public class Movie {

	/**
	 * Movie title
	 */
	final private String title;

	/**
	 * Director's name
	 */
	private String director;

	/** 
	 * Movie synopsis
	 */
	private String synopsis;

	/**
	 * Movie duration in minutes
	 */
	private int duration;

	/**
	 * Names of cast members
	 */
	private String[] cast;

	private AGE_RATING ageRating;

	final private boolean isBlockBuster;

	private Date endDate;

	/**
	 * History of ratings; movies are rated from 1-5
	 * Ratings are made by moviegoers and are paired with reviews
	 */
	private ArrayList<Integer> pastRatings = new ArrayList<Integer>();

	/**
	 * History of reviews
	 * Reviews are made by moviegoers and are paired with ratings
	 */
	private ArrayList<String> reviews = new ArrayList<String>();


	/**
	 * Maps ticket ID to the rating and review made by the ticket holder
	 */
	private HashMap<String, Integer> tixIDToIdx = new HashMap<String, Integer>();

	/**
	 * Average of moviegoers' ratings
	 */
	private double totalRating = 0;

	/**
	 * Movie status
	 * Defaults to COMING_SOON, and can switch to NOW_SHOWING or END_OF_SHOWING
	 */
	private STATUS status = STATUS.COMING_SOON;

	/**
	 * Number of cineplexes screening the movie
	 * Used to calculate how STATUS should change
	 */
	private int counter = 0;

	/**
	 * Total number of tickets sold for this movie
	 */
	private int ticketSales = 0;

	// /**
	//  * Date representing final screening of the movie
	//  */
	// private Calendar endDate;
	
	/**
	 * Constructs Movie object given the data stored in a text file
	 * Assumes text file is properly formatted; text files can only be
	 * accessed by the MovieList and Movie classes to maintain proper formatting
	 * @param movFile Text file's filepath
	 * @throws FileNotFoundException Error thrown if file is not found
	 * @throws ParseException
	 */
	Movie(File movFile) throws Exception {
		try {
			Scanner sc = new Scanner(movFile);
			title = sc.nextLine();
			director = sc.nextLine();
			synopsis = sc.nextLine();
			duration = sc.nextInt();
			sc.nextLine();
			int n = sc.nextInt();
			sc.nextLine();
			cast = new String[n];
			for(int i=0; i<n; i++) {
				cast[i] = sc.nextLine();
			}
			ageRating = AGE_RATING.valueOf(sc.nextLine());
			isBlockBuster = Boolean.valueOf(sc.nextLine());
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			sdf.setLenient(false);
			endDate = sdf.parse(sc.nextLine());
			n = sc.nextInt();
			sc.nextLine();
			for(int i=0; i<n; i++) {
				tixIDToIdx.put(sc.next(), i);
				pastRatings.add(sc.nextInt());
				reviews.add(sc.nextLine().substring(1));
			}
			totalRating = sc.nextDouble();
			sc.nextLine();
			status = STATUS.valueOf(sc.nextLine());
			counter = sc.nextInt();
			ticketSales = sc.nextInt();
			sc.close();
		}
		catch(FileNotFoundException e){
			String T = movFile.getName();
			System.out.printf("Movie %s not found \n\n", T.substring(0, T.length()));
			throw e;
		}
	}
	
	/**
	 * Constructs Movie class from user input
	 * Used to define a new movie not recorded in the movie list
	 * @param Title Movie title
	 * @param sc Scanner to read user input
	 */
	Movie(String Title, Scanner sc){
		title = Title;
		setDirector(sc);
		setSynopsis(sc);
		setDuration(sc);
		setCast(sc);
		setRating(sc);
		System.out.println("Is the movie a blockbuster?");
		System.out.println("1. Yes");
		System.out.println("2. No");
		if(inputHandling.getInt("", "Invalid input", 1, 2)==1) isBlockBuster = true;
		else isBlockBuster=false;
		endDate = setEndDate(sc);
		System.out.println();
	}

	/**
	 * Prints a custom message and queries the user
	 * until a positive integer is received
	 * @param message Custom message
	 * @param sc Scanner to read user input
	 * @return User's positive integer input
	 */
	private int getPosInt(String message, Scanner sc) {
		int n;
		while(true){
			n = inputHandling.getInt(message);
			if(n<0) System.out.println("Input must be positive. ");
			else break;
		}
		return n;
	}

	/**
	 * Queries user for a valid integer rating in the range 1-5
	 * @param sc Scanner to read user input
	 * @return User's rating input
	 */
	private int getR(Scanner sc) {
		int r;
		while(true){
			r = inputHandling.getInt("Enter rating: ");
			if(r < 0 || r > 5) System.out.println("Input must be between 0 and 5. ");
			else break;
		}
		return r;
	}

	/**
	 * Queries user for a valid number of cast members greater than 1
	 * @param sc Scanner to read user input
	 * @return User's rating input
	 */
	private int getC(Scanner sc) {
		int r;
		while(true){
			r = inputHandling.getInt("Enter number of cast members: ");
			if(r < 2) System.out.println("Input must be at least 2. ");
			else break;
		}
		return r;
	}

	private static Date setEndDate(Scanner sc) {
        while(true){
            System.out.print("Enter end date in dd/MM/yyyy format: ");
            String date = sc.nextLine();
            System.out.println("Enter 24 hour time in HH:mm");
            String time = sc.nextLine();
            try{
                SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy HH:mm");
                sdf.setLenient(false);
                return sdf.parse(date + " " + time);
            }
            catch(Exception e){System.out.println("Invalid input");}
        }
    }

	/**
	 * Utility method for updating director's name
	 * @param sc Scanner to read user input
	 */
	void setDirector(Scanner sc){
		System.out.print("Enter director's name: ");
		director = sc.nextLine();
	}

	/**
	 * Utility method for updating synopsis
	 * @param sc Scanner to read user input
	 */
	void setSynopsis(Scanner sc){
		System.out.println("Enter synopsis: ");
		synopsis = sc.nextLine();
	}

	/**
	 * Utility method for updating movie duration
	 * @param sc Scanner to read user input
	 */
	void setDuration(Scanner sc){
		duration = getPosInt("Enter duration in minutes: ", sc);
	}

	/**
	 * Utility method for updating cast member's names
	 * @param sc Scanner to read user input
	 */
	void setCast(Scanner sc) {
		int n = getC(sc);
		cast = new String[n];
		for(int i=0; i<n; i++) {
			System.out.printf("Enter cast member %d: ", i+1);
			cast[i] = sc.nextLine().strip();
		}
	}

	void setRating(Scanner sc) {
		String newRating;
		while(true) {
			System.out.print("Enter new age rating (G, PG, PG_13, R, NC_17): ");
			newRating = sc.nextLine();
			try{
				ageRating = AGE_RATING.valueOf(newRating);
				break;
			}
			catch(Exception e){
				System.out.printf("Input %s not valid. \n", newRating);
			}
		}
	}

	void setEndDate(Date end) {
		endDate = end;
	}

	void setEndDate() {
		if(endDate==null) endDate = inputHandling.getDate();
		else{
			while(true){
				Date newDate = inputHandling.getDate();
				if(newDate.after(endDate)){
					endDate = newDate;
					break;
				}
				else System.out.printf("Invalid end date. New end date must be after %s \n", 
				new SimpleDateFormat("dd/MM/yyyy HH:mm").format(endDate));
			}
		}
	}

	 /**
	  * Utility method for updating reviews
	  * @param tixID Ticket ID associated with review
	  * @param sc Scanner to read user input
	  * @return New rating and review after editing
	  */
	String editReview(String tixID, Scanner sc) {
		int idx = tixIDToIdx.containsKey(tixID) ? tixIDToIdx.get(tixID) : -1;
		int r = getR(sc);
		System.out.println("Enter review: ");
		String newReview = sc.nextLine();
		if(idx!=-1){
			reviews.set(idx, newReview);
			totalRating -= (pastRatings.get(idx) - r)/pastRatings.size();
			pastRatings.set(idx, r);
		}
		else{
			tixIDToIdx.put(tixID, reviews.size());
			reviews.add(newReview);
			totalRating = (totalRating*pastRatings.size() + r)/reviews.size();
			pastRatings.add(r);
		}
		System.out.println("Review edited");
		return Integer.valueOf(r) + newReview;
	}

	/**
	 * Utility method for updating status
	 * @param newStatus New status to be updated to
	 */
	void setStatus(STATUS newStatus){
		status = newStatus;
		if(status==STATUS.END_OF_SHOWING) {
			System.out.printf("Movie %s no longer showing \n", title);
		}
	}

	/**
	 * Utility method for writing to file
	 * @param cwd Movie folder to write to
	 */
	void write(String cwd) {
		String filepath = cwd + "/" + title + ".txt";
		try {
			FileWriter fwriter = new FileWriter(filepath);
			fwriter.write(this.asString());
			fwriter.close();
		}
        catch (IOException e) {
            System.out.print(e.getStackTrace());
        }
	}

	/**
	 * Utility method to increase cineplex counter
	 */
	void incCounter() {
		if(counter==3){
			System.out.println("Counter at 3, cannot increment");
			System.out.println();
			return;
		}
		counter++;
		if(counter==1){
			status = STATUS.PREVIEW;
			System.out.printf("Movie %s set to PREVIEW \n", title);
			System.out.println();
		}
	}

	/**
	 * Utility method to decrease cineplex counter
	 */
	void decCounter() {
		if(counter==0){
			System.out.println("Counter at 0, cannot decrement");
			System.out.println();
			return;
		}
		counter--;
		if(counter==0){
			status = STATUS.END_OF_SHOWING;
			System.out.printf("Movie %s set to END_OF_SCREENING \n", title);
			System.out.println();
		}
	}

	/**
	 * Utility method to increase ticket sales
	 */
	void incSales() {
		ticketSales++;
	}

	/**
	 * Sets status to NOW_SHOWING, unless cineplex counter is 0
	 */
	void setNowShowing() {
		if(counter==0){
			System.out.printf("Movie %s not showing at any cineplex, cannot set to NOW_SHOWING \n", title);
			return;
		}
		status = STATUS.NOW_SHOWING;
		System.out.printf("Movie %s set to NOW_SHOHWING \n", title);
		System.out.println();
	}

	/**
	 * Formats movie metadata into string to be written to text file
	 */
	String asString() {
		String mov = title + "\n" 
					+ director + "\n" 
					+ synopsis + "\n"
					+ duration + "\n"
					+ String.valueOf(cast.length) + "\n";
		for(String actor : cast){
			mov += actor + "\n";
		}
		mov += String.valueOf(ageRating) + "\n";
		mov += String.valueOf(isBlockBuster) + "\n";
		mov += new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(endDate) + "\n";
		if(pastRatings!=null){
			mov += String.valueOf(pastRatings.size()) + "\n";
			for(String tixID : tixIDToIdx.keySet()){
				int i = tixIDToIdx.get(tixID);
				mov += tixID + " " + pastRatings.get(i).toString() + " " + reviews.get(i) + "\n";
			}
		}
		mov += String.valueOf(totalRating) + "\n"
				+ String.valueOf(status) + "\n"
				+ String.valueOf(counter) + "\n"
				+ String.valueOf(ticketSales);
		return mov;
	}

	/**
	 * Getter for movie title
	 * @return Movie title
	 */
	public String getTitle(){
		return title;
	}

	/**
	 * Getter for director's name
	 * @return Director's name
	 */
	public String getDirector(){
		return director;
	}

	/**
	 * Getter for movie synopsis
	 * @return Movie synopsis
	 */
	public String getSynopsis(){
		return synopsis;
	}

	/**
	 * Getter for movie duration
	 * @return Movie duration
	 */
	public int getDuration(){
		return duration;
	}

	/**
	 * Getter for cast member's names
	 * @return Cast member's names
	 */
	public String[] getCast(){
		return cast;
	}

	public AGE_RATING getAgeRating(){
		return ageRating;
	}

	/**
	 * Getter for past ratings
	 * @return Past ratings
	 */
	public ArrayList<Integer> getPastRatings(){
		return pastRatings;
	}

	/**
	 * Getter for past reviews
	 * @return Past reviews
	 */
	public ArrayList<String> getReviews(){
		return reviews;
	}

	/**
	 * Getter for overall rating
	 * @return Overall rating
	 */
	public double getTotalRating(){
		return totalRating;
	}

	/**
	 * Getter for movie status
	 * @return Movie status
	 */
	public STATUS getStatus(){
		return status;
	}

	/**
	 * Getter for cineplex counter
	 * @return Cinema counter
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * Getter for ticket sales
	 * @return Ticket sales
	 */
	public int getSales(){
		return ticketSales;
	}

	public Date getEndDate() {
		return endDate;
	}

	public boolean isBlockBuster() {
		return isBlockBuster;
	}

	
}