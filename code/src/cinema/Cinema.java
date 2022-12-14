package src.cinema;

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

import src.movlist.MovieList;
import src.ticket.ClassOfCinema;
import src.ticket.TypeOfMovie;

/**
  Reperesents a Cinema hall  within a specific Cineplex. 
  Can be used to screen movies.
*/


public class Cinema {

	/**
	 * Name of the Cinema hall that will be used to screen movies
	 */
	int name;
	/**
	 * Name of Cineplex which the cinema belongs to
	 */
	String Cineplex;
	/**
	 * path to the database for this specific cinema which contains all the movie screenings and occupancy.
	 */
	String path = System.getProperty("user.dir") + "\\src\\Cineplex\\"; /////CHANGE THIS STRING TO LOCATION OF THIS JAVA FILE
	
	/**
	 * An array list of all the movies that are scheduled to be screened in the cinema.
	 */
	List<MovieScreening> mlist = new ArrayList<>();

	/**
	 * A dateformat tool for reading and writing Date to String
	 */
	DateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
	
	/**
	 * enum type representing class of cinema
	 */
	ClassOfCinema type;

	/**
	 * Creates a cinema with the given name.
	 * Loads all relevant data for this cinema hall such as movie screenings and occupancy
	 * @param cinemaName The unique identifier for this cinema hall
	 * @param cineplex The name of the cineplex that this hall belongs to
	 * @param cinemaID The type of cinema hall that this cinema will be
	 */
	public Cinema(int cinemaName,String cineplex,int cinemaID) throws FileNotFoundException, ParseException{
		this.name = cinemaName;
		this.Cineplex = cineplex;
		File f = new File(this.path+this.Cineplex+"\\"+this.name);
		if (!f.exists()){
			f.mkdirs();
		}
		else{
			String l[] = f.list();
			String s2[];
			for (int i=0;i<l.length;i++){
				s2 = l[i].split("@",5);
				Date startDate = new SimpleDateFormat("yyyyMMddHHmm").parse(s2[0]);
				Date endDate = new SimpleDateFormat("yyyyMMddHHmm").parse(s2[1]);
				mlist.add(new MovieScreening(cinemaName, startDate, endDate, s2[2],TypeOfMovie.valueOf(s2[3]),this.Cineplex));
			}
		}
		switch(cinemaID){
			case 0: type = ClassOfCinema.REGULAR; break;
			case 1: type = ClassOfCinema.ATMOS; break;
			case 2: type = ClassOfCinema.PLATINUM; break;
		}
	}
	
	/**
	 * Adds a screening of a movie to this cinema hall.
	 * @param movie The movie title
	 * @param startDate The starting date and time for the movie screening to be added
	 * @param typeOfMovie The type of movie that this screening will be
	 * @return Returns true if adding is successful or false otherwise
	 */
	public boolean addMovie(String movie, Date startDate,TypeOfMovie typeOfMovie) throws IOException, ParseException {
		Date endDate = calculateEndDate(startDate, movie);
		String start = df.format(startDate);
		String end = df.format(endDate);
		File g = new File(this.path+this.Cineplex+"\\"+this.name+"\\"+start+"@"+end+"@"+movie+"@"+typeOfMovie+"@.txt");
		if (g.exists()) {
			System.out.println("Movie with similar showtime already exists");
			return false;
		}
		if(checkVacant(movie,startDate) == false){
			return false;
		}
		mlist.add(new MovieScreening(this.name, startDate, endDate, movie,typeOfMovie,this.Cineplex));
		g.createNewFile();
		FileWriter w = new FileWriter(this.path+this.Cineplex+"\\"+this.name+"\\"+start+"@"+end+"@"+movie+"@"+typeOfMovie+"@.txt",true);
		for (int i=0;i<80;i++){
			w.append("AAA \n");
		}
		w.flush();
		w.close();
		return true;
	}
	
	/**
	 * Lists all movies scheduled to be screen in this cinema
	 */
	public void listMovie(){
		for (int i=0;i<mlist.size();i++){
			if(mlist.get(i).showing == true)
				System.out.println(mlist.get(i).movie+ " showing at " + mlist.get(i).startDate );
		}
	}
	
	/**
	 * Lists the vacancy of a specific movie screening for this cinema
	 * @param index The index to be passed into the arrayList to iddentify which movie screening to list its vacancy
	 */
	public void listVacancy(int index) throws FileNotFoundException {
		mlist.get(index).listVacancy();
	}

	/**
	 * /**
	 * Converts a SeatID in string format to an integer
	 * @param SeatID The unique seat ID to be converted
	 * @return The integer form of the Seat ID
	 */
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

	/**
	 * Converts the string format of the SeatID into an integer
	 * Checks whether or not the seat is already taken
	 * @param index The index of the movie screening in the arrayList to be updated
	 * @param SeatID The seatID that the guest would wish to book
	 * @return Returns true if successfully updated, false otherwise
	 */
	public boolean updateVacancy(int index, String SeatID) throws IOException{
		int var = seatConversion(SeatID);
		if(var >19 && this.name ==3){
			System.out.println("Invalid seat");
			return false;
		}
			
		if(mlist.get(index).seats[var].taken){
			System.out.println("Seat already taken");
			return false;
		}
		mlist.get(index).updateVacancy(var);
		mlist.get(index).seats[var].taken = true;
		return true;
	}

	/**
	 * Calculates the ending time for any movie scheduled.
	 * @param startDate The starting time of a movie screening.
	 * @param movie The movie object which would be screened.
	 * @return The end date of the movie screening
	 */
	public Date calculateEndDate(Date startDate, String movie){
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.add(Calendar.MINUTE,MovieList.getMovieByTitle(movie).getDuration());
		Date end = cal.getTime();
		return end;
	}

	/**
	 * Deletes all movie screenings that are already over
	 * @param currentDate The current date
	 */
	public void delete(Date currentDate){
		String path = this.path+this.Cineplex+"\\"+this.name+"\\";
		File g = new File(path);
		for(int i=mlist.size()-1;i>=0;i--){
			if(mlist.get(i).startDate.compareTo(currentDate) < 0){
				g = new File(this.mlist.get(i).path);
				g.delete();
				this.mlist.remove(i);
			}
		}
	}

	/**
	 * Deletes a select movie screening from the mlist arrayList of this cinema based on the index given
	 * @param index The index of the array to be deleted
	 */
	public void deleteSelect(int index){
		File f = new File(this.mlist.get(index).path);
		f.delete();
		this.mlist.remove(index);
	}

	/**
	 * Deletes all movie screenings in this cinema with the given movie title
	 * @param movieTitle The title of the movie to be deleted
	 */
	public void deleteMovieName(String movieTitle){
		for(int i=mlist.size()-1; i>=0;i--){
			if(mlist.get(i).movie.equals(movieTitle)){
				deleteSelect(i);
			}
		}
	}

	/**
	 * Searches the mlist arrayList for the index of a movie screening given movie title and start date
	 * @param title The title of movie to look for
	 * @param startDate The starting date of the movie screening
	 * @return Returns the index of the mlist arrayList if a match is found
	 */
	public int search(String title,Date startDate){

		for(int i=0;i<this.mlist.size();i++){
			MovieScreening movieScreening = this.mlist.get(i);
			if(startDate.equals(movieScreening.startDate) && movieScreening.movie.equals(title)){
				return i;
			}
		}
		return -1;
	}

	/**
	 * Updates a pre-existing movie screening with a new date and time
	 * @param movie The name of the movie to be updated
	 * @param oldDate The starting date and time of the movie screening
	 * @param newDate The new date and time of the movie screening
	 * @return Returns true if update successful, false otherwise
	 */
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

	/**
	 * Checks if the current cinema can accomodate the new movie screening
	 * @param movie Title of movie to be accomodated
	 * @param startDate Date and time of the movie screening
	 * @return True if it is possible, false otherwise
	 */
	public boolean checkVacant(String movie,Date startDate) throws ParseException{
		Date endDate = calculateEndDate(startDate, movie);
		File f = new File(this.path+this.Cineplex+"\\"+this.name);
		String file[] = f.list();
		String s1;
		String s2[];
		Date checkStart;
		Date checkEnd;
		for (int i=0;i<file.length;i++) {
			if(file.length==0)
				break;
			s1 = file[i];
			s2 = s1.split("@",5);
			checkStart = df.parse(s2[0]);
			checkEnd = df.parse(s2[1]);
			if(startDate.compareTo(checkEnd) >= 0)
				continue;
			if (startDate.compareTo(checkEnd) < 0) {
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

	/**
	 * Getter method for hall number
	 * @return name of this cinema hall
	 */
	public int getName() {
		return this.name;
	}
	
	
	/**
	 * Sets the name of this cinema
	 * @param name The new name of this cinema
	 */
	public void setName(int name) {
		this.name = name;
	}

	/**
	 * Getter method returning the cineplex holding this hall
	 * @return the name of cineplex this cinema is located in
	 */
	public String getCineplex() {
		return this.Cineplex;
	}

	/**
	 * Sets the name of cineplex that this cinema is located in
	 * @param Cineplex String cineplex name
	 */
	public void setCineplex(String Cineplex) {
		this.Cineplex = Cineplex;
	}

	/**
	 * Getter method for directory of class
	 * @return the directory of the class
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * Sets a new directory for this class
	 * @param path The new directory for this class
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Returns the arrayList of movie screenings held in this cinema
	 * @return  List of movie screenings
	 */
	public List<MovieScreening> getMlist() {
		return mlist;
	}

	/**
	 * Assings a new list of screenings to this hall
	 * @param mlist the new list of movie screenings
	 */
	public void setMlist(List<MovieScreening> mlist) {
		this.mlist = mlist;
	}

	/**
	 * Getter method for dateformat tool
	 * @return the date format used through out this class
	 */
	public DateFormat getDf() {
		return this.df;
	}

	/**
	 * Sets a new date format to be used by this class
	 * @param df the new date format
	 */
	public void setDf(DateFormat df) {
		this.df = df;
	}

	/**
	 * Returns the type of cinema hall this is
	 * @return Returns type of cinema hall this is
	 */
	public ClassOfCinema getType() {
		return type;
	}

	/**
	 * Sets a new cinema type for this cinema hall
	 * @param type The new type of cinema 
	 */
	public void setType(ClassOfCinema type){
		this.type = type;
	}

	/**
	 * Returns the cinema code represented by its cineplex code and hall number
	 * @return a unique identifier for this cinema hall.
	 */
	public String getCinemaCode(){
		return "CN"+this.name;
	}
}
