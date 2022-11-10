import java.util.Scanner;

public class inputHandling {

    final private static Scanner sc = new Scanner(System.in);

    public static int getInt(String message) {
        int n;
        while(true){
            System.out.print(message);
            String str = sc.nextLine();
            try{
                n = Integer.parseInt(str);
                break;
            }
            catch(NumberFormatException e){
                System.out.printf("Input %s not a valid integer. \n", str);
            }
        }
        return n;
    }
}
