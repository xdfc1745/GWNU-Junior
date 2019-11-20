package chap7_8;

import java. io.*;
import java.net.*;

public class MethodClient {
	public static void main(String args[]) {
		Socket theSocket;
		String host;
		if (args.length > 0) host = args[0];
		else host = "localhost";
		try {
			theSocket = new Socket(host, 7);
			System.out.println(theSocket.toString());
			System.out.println("before send buffer size = " + theSocket.getSendBufferSize());
			theSocket.setReceiveBufferSize(30);
			System.out.println("after send buffer size = " + theSocket.getSendBufferSize());
			System.out.println("before tcpnodelay status = " + theSocket.getTcpNoDelay());
			theSocket.setTcpNoDelay(true);
			System.out.println("after tcpnodelay status = " + theSocket.getTcpNoDelay());
		}catch(UnknownHostException e) {
			
		}catch(IOException e) {
			
		}
	}
}
