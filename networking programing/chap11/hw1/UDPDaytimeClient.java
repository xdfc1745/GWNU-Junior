package chap11_Client;
import java.io.*;
import java.net.*;

public class UDPDaytimeClient {
	public static final int BUFFER_SIZE = 8192;
	private static DatagramPacket receivePacket, sendPacket;
	private static DatagramSocket socket;
	public static final int PORT = 13;
	public static String hostname = "localhost";
	
	public static void main(String args[]) {
		byte data[] = new byte[BUFFER_SIZE];
		try {
			socket = new DatagramSocket();
			InetAddress inet = InetAddress.getByName(hostname);
			String bool = "true";
			data = bool.getBytes();
			byte data1[] = new byte[BUFFER_SIZE];
			sendPacket = new DatagramPacket(data1, data1.length, inet, PORT);
			socket.send(sendPacket);
			while(true) {
				receivePacket = new DatagramPacket(data1, data1.length);
				socket.receive(receivePacket);
				data1 = receivePacket.getData();
				System.out.println("today's date = " + new String(data1));
			}
		} catch (SocketException e2) {
			e2.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(socket != null) 
				socket.close();
		}

	
	
		
		
		
	}

}
