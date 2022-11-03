
public class Ticket {

	private TypeOfMovie typeOfMovie;
	private ClassOfCinema classOfCinema;
	private String clientName;
	private String clientContact;
	private Day dayOfWeek;
	private int[] timeOfMovie; //2-tuple of int (hour,min) in a 24H system
	private int ageOfMovieGoer;
	private double price;
	private boolean isStudent;

	public Ticket(TypeOfMovie typeOfMovie, ClassOfCinema classOfCinema, String clientName, String clientContact, Day dayOfWeek,
			int[] timeOfMovie, int ageOfMovieGoer, double price) {
		
		this.typeOfMovie = typeOfMovie;
		this.classOfCinema = classOfCinema;
		this.clientName = clientName;
		this.clientContact = clientContact;
		this.dayOfWeek = dayOfWeek;
		this.timeOfMovie = timeOfMovie;
		this.ageOfMovieGoer = ageOfMovieGoer;
		this.price=this.calculatePrice();
		
	}
	/*
	 * Function to calculate price of ticket based on class of cinema and type of movie
	 * For Cinema Class there is Regular, Atmos and Platinum
	 * For Type of movie there is regular/ digital, block buster and 3D
	 * There are no 3D movie for Atmos and Platinum class
	 * I based the price as closely as I can to the original Cathay website
	 * https://www.cathaycineplexes.com.sg/faqs/
	 * Remaining prices that are not reflected in the website such as 3D movies in general, platinum weekends are just a rough guess
	 */
	private double calculatePrice() {

		//Regular Cinema
		if(this.classOfCinema==ClassOfCinema.REGULAR) {
			switch(this.dayOfWeek) {
			
			case THURSDAY:
				//student
				if(this.isStudent==true && this.timeOfMovie[0]<18) {
					//3D movie
					if(this.typeOfMovie==TypeOfMovie.D3) return 9.00;
					//regular and digital are $1 cheaper than blockbuster 
					else {
						if(this.typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 8.00;
						else return 7.00;
					}
				}
				//senior citizen
				if(this.ageOfMovieGoer>55 && this.timeOfMovie[0]<18 && this.classOfCinema==ClassOfCinema.REGULAR)	return 5.00;
				//adult
				if(this.typeOfMovie==TypeOfMovie.D3) 	return 12.00;
				else {
					if(this.typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 11.00;
					else return 10.00;
				}

			case FRIDAY:
				//student
				if(this.isStudent==true && this.timeOfMovie[0]<18) {
					if(this.typeOfMovie==TypeOfMovie.D3) return 9.00;
					else {
						if(this.typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 8.00;
						else return 7.00;
					}
				}
				//senior citizen
				if(this.ageOfMovieGoer>55 && this.timeOfMovie[0]<18 && this.classOfCinema==ClassOfCinema.REGULAR)	return 5.00;
				//3D movies on Fridays are regardless of timing
				if(this.typeOfMovie==TypeOfMovie.D3) {
					return 18.00;
				}
				else {
					//before 6pm regular & digital
					if(this.timeOfMovie[0]<18) return 10.00;
					//after 6pm regular & digital
					else {
						if(this.typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 15.50;
						else return 14.50;
					}
				}

				//weekends and public holiday
			case PH:
			case SATURDAY:
			case SUNDAY:
				//3D movies
				if(this.typeOfMovie==TypeOfMovie.D3) 	return 18.00;
				//regular and digital are $1 cheaper than blockbuster 
				else {
					if(this.typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 15.50;
					else return 14.50;
				}

				// Monday, Tuesday, Wednesday
			default:
				//student
				if(this.isStudent==true && this.timeOfMovie[0]<18) {
					if(this.typeOfMovie==TypeOfMovie.D3) return 9.00;
					else {
						if(this.typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 8.00;
						else return 7.00;
					}
				}
				//senior citizen
				if(this.ageOfMovieGoer>55 && this.timeOfMovie[0]<18 && this.classOfCinema==ClassOfCinema.REGULAR)	return 5.00;
				//adult
				if(this.typeOfMovie==TypeOfMovie.D3) 	return 12.00;
				else {
					if(this.typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 11.00;
					else return 10.00;
				}

			}
		}
		//Platinum Class Cinema
		else if(this.classOfCinema==ClassOfCinema.PLATINUM) {
			switch(this.dayOfWeek) {
				case FRIDAY:
				case SATURDAY:
				case SUNDAY:
					//no student/ senior citizen promo
					if(this.typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 38.00;
					else return 35.00;
				default:
					if(this.isStudent==true) return 16.00;
					if(this.typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 28.00;
					else return 25.00;
			}
		}
		//Atmos class
		else {
			switch(this.dayOfWeek) {
			case FRIDAY:
			case SATURDAY:
			case SUNDAY:
				if(this.typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 17.00;
				else return 15.50;
				
			default:
				if(this.typeOfMovie==TypeOfMovie.BLOCKBUSTER) return 15.50;
				else return 14.00;
		}
		}
	}

	public int[] getTimeOfMovie() {
		return timeOfMovie;
	}


	public void setTimeOfMovie(int[] timeOfMovie) {
		this.timeOfMovie = timeOfMovie;
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

	public double getPrice() {
		return price;
	}

	public int getAgeOfMovieGoer() {
		return ageOfMovieGoer;
	}

	public void setAgeOfMovieGoer(int ageOfMovieGoer) {
		this.ageOfMovieGoer = ageOfMovieGoer;
	}

}
