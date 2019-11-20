package chap7_8;
import java.io.*;
import java.net.*;

public class DayTimeClient {
	public static void main(String args[])
	{
		Socket theSocket = null;
		String host;
		InputStream is;
		BufferedReader reader;
		BufferedWriter writer = null;
		PrintWriter out = null;
		if(args.length > 0) host = args[0];
		else host = "localhost";
		try {
			theSocket = new Socket(host, 13);
			is = theSocket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is));
			String theTime = reader.readLine();
			System.out.println("현재 시간은 " + theTime + "이다.");
			OutputStream os = theSocket.getOutputStream();
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));
			out.println("Thank You!\n");
			out.flush();
		}catch(UnknownHostException e) {
			System.err.println(e);
		}catch(IOException e1) {
			System.err.println(e1);
		}finally {
			try {
				if (writer != null) theSocket.close();
			}catch(IOException e) {
				System.err.println(e);
			}
		}
	}
}
