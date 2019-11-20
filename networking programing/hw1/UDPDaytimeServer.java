package chap11_server;
import java.io.*;
import java.net.*;
import java.util.Date;

public class UDPDaytimeServer {
	public final static int PORT = 13;
	protected static DatagramSocket socket;
	static DatagramPacket sendPacket, receivePacket;
	public static String hostname = "localhost";
	
	public final static void main(String args[]) {
		byte[] data = new byte[1000];
		try {
			socket = new DatagramSocket(PORT);
			receivePacket = new DatagramPacket(data, data.length);
			socket.receive(receivePacket);
			//while(true) {
				if (receivePacket.getData().length != 0) {
					Date now = new Date();
					String date = now.toString(); 
					sendPacket = new DatagramPacket(date.getBytes(), date.length(), receivePacket.getAddress(), receivePacket.getPort());
					
					socket.send(sendPacket);
					System.out.println(new String(sendPacket.getData()));
				}
				
			//}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			System.err.println(e);
		}
		catch(IOException e) {
			System.err.println(e);
		}finally {
			if (socket != null)
				socket.close();
		}
		
	}
		
}
