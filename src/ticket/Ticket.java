package ticket;

import movList.MovieList;
import java.util.Date;
import java.util.Calendar;

public class Ticket {

	private String clientName;
	private String clientContact;
	private String movieTitle;
	private String seatID;
	private String ticketID;

	private TypeOfMovie typeOfMovie;
	private ClassOfCinema classOfCinema;
	private AgeGroup ageGroup;
	private SeatType seatType;
	
	private Date date;

	private double price;

	private boolean isBlockBuster;
	private boolean isPreview;

	private static Integer ticketCount=0;

	/**
	 * 
	 * @param clientName
	 * @param clientContact
	 * @param movieTitle
	 * @param seatID
	 * @param transactionID
	 * @param typeOfMovie
	 * @param classOfCinema
	 * @param dayOfWeek
	 * @param ageGroup
	 * @param seatType
	 * @param timeOfMovie
	 * @param isBlockBuster
	 * @param isPreview
	 */
	public Ticket(String clientName, String clientContact, String movieTitle, String seatID, String transactionID,
			TypeOfMovie typeOfMovie, ClassOfCinema classOfCinema, Date date, AgeGroup ageGroup, SeatType seatType,
			boolean isBlockBuster, boolean isPreview) {
		this.clientName = clientName;
		this.clientContact = clientContact;
		this.movieTitle = movieTitle;
		this.seatID = seatID;
		this.ticketID = transactionID+ticketCount.toString();
		this.typeOfMovie = typeOfMovie;
		this.classOfCinema = classOfCinema;
		this.date=date;
		this.ageGroup = ageGroup;
		this.seatType = seatType;
		this.isBlockBuster = isBlockBuster;
		this.isPreview = isPreview;
		this.price=Ticket.calculatePrice(this);
		ticketCount++;
		MovieList.incTicketSales(movieTitle);
	}

	/**
	 * Functions to calculate price of ticket based on class of cinema and type of
	 * movie
	 * For Cinema Class there is Regular, Atmos and Platinum
	 * For Type of movie there is regular/ digital, block buster and 3D
	 * There are no 3D movie for Atmos and Platinum class
	 * I based the price as closely as I can to the original Cathay website
	 * https://www.cathaycineplexes.com.sg/faqs/
	 * Remaining prices that are not reflected in the website such as 3D movies in
	 * general, platinum weekends are just a rough guess
	 */

	

	/**
	 * Class method to calculate price of a ticket without creating the object yet
	 * @param classOfCinema, Regular/ Atmos/ Platinum
	 * @param typeOfMovie 3D and Digital
	 * @param ageGroup	Student/ Adult/ Senior
	 * @param dayOfWeek Mon-Sun
	 * @param timeOfMovie 4digit int in HHMM 24H clock
	 * @param isBlockBuster boolean to check if a movie is a block buster
	 * @return corresponding price of ticket
	 */
	public static double calculatePrice(ClassOfCinema classOfCinema, TypeOfMovie typeOfMovie, AgeGroup ageGroup, SeatType seatType,
			Date date,boolean isBlockBuster, boolean isPreview) {
			
		/**
		 * Regular cinema
		 * Mon-Thurs, Fri before 6
		 * 9 Student 3D 
		 * 8 Student Blockuster 
		 * 7 Student Digital  
		 * 5 Senior Digital 
		 * 12 Adult 3D
		 * 11 Adult Blockbuster
		 * 10 Adult Digital
		 * 
		 * Fri after 6, Weekends & PH
		 * 18 Adult 3D
		 * 15.50 Adult Blockbuster
		 * 14.50 Adult Digital
		 * 
		 * Platinum Cinema
		 * Weekends & PH
		 * 38 Blockbuster
		 * 35 Digital
		 * 
		 * Weekdays
		 * 16 Student
		 * 28 Adult Blockbuster
		 * 25 Adult Digital
		 * 
		 * Atmos Cinema
		 * Weekends & PH
		 * 17 Blockbuster
		 * 15.50 Digital
		 * 
		 * Weekday
		 * 15.50 Blockbuster
		 * 14 Digital
		 */
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day= calendar.get(Calendar.DAY_OF_WEEK);
		Day dayOfWeek = Day.values()[day];
		int timeOfMovie=calendar.get(Calendar.HOUR_OF_DAY)*100+calendar.get(Calendar.MINUTE);
		double price;
		//weekdays after 6 no student/ senior promo 
		if((ageGroup==AgeGroup.STUDENT || ageGroup==AgeGroup.SENIOR) && timeOfMovie>1800 &&
		(dayOfWeek==Day.MONDAY||dayOfWeek==Day.TUESDAY||dayOfWeek==Day.WEDNESDAY||dayOfWeek==Day.THURSDAY)){
			price=PriceTable.checkPrice(classOfCinema, dayOfWeek, AgeGroup.ADULT,seatType, typeOfMovie);
		}
		if(dayOfWeek==Day.FRIDAY && timeOfMovie>1800){
			price=PriceTable.checkPrice(classOfCinema, Day.SATURDAY, ageGroup,seatType, typeOfMovie);
		}
		else{
			price=PriceTable.checkPrice(classOfCinema, dayOfWeek, ageGroup, seatType,typeOfMovie);
		}
		if(isBlockBuster) price++;
		if(isPreview) price++;
		return price;
	}

	//overload to take in only ticket
	public static double calculatePrice(Ticket ticket) {
			
		double price;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(ticket.getDate());
		int day= calendar.get(Calendar.DAY_OF_WEEK);
		Day dayOfWeek = Day.values()[day];
		int timeOfMovie=calendar.get(Calendar.HOUR_OF_DAY)*100+calendar.get(Calendar.MINUTE);
		//weekdays after 6 no student/ senior promo 
		if((ticket.getAgeGroup()==AgeGroup.STUDENT || ticket.getAgeGroup()==AgeGroup.SENIOR) && timeOfMovie>1800 &&
		(dayOfWeek==Day.MONDAY||dayOfWeek==Day.TUESDAY||dayOfWeek==Day.WEDNESDAY||dayOfWeek==Day.THURSDAY)){
			price=PriceTable.checkPrice(ticket.getClassOfCinema(), dayOfWeek, AgeGroup.ADULT, ticket.getSeatType(),ticket.getTypeOfMovie());
		}
		if(dayOfWeek==Day.FRIDAY && timeOfMovie>1800){
			price=PriceTable.checkPrice(ticket.getClassOfCinema(), Day.SATURDAY, ticket.getAgeGroup(), ticket.getSeatType(),ticket.getTypeOfMovie());
		}
		else{
			price=PriceTable.checkPrice(ticket.getClassOfCinema(), dayOfWeek, ticket.getAgeGroup(),ticket.getSeatType(), ticket.getTypeOfMovie());
		}
		if(ticket.isBlockBuster()) price++;
		if(ticket.isPreview) price++;
		return price;
	}

	public Date getDate() {
		return date;
	}

	public String getTicketID() {
		return ticketID;
	}

	public SeatType getSeatType() {
		return seatType;
	}

	public boolean isPreview() {
		return isPreview;
	}

	public boolean isBlockBuster() {
		return isBlockBuster;
	}

	public String getMovieTitle() {
		return movieTitle;
	}

	public TypeOfMovie getTypeOfMovie() {
		return typeOfMovie;
	}

	public ClassOfCinema getClassOfCinema() {
		return classOfCinema;
	}

	public String getClientName() {
		return clientName;
	}

	public String getClientContact() {
		return clientContact;
	}

	public AgeGroup getAgeGroup() {
		return ageGroup;
	}

	public double getPrice() {
		return price;
	}

	public String getSeatID() {
		return seatID;
	}

}
