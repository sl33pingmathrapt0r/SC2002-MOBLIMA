package ticket;

import movList.MovieList;

public class Ticket {

	private String clientName;
	private String clientContact;
	private String movieTitle;
	private String seatID;
	private String ticketID;

	private TypeOfMovie typeOfMovie;
	private ClassOfCinema classOfCinema;
	private Day dayOfWeek;
	private AgeGroup ageGroup;
	private SeatType seatType;

	private int timeOfMovie; // 4digit int HHMM in a 24H system
	private double price;


	private boolean isBlockBuster;
	private boolean isPreview;

	private static Integer ticketCount=0;

	/**
	 * Constructor class for ticket
	 * @param clientName
	 * @param clientContact
	 * @param movieTitle
	 * @param seatID
	 * @param ticketID
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
			TypeOfMovie typeOfMovie, ClassOfCinema classOfCinema, Day dayOfWeek, AgeGroup ageGroup, SeatType seatType,
			int timeOfMovie, boolean isBlockBuster, boolean isPreview) {
		this.clientName = clientName;
		this.clientContact = clientContact;
		this.movieTitle = movieTitle;
		this.seatID = seatID;
		this.ticketID = transactionID+ticketCount.toString();
		this.typeOfMovie = typeOfMovie;
		this.classOfCinema = classOfCinema;
		this.dayOfWeek = dayOfWeek;
		this.ageGroup = ageGroup;
		this.seatType = seatType;
		this.timeOfMovie = timeOfMovie;
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
			Day dayOfWeek, int timeOfMovie,boolean isBlockBuster, boolean isPreview) {
			
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
		PriceTable priceTable = new PriceTable();
		//weekdays after 6 no student/ senior promo 
		if((ticket.getAgeGroup()==AgeGroup.STUDENT || ticket.getAgeGroup()==AgeGroup.SENIOR) && ticket.getTimeOfMovie()>1800 &&
		(ticket.getDayOfWeek()==Day.MONDAY||ticket.getDayOfWeek()==Day.TUESDAY||ticket.getDayOfWeek()==Day.WEDNESDAY||ticket.getDayOfWeek()==Day.THURSDAY)){
			price=PriceTable.checkPrice(ticket.getClassOfCinema(), ticket.getDayOfWeek(), AgeGroup.ADULT, ticket.getSeatType(),ticket.getTypeOfMovie());
		}
		if(ticket.getDayOfWeek()==Day.FRIDAY && ticket.getTimeOfMovie()>1800){
			price=PriceTable.checkPrice(ticket.getClassOfCinema(), Day.SATURDAY, ticket.getAgeGroup(), ticket.getSeatType(),ticket.getTypeOfMovie());
		}
		else{
			price=PriceTable.checkPrice(ticket.getClassOfCinema(), ticket.getDayOfWeek(), ticket.getAgeGroup(),ticket.getSeatType(), ticket.getTypeOfMovie());
		}
		if(ticket.isBlockBuster()) price++;
		if(ticket.isPreview) price++;
		return price;
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

	public Day getDayOfWeek() {
		return dayOfWeek;
	}

	public int getTimeOfMovie() {
		return timeOfMovie;
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
