package movList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.*;

public class Movie {
	private String title;
	private String director;
	private String synopsis;
	private int duration;
	private String[] cast;
	private Integer[] pastRatings;
	private String[] reviews;
	private float rating;
	private STATUS status;
	private int counter;

	Movie(){
		counter = 0;
	}
	
	Movie(File movFile) throws FileNotFoundException{
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
			pastRatings = new Integer[n];
			reviews = new String[n];
			for(int i=0; i<n; i++) {
				pastRatings[i] = sc.nextInt();
				reviews[i] = sc.nextLine().substring(1);
			}
			rating = sc.nextFloat();
			sc.nextLine();
			status = STATUS.valueOf(sc.nextLine());
			counter = sc.nextInt();
			sc.close();
		}
		catch (FileNotFoundException e) {
			throw e;
		}
	}
	
	Movie(String Title, Scanner sc){
		title = Title;
		setDirector(sc);
		setSynopsis(sc);
		setDuration(sc);
		setCast(sc);
		setPastRatings(sc);
		setStatus(sc);
		counter = 0;
	}

	private int getInt(String message, Scanner sc) {
		int n;
		while(true){
			System.out.println(message);
			try{
				n = sc.nextInt();
				if(n < 0){
					System.out.println("Input must be positive. ");
					continue;
				}
				else break;
			}
			catch(InputMismatchException e){
				System.out.println("Input not a valid integer. ");
			}
		}
		return n;
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

	void setPastRatings(Scanner sc){
		int n = getInt("Enter number of reviews: ", sc);
		pastRatings = new Integer[n];
		reviews = new String[n];
		rating = 0;
		for(int i=0; i<n; i++) {
			int r;
			while(true){
				System.out.printf("Enter rating %d: ", i+1);
				try{
					r = sc.nextInt();
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
			pastRatings[i] = r;
			rating += pastRatings[i];
			System.out.printf("Enter review %d: ", i+1);
			System.out.println();
			reviews[i] = sc.nextLine().strip();
		}
		rating /= n;
	}

	void setStatus(Scanner sc){
		System.out.print("Enter status (\"COMING_SOON\", \"NOW_SHOWING\", \"END_OF_SHOWING\"): ");
		String in = sc.nextLine().strip();
		while(!in.equals("COMING_SOON") && !in.equals("NOW_SHOWING") && !in.equals("END_OF_SHOWING")){
			System.out.println("Invalid status (\"COMING_SOON\", \"NOW_SHOWING\", \"END_OF_SHOWING\"): ");
			in = sc.nextLine().strip();
		}
		status = STATUS.valueOf(in);
		if(status==STATUS.END_OF_SHOWING) {
			System.out.printf("Movie %s no longer showing \n", title);
		}
		sc.nextLine();
	}

	void write(String cwd) {
		String filepath = cwd + "/" + title + ".txt";
		try {
			FileWriter fwriter = new FileWriter(filepath);
			fwriter.write(this.toString());
			fwriter.close();
            System.out.println("Movie successfully created.");
		}
        catch (IOException e) {
            System.out.print(e.getMessage());
        }
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

	public Integer[] getPastRatings(){
		return pastRatings;
	}

	public String[] getReviews(){
		return reviews;
	}

	public float getRating(){
		return rating;
	}

	public int getCounter() {
		return counter;
	}

	public STATUS getStatus(){
		return status;
	}

	public void inc(String cwd) {
		if(counter==3){
			System.out.println("Counter at 3, cannot increment");
			return;
		}
		counter++;
		this.write(cwd);
	}

	public void dec(String cwd) {
		if(counter==0){
			System.out.println("Counter at 0, cannot decrement");
			return;
		}
		counter--;
		if(counter==0){
			status = STATUS.END_OF_SHOWING;
			System.out.printf("Movie %s no longer showing \n", title);
		}
		this.write(cwd);
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
		mov += String.valueOf(pastRatings.length) + "\n";
		for(int i=0; i < pastRatings.length; i++){
			mov += pastRatings[i].toString() + " " + reviews[i] + "\n";
		}
		mov += String.valueOf(rating) + "\n"
				+ String.valueOf(status) + "\n"
				+ String.valueOf(counter);
		return mov;
	}
}