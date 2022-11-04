package Cineplex;
package cinema;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Cineplex {
    public static int MovieListCounter=0;
    final private static String path = Path.of("").toAbsolutePath().toString()

    public Cineplex(){
		boolean b;
		File f = new File(this.path+"MovieList");
		if (!f.exists()){
			b = f.mkdir();
        }
	}

    public void AddMovie(String MovieName,String Type,String MovieRating,String ShowingStatus) throws IOException {
		File g = new File(this.path+"MovieList\\"+MovieName+"@.txt");
		if (g.exists()) {
			System.out.println("Movie already exists");
			return;
		}
		FileWriter w = new FileWriter(this.path+"MovieList\\"+MovieName+"@.txt",true);
		w.append( Type + "\n" + MovieRating + "\n" + ShowingStatus + "\n");
		w.close();
        this.MovieListCounter++;
	}

    public void RemoveMovie(String MovieName) throws IOException {
		boolean b;
        File g = new File(this.path+"MovieList\\"+MovieName+"@"+"@.txt");
		if (g.exists()) {
            b=g.delete();
            this.MovieListCounter--;
			System.out.println("Movie has been deleted.");
            return;
		}
    }

    public void ListMovie() throws IOException {
        File[] movFolder = new File(this.path+"MovieList").listFiles();
        for(File it : movFolder) System.out.println(it.getName());
    }
}