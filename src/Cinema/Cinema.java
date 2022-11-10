package Cinema;
import movList.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cinema {
	int name;
	String path = System.getProperty("user.dir") + "\\src\\Cinema\\"; //CHANGE THIS STRING TO LOCATION OF THIS JAVA FILE
	List<MovieScreening> mlist = new ArrayList<>();

	public Cinema(int s) throws FileNotFoundException{
		this.name = s;
		boolean b;
		File f = new File(this.path+this.name);
		if (!f.exists())
			b = f.mkdir();
		else{
			String l[] = f.list();
			String s2[];
			int temp[] = {0,0,0};
			for (int i=0;i<l.length;i++){
				s2 = l[i].split("@",5);
				for (int k=0;k<3;k++){
					temp[k] = Integer.valueOf(s2[k]);
				}
				mlist.add(new MovieScreening(s, temp[0], temp[1], temp[2], s2[3]));
			}
		}

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
			if(Integer.valueOf(s2[0]) == date)
				continue;
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
		mlist.add(new MovieScreening(this.name, date, t, e, s));
		b=g.createNewFile();
		FileWriter w = new FileWriter(this.path+this.name+"\\"+date+"@"+t+"@"+e+"@"+s+"@.txt",true);
		/*w.append("O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n"
				+ "O O O O O O O O O O\n");*/
		for (int i=0;i<80;i++){
			w.append("A \n");
		}
		w.flush();
		w.close();
	}
	
	public void listMovie(){
		/*File f = new File(this.path+this.name);
		String l[] = f.list();
		String s;
		System.out.println("List of movie and showtime");
		for (int i=0;i<l.length;i++) {
			s = l[i];
			String k[] = s.split("@",5);
			System.out.println("Name of movie: "+k[3]+ " |ShowTime: "+ k[1]);
		}*/
		for (int i=0;i<mlist.size();i++){
			System.out.println(mlist.get(i).movie+ " showing at " + mlist.get(i).start );
		}
	}
	
	public void listVacancy(int index) throws FileNotFoundException {
		mlist.get(index).listVacancy();
		/*String s = movie.getTitle();
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
		sc.close();*/
		/*MovieScreening movS = mlist.get(index);
		for(int i=0;i<60;i++){
			if(movS.seats[i].taken == false)
				System.out.print("X");
			else
				System.out.print("O");
			if(i-(i/10)*10 !=9)
				System.out.print(" ");
			else
			System.out.print("\n");
		}
		for(int i=60;i<80;i++){
			if(movS.seats[i].taken == false)
				System.out.print("XXX");
			else
				System.out.print("OOO");
			if(i-(i/10)*10 ==9 || i-(i/10)*10 ==4 && i != 60)
				System.out.print("\n");
			else
				System.out.print(" ");
		}*/
	}

	public void updateVacancy(int index, String SeatID) throws IOException{
		/*String m = movie.getTitle();
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
		String h[] = SeatID.split(" ",2);
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
		sc.close();*/
		int del = SeatID.charAt(0)-65;
		System.out.println(del);
		int var;
		if(del<6){
			var = del*10+Integer.valueOf(SeatID.charAt(1))-48;
		}
		else{
			var = 60;
			del -=5;
			var +=del*5;
			var +=Integer.valueOf(SeatID.charAt(1))-48;
		}
		System.out.println(var);
		if(mlist.get(index).seats[var].taken){
			System.out.println("Seat already taken");
			return;
		}
		mlist.get(index).updateVacancy(var);
		System.out.println("Successfully updated");
		return;
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
