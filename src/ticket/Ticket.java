package src.ticket;

public class Ticket {

	private String movieTitle;
	private TypeOfMovie typeOfMovie;
	private ClassOfCinema classOfCinema;
	private String clientName;
	private String clientContact;
	private Day dayOfWeek;
	private int[] timeOfMovie; //2-tuple of int (hour,min) in a 24H system
	private AgeGroup ageGroup;
	private double price;
	private String seatID;

	/**
	 * Constructor class for ticket
	 * @param typeOfMovie
	 * @param classOfCinema
	 * @param clientName
	 * @param clientContact
	 * @param dayOfWeek
	 * @param timeOfMovie
	 * @param ageGroup
	 * @param price
	 */
	public Ticket(String movieTitle,TypeOfMovie typeOfMovie, ClassOfCinema classOfCinema, String clientName, String clientContact, Day dayOfWeek,
			int[] timeOfMovie, AgeGroup ageGroup, String seatID) {
		
		this.movieTitle=movieTitle;
		this.typeOfMovie = typeOfMovie;
		this.classOfCinema = classOfCinema;
		this.clientName = clientName;
		this.clientContact = clientContact;
		this.dayOfWeek = dayOfWeek;
		this.timeOfMovie = timeOfMovie;
		this.ageGroup = ageGroup;
		this.seatID=seatID;
		this.price=Ticket.calculatePrice(classOfCinema, typeOfMovie, ageGroup, dayOfWeek, timeOfMovie);
		
	}
	/**
	 * Functions to calculate price of ticket based on class of cinema and type of movie
	 * For Cinema Class there is Regular, Atmos and Platinum
	 * For Type of movie there is regular/ digital, block buster and 3D
	 * There are no 3D movie for Atmos and Platinum class
	 * I based the price as closely as I can to the original Cathay website
	 * https://www.cathaycineplexes.com.sg/faqs/
	 * Remaining prices that are not reflected in the website such as 3D movies in general, platinum weekends are just a rough guess
	 */
	
	/**
	 * Class method to check price of ticket without creating the object
	 * @param classOfCinema
	 * @param typeOfMovie
	 * @param ageGroup
	 * @param dayOfWeek
	 * @param timeOfMovie
	 * @return price of ticket
	 */
	private static double calculatePrice(ClassOfCinema classOfCinema,TypeOfMovie typeOfMovie,AgeGroup ageGroup,Day dayOfWeek, int[] timeOfMovie){
		

		//Regular Cinema
		if(classOfCinema==ClassOfCinema.REGULAR) {
			switch(dayOfWeek) {
			
			case THURSDAY:
				//student
				if(ageGroup==AgeGroup.STUDENT && timeOfMovie[0]<18) {
					//3D movie
					if(typeOfMovie==TypeOfMovie.D3) return 9.00;
					//regular and digital are $1 cheaper than blockbuster 
					else {
						if(typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 8.00;
						else return 7.00;
					}
				}
				//senior citizen
				if(ageGroup==AgeGroup.SENIOR && timeOfMovie[0]<18 && classOfCinema==ClassOfCinema.REGULAR)	return 5.00;
				//adult
				if(typeOfMovie==TypeOfMovie.D3) 	return 12.00;
				else {
					if(typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 11.00;
					else return 10.00;
				}

			case FRIDAY:
				//student
				if(ageGroup==AgeGroup.STUDENT && timeOfMovie[0]<18) {
					if(typeOfMovie==TypeOfMovie.D3) return 9.00;
					else {
						if(typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 8.00;
						else return 7.00;
					}
				}
				//senior citizen
				if(ageGroup==AgeGroup.SENIOR && timeOfMovie[0]<18 && classOfCinema==ClassOfCinema.REGULAR)	return 5.00;
				//3D movies on Fridays are regardless of timing
				if(typeOfMovie==TypeOfMovie.D3) {
					return 18.00;
				}
				else {
					//before 6pm regular & digital
					if(timeOfMovie[0]<18) return 10.00;
					//after 6pm regular & digital
					else {
						if(typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 15.50;
						else return 14.50;
					}
				}

				//weekends and public holiday
			case PH:
			case SATURDAY:
			case SUNDAY:
				//3D movies
				if(typeOfMovie==TypeOfMovie.D3) 	return 18.00;
				//regular and digital are $1 cheaper than blockbuster 
				else {
					if(typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 15.50;
					else return 14.50;
				}

				// Monday, Tuesday, Wednesday
			default:
				//student
				if(ageGroup==AgeGroup.STUDENT && timeOfMovie[0]<18) {
					if(typeOfMovie==TypeOfMovie.D3) return 9.00;
					else {
						if(typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 8.00;
						else return 7.00;
					}
				}
				//senior citizen
				if(ageGroup==AgeGroup.SENIOR && timeOfMovie[0]<18 && classOfCinema==ClassOfCinema.REGULAR)	return 5.00;
				//adult
				if(typeOfMovie==TypeOfMovie.D3) 	return 12.00;
				else {
					if(typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 11.00;
					else return 10.00;
				}

			}
		}
		//Platinum Class Cinema
		else if(classOfCinema==ClassOfCinema.PLATINUM) {
			switch(dayOfWeek) {
				case FRIDAY:
				case SATURDAY:
				case SUNDAY:
					//no student/ senior citizen promo
					if(typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 38.00;
					else return 35.00;
				default:
					if(ageGroup==AgeGroup.STUDENT) return 16.00;
					if(typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 28.00;
					else return 25.00;
			}
		}
		//Atmos class
		else {
			switch(dayOfWeek) {
			case FRIDAY:
			case SATURDAY:
			case SUNDAY:
				if(typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 17.00;
				else return 15.50;
				
			default:
				if(typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 15.50;
				else return 14.00;
		}
		}
	}

	public String getMovieTitle() {
		return movieTitle;
	}

	public void setMovieTitle(String movieTitle) {
		this.movieTitle = movieTitle;
	}

	public TypeOfMovie getTypeOfMovie() {
		return typeOfMovie;
	}

	public void setTypeOfMovie(TypeOfMovie typeOfMovie) {
		this.typeOfMovie = typeOfMovie;
	}

	public ClassOfCinema getClassOfCinema() {
		return classOfCinema;
	}

	public void setClassOfCinema(ClassOfCinema classOfCinema) {
		this.classOfCinema = classOfCinema;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientContact() {
		return clientContact;
	}

	public void setClientContact(String clientContact) {
		this.clientContact = clientContact;
	}

	public Day getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Day dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public int[] getTimeOfMovie() {
		return timeOfMovie;
	}

	public void setTimeOfMovie(int[] timeOfMovie) {
		this.timeOfMovie = timeOfMovie;
	}

	public AgeGroup getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(AgeGroup ageGroup) {
		this.ageGroup = ageGroup;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getSeatID() {
		return seatID;
	}

	public void setSeatID(String seatID) {
		this.seatID = seatID;
	}


}
