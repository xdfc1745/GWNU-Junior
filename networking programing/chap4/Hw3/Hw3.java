package chapter4;

import java.io.*;

public class Hw3 {
	
	public static void main(String args[]) {
		String text;
		char[] buffer = new char[80];
			try {
				FileReader fr = new FileReader(args[0]);
				FileReader fr2 = new FileReader(args[1]);
				BufferedReader br = new BufferedReader(fr);
				BufferedReader br2 = new BufferedReader(fr2);
				
				String name = args[0];
				int ind = name.indexOf(".");
				name = name.substring(0, ind);
				FileWriter fw = new FileWriter(name + args[1]+".txt");
				BufferedWriter bw = new BufferedWriter(fw);
				
				while((text = br.readLine()) != null) {
					bw.write(text  + "\r\n");
					bw.flush();
				}
				while((text = br2.readLine()) != null) {
					bw.write(text  + "\r\n");
					bw.flush();
				}
				
				fr.close();
				fw.close();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println(e);
			}
			
			
	}
}
