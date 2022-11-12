package Cinema;
import movList.*;
import ticket.ClassOfCinema;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/*
   Reperesents a Cinema hall  within a specific Cineplex. 
   Can be used to screen movies.
 */
enum TypeOfMovie{
	D3,DIGITAL
}

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
	List<MovieScreening> mlist = new ArrayList<>();
	DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
	
	ClassOfCinema type;

	/*
	 * Creates a cinema with the given name.
	 * Loads all relevant data for this cinema hall such as movie screenings and occupancy
	 * @param s the unique identifier for this cinema hall
	 */
	public Cinema(int cinemaName,String cineplex,int cinemaID) throws FileNotFoundException, ParseException{
		this.name = cinemaName;
		this.Cineplex = cineplex;
		File f = new File(this.path+this.Cineplex+"\\"+this.name);
		if (!f.exists()){
			System.out.println("creating file");
			f.mkdirs();
		}
		else{
			String l[] = f.list();
			String s2[];
			for (int i=0;i<l.length;i++){
				System.out.println(l[i]);
				s2 = l[i].split("@",5);
				Date startDate = new SimpleDateFormat("yyyyMMddHHmm").parse(s2[0]);
				Date endDate = new SimpleDateFormat("yyyyMMddHHmm").parse(s2[1]);
				mlist.add(new MovieScreening(cinemaName, startDate, endDate, s2[2],TypeOfMovie.valueOf(s2[3]),this.Cineplex));
			}
		}
		switch(cinemaID){
			default: type = ClassOfCinema.REGULAR; break;
			case 1: type = ClassOfCinema.ATMOS; break;
			case 2: type = ClassOfCinema.PLATINUM; break;
		}
	}
	
	/*
	 * Adds a screening of a movie to this cinema hall.
	 * @param movie An object from the movie class that contains all information about the movie
	 * @param startTime A 4 digit integer to take in the starting time of the movie in a 24-hour clock format
	 * @param date A 6 digit integer in the format YYMMDD to record the date of the movie screening
	 */
	public boolean AddMovie(String movie, Date startDate,TypeOfMovie typeOfMovie) throws IOException, ParseException {
		Date endDate = calculateEndDate(startDate, movie);
		String start = df.format(startDate);
		String end = df.format(endDate);
		File g = new File(this.path+this.Cineplex+"\\"+this.name+"\\"+start+"@"+end+"@"+movie+"@"+typeOfMovie+"@.txt");
		if (g.exists()) {
			System.out.println("Movie with similar showtime already exists");
			return false;
		}
		System.out.println(this.path+this.Cineplex+"\\"+this.name+"\\"+start+"@"+end+"@"+movie+"@"+typeOfMovie+"@.txt");
		if(checkVacant(movie,startDate) == false)
			return false;
		mlist.add(new MovieScreening(this.name, startDate, endDate, movie,typeOfMovie,this.Cineplex));
		g.createNewFile();
		FileWriter w = new FileWriter(this.path+this.Cineplex+"\\"+this.name+"\\"+start+"@"+end+"@"+movie+"@"+typeOfMovie+"@.txt",true);
		/*w.append("O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n");*/
		for (int i=0;i<80;i++){
			w.append("AAA \n");
		}
		w.flush();
		w.close();
		return true;
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
				System.out.println(mlist.get(i).movie+ " showing at " + mlist.get(i).startDate );
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

	public int seatConversion(String SeatID){
		int del = SeatID.charAt(0)-65;
		int var;
		if(this.name !=3){
			if(del<6){
				var = del*10+Integer.valueOf(SeatID.charAt(1))-48;
			}
			else{
				var = 60;
				del -=6;
				var +=del*5;
				var +=Integer.valueOf(SeatID.charAt(1))-48;
			}
			return var;
		}
		else{
			var = del*5 + Integer.valueOf(SeatID.charAt(1))-48;
			return var;
		}
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
		/*int del = SeatID.charAt(0)-65;
		int var;
		if(del<6){
			var = del*10+Integer.valueOf(SeatID.charAt(1))-48;
		}
		else{
			var = 60;
			del -=6;
			var +=del*5;
			var +=Integer.valueOf(SeatID.charAt(1))-48;
		} */
		int var = seatConversion(SeatID);
		if(var >19 && this.name ==3){
			System.out.println("Invalid seat");
			return;
		}
			
		if(mlist.get(index).seats[var].taken){
			System.out.println("Seat already taken");
			return;
		}
		mlist.get(index).updateVacancy(var);
		mlist.get(index).seats[var].taken = true;
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

	public Date calculateEndDate(Date startDate, String movie){
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.MINUTE,MovieList.getMovieByTitle(movie).getDuration());
		Date end = cal.getTime();
		return end;
	}

	public void delete(Date currentDate){
		String path = this.path+this.Cineplex+"\\"+this.name+"\\";
		File f = new File(path);
		File g = new File(path);
		String l[] = f.list();
		for(int i=mlist.size()-1;i>=0;i--){
			if(mlist.get(i).startDate.compareTo(currentDate) < 0){
				g = new File(this.mlist.get(i).path);
				g.delete();
				System.out.println("delete");
				this.mlist.remove(i);
			}
		}
	}

	public void deletSelect(int i){
		File f = new File(this.mlist.get(i).path);
		f.delete();
		this.mlist.remove(i);
	}

	public int search(String movie,Date startDate){
		for(int i=0;i<this.mlist.size();i++){
			MovieScreening movieScreening = this.mlist.get(i);
			if(startDate.compareTo(movieScreening.startDate) == 0 && movieScreening.movie.equals(movie)){
				System.out.println("found");
				return i;
			}
		}
		return -1;
	}

	public boolean updateScreening(String movie, Date oldDate, Date newDate) throws ParseException{
		int index = search(movie,oldDate);
		Date newEnd = calculateEndDate(newDate, movie);
		String newStartString = df.format(newDate);
		String newEndString = df.format(newEnd);
		MovieScreening update = this.mlist.get(index);
		if(checkVacant(movie, newDate) == false)
			return false;
		File f = new File(update.path);
		File g = new File(this.path+this.Cineplex+"\\"+this.name+"\\"+newStartString+"@"+newEndString+"@"+movie+"@"+update.typeOfMovie+"@.txt");
		update.startDate = newDate;
		update.endDate = newEnd;
		f.renameTo(g);
		return true;
	}

	public boolean checkVacant(String movie,Date startDate) throws ParseException{
		System.out.println("test");
		Date endDate = calculateEndDate(startDate, movie);
		File f = new File(this.path+this.Cineplex+"\\"+this.name);
		String l[] = f.list();
		String s1;
		String s2[];
		Date checkStart;
		Date checkEnd;
		for (int i=0;i<l.length;i++) {
			if(l.length==0)
				break;
			s1 = l[i];
			s2 = s1.split("@",5);
			checkStart = df.parse(s2[0]);
			checkEnd = df.parse(s2[1]);
			if(startDate.compareTo(checkEnd) >= 0)
				continue;
			if (startDate.compareTo(checkEnd) < 0) {
				System.out.println( startDate +" ||| " + checkEnd+ "|||"+endDate+"|||"+ startDate.compareTo(checkEnd));
				if (endDate.compareTo(checkStart) > 0) {
					System.out.println("Timing clash, failed to add movie");
					return false;
				}
				else 
					continue;
			}
		}
		return true;
	}

	public ClassOfCinema getClassOfCinema() {
		return type;
	}

	
}

