package src.ticket;

public class Ticket {

	private String movieTitle;
	private TypeOfMovie typeOfMovie;
	private ClassOfCinema classOfCinema;
	private String clientName;
	private String clientContact;
	private Day dayOfWeek;
	private int timeOfMovie; // 4digit int HHMM in a 24H system
	private AgeGroup ageGroup;
	private double price;
	private String seatID;
	private boolean isBlockBuster;
	/**
	 * Constructor class for ticket
	 * 
	 * @param typeOfMovie
	 * @param classOfCinema
	 * @param clientName
	 * @param clientContact
	 * @param dayOfWeek
	 * @param timeOfMovie
	 * @param ageGroup
	 * @param price
	 */
	
	public Ticket(String movieTitle, TypeOfMovie typeOfMovie, ClassOfCinema classOfCinema, String clientName,
			String clientContact, Day dayOfWeek,
			int timeOfMovie, AgeGroup ageGroup, String seatID, boolean isBlockBuster) {

		this.movieTitle = movieTitle;
		this.typeOfMovie = typeOfMovie;
		this.classOfCinema = classOfCinema;
		this.clientName = clientName;
		this.clientContact = clientContact;
		this.dayOfWeek = dayOfWeek;
		this.timeOfMovie = timeOfMovie;
		this.ageGroup = ageGroup;
		this.seatID = seatID;
		this.isBlockBuster=isBlockBuster;
		this.price = Ticket.calculatePrice(this);
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
	public static double calculatePrice(ClassOfCinema classOfCinema, TypeOfMovie typeOfMovie, AgeGroup ageGroup,
			Day dayOfWeek, int timeOfMovie,boolean isBlockBuster) {
			
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
		PriceTable priceTable = new PriceTable();
		//weekdays after 6 no student/ senior promo 
		if((ageGroup==AgeGroup.STUDENT || ageGroup==AgeGroup.SENIOR) && timeOfMovie>1800 &&
		(dayOfWeek==Day.MONDAY||dayOfWeek==Day.TUESDAY||dayOfWeek==Day.WEDNESDAY||dayOfWeek==Day.THURSDAY)){
			price=priceTable.checkPrice(classOfCinema, dayOfWeek, AgeGroup.ADULT, typeOfMovie);
		}
		if(dayOfWeek==Day.FRIDAY && timeOfMovie>1800){
			price=priceTable.checkPrice(classOfCinema, Day.SATURDAY, ageGroup, typeOfMovie);
		}
		else{
			price=priceTable.checkPrice(classOfCinema, dayOfWeek, ageGroup, typeOfMovie);
		}
		if(isBlockBuster) return price+1;
		return price;
	}

	//overload to take in only ticket
	public static double calculatePrice(Ticket ticket) {
			
		double price;
		PriceTable priceTable = new PriceTable();
		//weekdays after 6 no student/ senior promo 
		if((ticket.getAgeGroup()==AgeGroup.STUDENT || ticket.getAgeGroup()==AgeGroup.SENIOR) && ticket.getTimeOfMovie()>1800 &&
		(ticket.getDayOfWeek()==Day.MONDAY||ticket.getDayOfWeek()==Day.TUESDAY||ticket.getDayOfWeek()==Day.WEDNESDAY||ticket.getDayOfWeek()==Day.THURSDAY)){
			price=priceTable.checkPrice(ticket.getClassOfCinema(), ticket.getDayOfWeek(), AgeGroup.ADULT, ticket.getTypeOfMovie());
		}
		if(ticket.getDayOfWeek()==Day.FRIDAY && ticket.getTimeOfMovie()>1800){
			price=priceTable.checkPrice(ticket.getClassOfCinema(), Day.SATURDAY, ticket.getAgeGroup(), ticket.getTypeOfMovie());
		}
		else{
			price=priceTable.checkPrice(ticket.getClassOfCinema(), ticket.getDayOfWeek(), ticket.getAgeGroup(), ticket.getTypeOfMovie());
		}
		if(ticket.isBlockBuster()) return price+1;
		return price;
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
