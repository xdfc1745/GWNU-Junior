package chapter4;

import java.io.*;

public class Hw1 {


	public static void main(String args[])
	{
		
		int numberRead;
		char[] buffer = new char[80];
		String text;
		try {
			FileWriter fw = new FileWriter("file1.txt");
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			BufferedWriter bw = new BufferedWriter(fw);
			while ((text = br.readLine()) != null) {
				bw.write(text +"\r\n");
				bw.flush();
			}
			bw.close();
			FileReader fr = new FileReader("file1.txt");
			OutputStreamWriter osw = new OutputStreamWriter(System.out);
			PrintWriter pw = new PrintWriter(osw);
			
			fr.read(buffer);
		
			pw.println(buffer);
			pw.flush();
			
			fr.close();
			fw.close();
		
		}catch (IOException e) {
			System.err.println(e);
		}
	}
}
