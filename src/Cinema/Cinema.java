package Cinema;
import movList.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
/*
   Reperesents a Cinema hall  within a specific Cineplex. 
   Can be used to screen movies.
 */
import java.util.stream.Stream;

public class Cinema {

	/*
	 * Name of the Cinema hall that will be used to screen movies
	 */
	int name;
	/*
	 * Name of Cineplex which the cinema belongs to
	 */
	String Cineplex;
	/*
	 * path to the database for this specific cinema which contains all the movie screenings and occupancy.
	 */

	String path = System.getProperty("user.dir") + "\\src\\"; /////CHANGE THIS STRING TO LOCATION OF THIS JAVA FILE
	
	/*
	 * An array list of all the movies that are scheduled to be screened in the cinema.
	 */
	public List<MovieScreening> mlist = new ArrayList<>();

	/*
	 * Creates a cinema with the given name.
	 * Loads all relevant data for this cinema hall such as movie screenings and occupancy
	 * @param s the unique identifier for this cinema hall
	 */

	public Cinema(int s,String cineplex) throws FileNotFoundException{
		this.name = s;
		this.Cineplex = cineplex;
		boolean b;
		//Path path = Paths.get(this.path+this.Cineplex+"\\"+this.name);
		//Stream<Path> stream = Files.list(path);
		File f = new File(this.path+this.Cineplex+"\\"+this.name);
		System.out.println(this.path+this.Cineplex+"\\"+this.name);
		if (!f.exists()){
			System.out.println("creating file");
			b=f.mkdirs();
		}
		else{
			String l[] = f.list();
			String s2[];
			int temp[] = {0,0,0};
			for (int i=0;i<l.length;i++){
				System.out.println(l[i]);
				s2 = l[i].split("@",6);
				for (int k=0;k<3;k++){
					temp[k] = Integer.valueOf(s2[k]);
				}
				mlist.add(new MovieScreening(s, temp[0], temp[1], temp[2], s2[3],TypeOfMovie.valueOf(s2[4]),this.Cineplex));
			}
		}
		//File h = new File("C:\\Users\\yeozo\\OneDrive\\Documents\\GitHub\\SC2002-MOBLIMA\\src\\gg\\1\\3@200@211@ca@D3@.txt");
		//h.delete();
	}

	public boolean deleteSelect(MovieScreening movieScreening){
		File f = new File(movieScreening.path);
		if(f.exists())
		  return f.delete();
		else
		  return false;
	  }
	
	/*
	 * Adds a screening of a movie to this cinema hall.
	 * @param movie An object from the movie class that contains all information about the movie
	 * @param startTime A 4 digit integer to take in the starting time of the movie in a 24-hour clock format
	 * @param date A 6 digit integer in the format YYMMDD to record the date of the movie screening
	 */
	public void AddMovie(Movie movie, int startTime, int date,TypeOfMovie typeOfMovie) throws IOException {
		String s = movie.getTitle();
		int e = calculateEndTime(startTime,movie);

		File g = new File(this.path+this.Cineplex+"\\"+this.name+"\\"+date+"@"+startTime+"@"+e+"@"+s+"@"+typeOfMovie+"@.txt");

		if (g.exists()) {
			System.out.println("Movie with similar showtime already exists");
			return;
		}

		File f = new File(this.path+this.Cineplex+"\\"+this.name);

		String l[] = f.list();
		String s1;
		String s2[];
		for (int i=0;i<l.length;i++) {
			if(l.length==0)
				break;
			s1 = l[i];
			s2 = s1.split("@",5);
			if(Integer.valueOf(s2[0]) == date)
				continue;
			if (Integer.valueOf(s2[1]) >= e || i == l.length-1) {
				if(i==0)
					break;
				s1 = l[i-1];
				s2 = s1.split("@",5);
				if (Integer.valueOf(s2[2]) > startTime) {
					System.out.println("Timing clash, failed to add movie");
					return;
				}
				else 
					break;
			}
		}
		mlist.add(new MovieScreening(this.name, date, startTime, e, s,typeOfMovie,this.Cineplex));
		g.createNewFile();

		FileWriter w = new FileWriter(this.path+this.Cineplex+"\\"+this.name+"\\"+date+"@"+startTime+"@"+e+"@"+s+"@"+typeOfMovie+"@.txt",true);
		/*w.append("O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n");*/
		for (int i=0;i<80;i++){
			w.append("A \n");
		}
		w.flush();
		w.close();
	}
	
	/*
	 * Lists all movies scheduled to be screen in this cinema
	 */
	public void listMovie(){
		/*File f = new File(this.path+this.name);
		String l[] = f.list();
		String s;
		System.out.println("List of movie and showtime");
		for (int i=0;i<l.length;i++) {
			s = l[i];
			String k[] = s.split("@",5);
			System.out.println("Name of movie: "+k[3]+ " |ShowTime: "+ k[1]);
		}*/
		for (int i=0;i<mlist.size();i++){
			if(mlist.get(i).showing == true)
				System.out.println(i+1+") "+mlist.get(i).movie+ " showing at " + mlist.get(i).start );
		}
	}
	
	/*
	 * Lists the vacancy of a specific movie screening for this cinema
	 * @param index The index to be passed into the arrayList to iddentify which movie screening to list its vacancy
	 */
	public void listVacancy(int index) throws FileNotFoundException {
		mlist.get(index).listVacancy();
		/*String s = movie.getTitle();
		int e = calculateEndTime(t, movie);
		File f = new File(this.path+this.name+"\\"+date+"@"+t+"@"+e+"@"+s+"@.txt");
		if(!f.exists()) {
			System.out.println("No such movie with the showtime exists");
			return;
		}
		Scanner sc = new Scanner(f);
		int i=1;
		System.out.println("Vacancy of "+s+" at "+t+" on "+date);
		System.out.println("   A B C D E F G H I J");
		System.out.println("   ___________________");
		while(sc.hasNextLine()) {
			System.out.println(i + " |" +sc.nextLine());
			i++;
		}
		sc.close();*/
		/*MovieScreening movS = mlist.get(index);
		for(int i=0;i<60;i++){
			if(movS.seats[i].taken == false)
				System.out.print("X");
			else
				System.out.print("O");
			if(i-(i/10)*10 !=9)
				System.out.print(" ");
			else
			System.out.print("\n");
		}
		for(int i=60;i<80;i++){
			if(movS.seats[i].taken == false)
				System.out.print("XXX");
			else
				System.out.print("OOO");
			if(i-(i/10)*10 ==9 || i-(i/10)*10 ==4 && i != 60)
				System.out.print("\n");
			else
				System.out.print(" ");
		}*/
	}

	/*
	 * Converts the string format of the SeatID into an integer
	 * Checks whether or not the seat is already taken
	 * @param index The index of the movie screening in the arrayList to be updated
	 * @param SeatID The seatID that the guest would wish to book
	 */
	public void updateVacancy(int index, String SeatID) throws IOException{
		/*String m = movie.getTitle();
		File f = new File(this.path+this.name);
		String l[] = f.list();
		String temp = "";
		for (int i=0;i<l.length;i++) {
			temp = l[i];
			String k[] = temp.split("@",5);
			if(Integer.valueOf(k[1])== t || k[3]==m)
				break;
			if (i==l.length-1){
				System.out.println("No such movie exists");
				return;
			}
		}
		File g = new File(this.path+this.name+"\\"+temp);
		Scanner sc = new Scanner(g);
		String h[] = SeatID.split(" ",2);
		int del = h[1].charAt(0)-65;
		String new1="";
		StringBuffer buffer = new StringBuffer();
		for(int k=1;k<=5;k++){
			if(sc.hasNextLine()){
				if(k==Integer.valueOf(h[0])){
					String curr = sc.nextLine();
					for(int j=0;j<10;j++){
						if(j!=del)
							new1 = new1+curr.charAt(j*2);
						else
							new1 = new1+"X";
						if(j!=9)
							new1 = new1 + " ";
					}
					buffer.append(new1+System.lineSeparator());
					continue;
				}
				buffer.append(sc.nextLine()+System.lineSeparator());
			}
		}
		FileWriter wr = new FileWriter(g);
		BufferedWriter bw = new BufferedWriter(wr);
		bw.write(buffer.toString());
		bw.flush();
		bw.close();
		sc.close();*/
		int del = SeatID.charAt(0)-65;
		int var;
		if(del<6){
			var = del*10+Integer.valueOf(SeatID.charAt(1))-48;
		}
		else{
			var = 60;
			del -=5;
			var +=del*5;
			var +=Integer.valueOf(SeatID.charAt(1))-48;
		}
		if(mlist.get(index).seats[var].taken){
			System.out.println("Seat already taken");
			return;
		}
		mlist.get(index).updateVacancy(var);
		System.out.println("Successfully updated");
		return;
	}

	/*
	 * Returns a unique identifier for this cinema hall.
	 */
	public String getCinemaCode(){
		return "CN"+this.name;
	}

	/*
	 * Calculates the ending time for any movie scheduled.
	 * @param startTime The starting time of a movie screening.
	 * @param movie The movie object which would be screened.
	 */
	public int calculateEndTime(int startTime,Movie movie){
		int i = movie.getDuration();
		int hour = i/60;
		int min = i-(hour)*60;
		startTime = startTime + min;
		if(startTime-(startTime/100)*100 >60)
			startTime = startTime+40;
		startTime = startTime + hour*100;
		if(startTime>2500)
			startTime = startTime -2400;
		return startTime;	
	}

	public boolean deleteSelect(MovieScreening movieScreening){
		File f = new File(movieScreening.path);
		if(f.exists())
			return f.delete();
		else
			return false;
	}

	public void delete(int date,int time){

		String path = this.path+this.Cineplex+"\\"+this.name+"\\";
		File f = new File(path);
		File g = new File(path);
		String l[] = f.list();
		for(int i=0;i<l.length;i++){
			String k[] = l[i].split("@",5);
			int x = Integer.valueOf(k[0]);
			int y = Integer.valueOf(k[1]);
			if(date > x){
				g = new File(path+l[i]);
				g.delete();
			}
			if (date == x){
				if(time > y)
					g = new File(path+l[i]);
					g.delete();
			}
		}
	}

	public void rename(String movie, int startTime, int date,File f) throws IOException{
		//String s = movie.getTitle();
		//int e = calculateEndTime(startTime,movie);

		File g = new File(this.path+"\\"+date+"@"+startTime+"@1asdaasj"+"@"+movie+"@.txt");
		if(f.exists()){
			System.out.println("zam");
			//Files.move(f.toPath(),g.toPath());
			f.renameTo(g);
		}
		
	}
}