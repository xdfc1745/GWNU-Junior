package chap7_8;
import java.io.*;
import java.net.*;

public class EchoServer {
	public static void main(String args[]) 
	{
		ServerSocket theServer;
		Socket theSocket = null;
		InputStream is;
		BufferedReader reader;
		OutputStream os;
		BufferedWriter writer;
		String theLine;
		
		try {
			theServer = new ServerSocket(7);
			theServer.setReuseAddress(true);
			while(true) {
				theSocket = theServer.accept();
				System.out.println("port num = " + theSocket.getPort());
				Echo e = new Echo(theSocket);
				e.start();
			}
		} catch (IOException e1) {
			System.err.println(e1);
		}
		
	}
	
}

class Echo extends Thread {
	Socket theSocket = null;
	InputStream is;
	BufferedReader reader;
	OutputStream os;
	BufferedWriter writer;
	String theLine;
	public Echo(Socket connection) {
		try {
			theSocket = connection; 
			is = theSocket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is));
			os = theSocket.getOutputStream();
			writer = new BufferedWriter(new OutputStreamWriter(os));
		} catch (IOException e) {
			System.err.println(e);
		}
		
	}
	public void run() {
		try {
			while((theLine = reader.readLine()) != null) {
				System.out.println(theLine);
				writer.write(theLine+'\r'+'\n');
				writer.flush();
			}
		} catch (IOException e) {
			System.err.println(e);
		}finally {
				try{
					if (theSocket != null) 
						theSocket.close();
				}catch(IOException e) {
					System.out.println(e);
				}
		}
	}
}

