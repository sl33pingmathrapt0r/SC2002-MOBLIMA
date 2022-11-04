package movList;
import java.util.Scanner;

public class Test {
	
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		MovieList movlist = new MovieList(sc);
		for(Movie mov : movlist.listMovies()) System.out.println(mov.getTitle());
		movlist.createMovie();
		movlist.listMovies();
		for(int i=0; i<2; i++) {
			System.out.print("Select movie to be inspected ");
			String s = sc.nextLine();
			Movie mov = movlist.getMovie(s);
			int n = 0;
			while(n!=8) {
				System.out.println("0: Get title");
				System.out.println("1: Get director");
				System.out.println("2: Get synopsis");
				System.out.println("3: Get cast");
				System.out.println("4: Get past ratings");
				System.out.println("5: Get reviews");
				System.out.println("6: Get overall rating");
				System.out.println("7: Print");
				System.out.println("8: Quit");
				n = sc.nextInt();
				sc.nextLine();
				switch (n) {
					case 0:
						System.out.println(mov.getTitle());
						break;
					case 1:
						System.out.println(mov.getDirector());
						break;
					case 2:
						System.out.println(mov.getSynopsis());
						break;
					case 3:
						for(String actor : mov.getCast()) {
							System.out.println(actor);
						}
						break;
					case 4:
						for(int rating : mov.getPastRatings()) {
							System.out.printf("%d ", rating);
						}
						System.out.println();
						break;
					case 5:
						for(String review : mov.getReviews()) {
							System.out.println(review);
						}
						break;
					case 6:
						System.out.println(mov.getRating());
						break;
					case 7:
						mov.print();
						break;
					default:
						continue;
				}
			}
		}
		sc.close();
	}
	
}
