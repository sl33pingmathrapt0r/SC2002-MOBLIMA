package src.usr;

import java.util.*;

class Admin extends User {
    private boolean isAdmin= true;
    private String username;
    private String pw;

    private static Scanner scan= new Scanner(System.in);

    Admin(String username, String pw) {
        this.username= username;
        this.pw= pw;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getUser() {
        return username;
    }

    boolean checkPW(String pw) {
        return (this.pw.equals(pw)) ? true : false;
    }

    // TODO
    public void createMovieListing() {

    }

    public void updateMovieListing() {

    }

    public void deleteMovieListing() {

    }

    public void createCinemaShowtimes() {

    }

    public void updateCinemaListing() {

    }

    public void deleteCinemaListing() {

    }

    public void configureSystemSettings() {
        // ???
    }
}