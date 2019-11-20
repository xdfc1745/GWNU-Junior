package chap7_8;
import java.io.*;
import java.net.*;
import java.util.Date;

public class DayTimeServer {
	public final static int daytimeport = 13;
	public static void main(String args[])
	{
		ServerSocket theServer;
		Socket theSocket = null, reSocket;
		BufferedWriter writer;
		BufferedReader read = null;
		String host;
		try {
			theServer = new ServerSocket(daytimeport);
			while(true) {
				try {
					theSocket = theServer.accept();
					OutputStream os = theSocket.getOutputStream();
					writer = new BufferedWriter(new OutputStreamWriter(os));
					Date now = new Date();
					writer.write(now.toString()+"\r\n");
					writer.flush();
					
					theSocket.shutdownOutput();
					read = new BufferedReader(new InputStreamReader(theSocket.getInputStream()));
					String bye = null;
					bye = read.readLine();
					System.out.println("come = " + bye);

				}catch(IOException e) {
					System.out.println(e +"  1");
				}finally {
					try {
						if(theSocket != null) theSocket.close();
					}catch(IOException e) {
						System.out.println(e);
					}
				}
			}
		}catch(IOException e) {
			System.out.println(e + "  2");
		}
	}
}
