import java.io.IOException;
import java.text.ParseException;

import ticket.PriceTable;

public class Main {
    final static int MAX_CINEPLEX = 3;
    public static void main(String[] args) throws IOException, ParseException {
        // APPLICATION STARTUP
        PriceTable.initPriceTable();
        usr.Accounts.load();
        loginApp.loginMenu();
    }
}