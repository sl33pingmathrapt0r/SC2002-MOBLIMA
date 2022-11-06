package src;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Cinema {
	int name;
	String path = System.getProperty("user.dir") + "\\src\\Cinema\\"; //CHANGE THIS STRING TO LOCATION OF THIS JAVA FILE
	
	public Cinema(int s){
		this.name = s;
		boolean b;
		File f = new File(this.path+this.name);
		if (!f.exists())
			b = f.mkdir();
	}
	
	public void AddMovie(Movie movie, int t, int date) throws IOException {
		String s = movie.getTitle();
		int e = calculateEndTime(t,movie);
		boolean b;
		File g = new File(this.path+this.name+"\\"+date+"@"+t+"@"+e+"@"+s+"@.txt");
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
			s2 = s1.split("@",5);
			if (Integer.valueOf(s2[1]) >= e || i == l.length-1) {
				if(i==0)
					break;
				s1 = l[i-1];
				s2 = s1.split("@",5);
				if (Integer.valueOf(s2[2]) > t) {
					System.out.println("Timing clash, failed to add movie");
					return;
				}
				else 
					break;
			}
		}
		b=g.createNewFile();
		FileWriter w = new FileWriter(this.path+this.name+"\\"+date+"@"+t+"@"+e+"@"+s+"@.txt",true);
		w.append("O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n");
		w.flush();
		w.close();
	}
	
	public void listMovie(){
		File f = new File(this.path+this.name);
		String l[] = f.list();
		String s;
		System.out.println("List of movie and showtime");
		for (int i=0;i<l.length;i++) {
			s = l[i];
			String k[] = s.split("@",5);
			System.out.println("Name of movie: "+k[3]+ " |ShowTime: "+ k[1]);
		}
	}
	
	public void listVacancy(Movie movie, int t, int date) throws FileNotFoundException {
		String s = movie.getTitle();
		int e = calculateEndTime(t, movie);
		File f = new File(this.path+this.name+"\\"+date+"@"+t+"@"+e+"@"+s+"@.txt");
		if(!f.exists()) {
			System.out.println("No such movie with the showtime exists");
			return;
		}
		Scanner sc = new Scanner(f);
		int i=1;
		System.out.println("Vacancy of "+s+" at "+t+" on "+date);
		System.out.println("   A B C D E F G H I J");
		System.out.println("   ___________________");
		while(sc.hasNextLine()) {
			System.out.println(i + " |" +sc.nextLine());
			i++;
		}
		sc.close();
	}

	public void updateVacancy(Movie movie,int t,Ticket ticket, int date) throws IOException{
		String s = ticket.getSeatID();
		String m = movie.getTitle();
		File f = new File(this.path+this.name);
		String l[] = f.list();
		String temp = "";
		for (int i=0;i<l.length;i++) {
			temp = l[i];
			String k[] = temp.split("@",5);
			if(Integer.valueOf(k[1])== t || k[3]==m)
				break;
			if (i==l.length-1){
				System.out.println("No such movie exists");
				return;
			}
		}
		File g = new File(this.path+this.name+"\\"+temp);
		Scanner sc = new Scanner(g);
		String h[] = s.split(" ",2);
		int del = h[1].charAt(0)-65;
		String new1="";
		StringBuffer buffer = new StringBuffer();
		for(int k=1;k<=5;k++){
			if(sc.hasNextLine()){
				if(k==Integer.valueOf(h[0])){
					String curr = sc.nextLine();
					for(int j=0;j<10;j++){
						if(j!=del)
							new1 = new1+curr.charAt(j*2);
						else
							new1 = new1+"X";
						if(j!=9)
							new1 = new1 + " ";
					}
					buffer.append(new1+System.lineSeparator());
					continue;
				}
				buffer.append(sc.nextLine()+System.lineSeparator());
			}
		}
		FileWriter wr = new FileWriter(g);
		BufferedWriter bw = new BufferedWriter(wr);
		bw.write(buffer.toString());
		bw.flush();
		bw.close();
		sc.close();
	}

	public String getCinemaCode(){
		return "CN"+this.name;
	}

	public int calculateEndTime(int startTime,Movie movie){
		int i = movie.getDuration();
		int hour = i/60;
		int min = i-(hour)*60;
		startTime = startTime + min;
		if(startTime-(startTime/100)*100 >60)
			startTime = startTime+40;
		startTime = startTime + hour*100;
		if(startTime>2500)
			startTime = startTime -2400;
		return startTime;	
	}
}
