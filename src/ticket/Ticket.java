/**
 * Represents a ticket that can be held by a movie goer
 * @author Pock Xuan
 * @version 1.1
 * @since 2022-11-13
 */
package ticket;

import movList.MovieList;
import java.util.Date;
import java.util.Calendar;

public class Ticket {

	/**
	 * Name of the user who booked the ticket
	 */
	private String clientName;

	/**
	 * Contact of the user who booked the ticket
	 */
	private String clientContact;

	/**
	 * Movie Title of the ticket booked
	 */
	private String movieTitle;

	/**
	 * The seat ID in the cinema
	 */
	private String seatID;

	/**
	 * A unique ID tied to the ticket 
	 */
	private String ticketID;

	/**
	 * The type of movie being screened
	 */
	private TypeOfMovie typeOfMovie;

	/**
	 * The class of cinema the movie is being screened in
	 */
	private ClassOfCinema classOfCinema;

	/**
	 * The age group of the viewer
	 */
	private AgeGroup ageGroup;

	/**
	 * The seat type of the ticket e.g. normal/ couple
	 */
	private SeatType seatType;
	
	/**
	 * The date in which the movie is being screened
	 */
	private Date date;

	/**
	 * The price of the ticket
	 */
	private double price;

	/**
	 * To check if a movie is a blockbuster
	 * it is not implemented within TypeOfMovie as a movie 
	 * can be both a block buster and 3D at the same time
	 */
	private boolean isBlockBuster;

	/**
	 * To check if a movie is a preview screening
	 */
	private boolean isPreview;

	/**
	 * How many tickets have been transacted in the current application
	 */
	private static Integer ticketCount=0;

	/**
	 * Constructor Class for Ticket
	 * @param clientName user name
	 * @param clientContact user contact details
	 * @param movieTitle title of the movie being screened
	 * @param seatID where the seat is at 
	 * @param transactionID a unique ID corresponding to the transaction when booking ticket
	 * @param typeOfMovie 3D and Digital
	 * @param classOfCinema Regular/ Atmos/ Platinum
	 * @param date a Date object containing time
	 * @param ageGroup	Student/ Adult/ Senior
	 * @param seatType Normal/ Couple/ Elite/ Ultim
	 * @param isBlockBuster boolean to check if a movie is a block buster
	 * @param isPreview boolean to check if a movie is preview screening
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
	 * Class method to calculate price of a ticket without creating the object yet
	 * @param classOfCinema Regular/ Atmos/ Platinum
	 * @param typeOfMovie 3D and Digital
	 * @param ageGroup	Student/ Adult/ Senior
	 * @param seatType Normal/ Couple/ Elite/ Ultima
	 * @param date a Date object containing time
	 * @param isBlockBuster boolean to check if a movie is a block buster
	 * @param isPreview boolean to check if a movie is preview screening
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
		//check if date is a public holiday
		if(PriceTable.isPH(date)){
			price=PriceTable.checkPrice(classOfCinema, Day.PH, AgeGroup.ADULT,seatType, typeOfMovie);
		}
		//weekdays after 6 no student/ senior promo 
		else if((ageGroup==AgeGroup.STUDENT || ageGroup==AgeGroup.SENIOR) && timeOfMovie>1800 &&
		(dayOfWeek==Day.MONDAY||dayOfWeek==Day.TUESDAY||dayOfWeek==Day.WEDNESDAY||dayOfWeek==Day.THURSDAY)){
			price=PriceTable.checkPrice(classOfCinema, dayOfWeek, AgeGroup.ADULT,seatType, typeOfMovie);
		}
		else if(dayOfWeek==Day.FRIDAY && timeOfMovie>1800){
			price=PriceTable.checkPrice(classOfCinema, Day.SATURDAY, ageGroup,seatType, typeOfMovie);
		}
		else{
			price=PriceTable.checkPrice(classOfCinema, dayOfWeek, ageGroup, seatType,typeOfMovie);
		}
		if(isBlockBuster) price++;
		if(isPreview) price++;
		return price;
	}

	/**
	 * Same as method above, overloaded to take in only ticket for easier computation
	 * @param ticket
	 * @return corresponding price of ticket
	 */
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

	/**
	 * Getter for the Date object
	 * @return date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Getter for the ticket ID which is a unique ID tag to the ticket itself
	 * @return ticket ID
	 */
	public String getTicketID() {
		return ticketID;
	}

	/**
	 * Getter for the seat type
	 * @return seat type
	 */
	public SeatType getSeatType() {
		return seatType;
	}

	/**
	 * Getter for the preview status
	 * @return preview status
	 */
	public boolean isPreview() {
		return isPreview;
	}

	/**
	 * Getter for block buster status 
	 * @return block buster status
	 */
	public boolean isBlockBuster() {
		return isBlockBuster;
	}

	/**
	 * Getting for movie title 
	 * @return movie title
	 */
	public String getMovieTitle() {
		return movieTitle;
	}

	/**
	 * Getter for the type of movie 
	 * @return type of movie
	 */
	public TypeOfMovie getTypeOfMovie() {
		return typeOfMovie;
	}

	/**
	 * Getter for class of cinema
	 * @return class of cinema
	 */
	public ClassOfCinema getClassOfCinema() {
		return classOfCinema;
	}

	/**
	 * Getter for Client name who booked the ticket
	 * @return client name
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * Getter for Client contact who booked the ticket
	 * @return
	 */
	public String getClientContact() {
		return clientContact;
	}

	/**
	 * Getter for age group for the ticket
	 * @return age group
	 */
	public AgeGroup getAgeGroup() {
		return ageGroup;
	}

	/**
	 * Getter for cost of ticket
	 * @return price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Getter for seat ID
	 * @return seat ID
	 */
	public String getSeatID() {
		return seatID;
	}

}
