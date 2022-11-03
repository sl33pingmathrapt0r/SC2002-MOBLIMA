package src;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Cinema {
	int name;
	String path = "C:\\Users\\yeozo\\eclipse-workspace\\sce.sc2002.zh.first\\src\\cinema\\"; //CHANGE THIS STRING TO LOCATION OF THIS JAVA FILE
	
	public Cinema(int s){
		this.name = s;
		boolean b;
		File f = new File(this.path+this.name);
		if (!f.exists())
			b = f.mkdir();
	}
	
	public void AddMovie(String s, int t, int e) throws IOException {
		boolean b;
		File g = new File(this.path+this.name+"\\"+t+"@"+e+"@"+s+"@.txt");
		if (g.exists()) {
			System.out.println("Movie with similar showtime already exists");
			return;
		}
		File f = new File(this.path+this.name);
		String l[] = f.list();
		String s1;
		String s2[];
		for (int i=0;i<l.length;i++) {
			s1 = l[i];
			s2 = s1.split("@",4);
			if (Integer.valueOf(s2[0]) >= e || i == l.length-1) {
				if(i==0)
					break;
				s1 = l[i-1];
				s2 = s1.split("@",4);
				if (Integer.valueOf(s2[1]) > t) {
					System.out.println("Timing clash, failed to add movie");
					return;
				}
				else 
					break;
			}
		}
		b=g.createNewFile();
		FileWriter w = new FileWriter(this.path+this.name+"\\"+t+"@"+e+"@"+s+"@.txt",true);
		w.append("O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n");
		w.close();
	}
	
	public void listMovie(){
		File f = new File(this.path+this.name);
		String l[] = f.list();
		String s;
		System.out.println("List of movie and showtime");
		for (int i=0;i<l.length;i++) {
			s = l[i];
			String k[] = s.split("@",4);
			System.out.println("Name of movie: "+k[2]+ " |ShowTime: "+ k[0]);
		}
	}
	
	public void listVacancy(String s, int t, int e) throws FileNotFoundException {
		File f = new File(this.path+this.name+"\\"+t+"@"+e+"@"+s+"@"+".txt");
		if(!f.exists()) {
			System.out.println("No such movie with the showtime exists");
			return;
		}
		Scanner sc = new Scanner(f);
		int i=1;
		System.out.println("Vacancy of "+s+" at "+t);
		System.out.println("   A B C D E F G H I J");
		System.out.println("   ___________________");
		while(sc.hasNextLine()) {
			System.out.println(i + " |" +sc.nextLine());
			i++;
		}
	}
}
