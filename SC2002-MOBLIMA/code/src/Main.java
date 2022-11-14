package src;

import java.io.IOException;
import java.text.ParseException;

import src.ticket.PriceTable;
import src.usr.Accounts;

/**
  Main class from which the programme will start running
  @author Jun Xiong
  @version 1.1
  @since 2022-11-13
 */
public class Main {
    /**
     * constant number of cineplexes required by assignment
     */
    final static int MAX_CINEPLEX = 3;

    /**
     * main method to start running programme
     * @param args standard main method parameters
     */
    public static void main(String[] args) throws IOException, ParseException {
        // APPLICATION STARTUP
        PriceTable.initPriceTable();
        Accounts.load();
        loginApp.loginMenu();
    }
}