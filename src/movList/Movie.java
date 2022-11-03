package Group_Assignment;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Movie {
	private String title;
	private String director;
	private String synopsis;
	private String[] cast;
	private Integer[] pastRatings;
	private String[] reviews;
	private float rating;
	
	Movie(String movFile){
		
		try {
			File file = new File(movFile);
			Scanner sc = new Scanner(file);
			title = sc.nextLine();
			director = sc.nextLine();
			synopsis = sc.nextLine();
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
			sc.close();
		}
		catch (FileNotFoundException e) {
			System.out.printf("Movie file %s not found!\n", movFile);
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
	
	public void print() {
		System.out.println(title);
		System.out.println(director);
		System.out.println(synopsis);
		for(String i : cast) System.out.println(i);
		for(int i=0; i < reviews.length; i++) System.out.println(pastRatings[i] + " " + reviews[i]);
		System.out.println(rating);
	}
}