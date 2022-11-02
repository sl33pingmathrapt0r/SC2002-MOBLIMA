package src.usr;

// import tickets.*;

public class MovieGoer extends User {
    private String username;
    private String pw;

    private String[] particulars= new String[3];
    // {name, mobile number, email address}
    // private ArrayList<Ticket> bookingHistory= new ArrayList<Ticket>();


    MovieGoer(String username, String pw, String name, String hp, String email) {
        this.username= username;
        this.pw= pw;
        particulars[0]= name;
        particulars[1]= hp;
        particulars[2]= email;
    }


    public String getUser() {
        return username;
    }

    boolean checkPW(String pw) {
        return (this.pw.equals(pw)) ? true : false;
    }

    // TODO
    // public void viewMovieDetails(Movie movie) {

    // }

    public void selectSeat() {

    }

    public void bookTicket() {

    }

    // public void viewBookingHistory() {
    //     for (int i=0; i<bookingHistory.size(); i++) {
    //         System.out.println( 
    //             (i+1) + ":\n" + 
    //             "\t" + bookingHistory.get(i).getCinema() + "\n" +
    //             "\t" + bookingHistory.get(i).getMovie() + "\n" +
    //             "\t" + bookingHistory.get(i).getTime() + "\n" +
    //             "\t" + bookingHistory.get(i).getSeat() + "\n"
    //             );
    //     }
    // }

    public void listTop5Movies() {
        
    }
}
