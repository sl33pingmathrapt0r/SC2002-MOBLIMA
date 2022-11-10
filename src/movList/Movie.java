package movList;
import java.util.*;
import java.io.*;

/** */

public class Movie {
	final private String title;
	private String director;
	private String synopsis;
	private int duration;
	private String[] cast;
	private ArrayList<Integer> pastRatings = new ArrayList<Integer>();
	private ArrayList<String> reviews = new ArrayList<String>();
	private HashMap<String, Integer> tixIDToIdx = new HashMap<String, Integer>();
	private float totalRating = 0;
	private STATUS status = STATUS.COMING_SOON;
	private int counter = 0;
	private int ticketSales = 0;
	
	Movie(File movFile) throws FileNotFoundException {
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
			n = sc.nextInt();
			sc.nextLine();
			for(int i=0; i<n; i++) {
				tixIDToIdx.put(sc.next(), i);
				pastRatings.add(sc.nextInt());
				reviews.add(sc.nextLine().substring(1));
			}
			totalRating = sc.nextFloat();
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
	
	Movie(String Title, Scanner sc){
		title = Title;
		setDirector(sc);
		setSynopsis(sc);
		setDuration(sc);
		setCast(sc);
		System.out.println();
	}

	private int getInt(String message, Scanner sc) {
		int n;
		while(true){
			System.out.print(message);
			String str = sc.nextLine();
			try{
				n = Integer.parseInt(str);
				if(n < 0){
					System.out.println("Input must be positive. ");
					continue;
				}
				else break;
			}
			catch(NumberFormatException e){
				System.out.printf("Input %s not a valid integer. \n", str);
			}
		}
		return n;
	}

	private int getR(Scanner sc) {
		int r;
		while(true){
			System.out.printf("Enter rating: ");
			try{
				r = sc.nextInt();
				sc.nextLine();
				if(r < 0 || r > 5){
					System.out.println("Input must be between 0 and 5. ");
					continue;
				}
				else break;
			}
			catch(InputMismatchException e){
				System.out.println("Input not a valid integer. ");
			}
		}
		return r;
	}

	void setDirector(Scanner sc){
		System.out.print("Enter director's name: ");
		director = sc.nextLine();
	}

	void setSynopsis(Scanner sc){
		System.out.println("Enter synopsis: ");
		synopsis = sc.nextLine();
	}

	void setDuration(Scanner sc){
		duration = getInt("Enter duration in minutes: ", sc);
	}

	void setCast(Scanner sc){
		int n = getInt("Enter number of cast members: ", sc);
		cast = new String[n];
		for(int i=0; i<n; i++) {
			System.out.printf("Enter cast member %d: ", i+1);
			cast[i] = sc.nextLine().strip();
		}
	}

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
		return newReview;
	}

	void setStatus(STATUS newStatus){
		status = newStatus;
		if(status==STATUS.END_OF_SHOWING) {
			System.out.printf("Movie %s no longer showing \n", title);
		}
	}

	void write(String cwd) {
		String filepath = cwd + "/" + title + ".txt";
		try {
			FileWriter fwriter = new FileWriter(filepath);
			fwriter.write(this.toString());
			fwriter.close();
		}
        catch (IOException e) {
            System.out.print(e.getMessage());
        }
	}

	void incCounter() {
		if(counter==3){
			System.out.println("Counter at 3, cannot increment");
			System.out.println();
			return;
		}
		counter++;
		if(counter==1){
			status = STATUS.NOW_SHOWING;
			System.out.printf("Movie %s now showing \n", title);
			System.out.println();
		}
	}

	void decCounter() {
		if(counter==0){
			System.out.println("Counter at 0, cannot decrement");
			System.out.println();
			return;
		}
		counter--;
		if(counter==0){
			status = STATUS.END_OF_SHOWING;
			System.out.printf("Movie %s no longer showing \n", title);
			System.out.println();
		}
	}

	void incSales() {
		ticketSales++;
	}

	public String getTitle(){
		return title;
	}

	public String getDirector(){
		return director;
	}

	public String getSynopsis(){
		return synopsis;
	}

	public int getDuration(){
		return duration;
	}

	public String[] getCast(){
		return cast;
	}

	public ArrayList<Integer> getPastRatings(){
		return pastRatings;
	}

	public ArrayList<String> getReviews(){
		return reviews;
	}

	public float getRating(){
		return totalRating;
	}

	public STATUS getStatus(){
		return status;
	}

	public int getCounter() {
		return counter;
	}

	public int getSales(){
		return ticketSales;
	}

	public String toString() {
		String mov = title + "\n" 
					+ director + "\n" 
					+ synopsis + "\n"
					+ duration + "\n"
					+ String.valueOf(cast.length) + "\n";
		for(String actor : cast){
			mov += actor + "\n";
		}
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
}