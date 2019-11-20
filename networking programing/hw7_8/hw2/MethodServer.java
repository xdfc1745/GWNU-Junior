package chap7_8;
import java.io.*;
import java.net.*;

public class MethodServer {
	public static void main(String args[]) 
	{
		ServerSocket theServer;
		Socket theSocket = null;
		
		try {
			theServer = new ServerSocket(7);
			theServer.setReuseAddress(true);
			theSocket = theServer.accept();
			System.out.println(theSocket.toString());
			System.out.println("reuseaddress 상태 = " + theSocket.getReuseAddress());
			System.out.println("before receive buffer size = " + theSocket.getReceiveBufferSize());
			theSocket.setReceiveBufferSize(30);
			System.out.println("after receive buffer size = " + theSocket.getReceiveBufferSize());
			System.out.println("before keepalive status = " + theSocket.getKeepAlive());
			theSocket.setKeepAlive(true);
			System.out.println("before keepAlive status = " + theSocket.getKeepAlive());
		}catch(IOException e) {
			System.err.println(e);
		}finally{
			try {
				if (theSocket != null) theSocket.close();
			}catch(IOException e) {
				System.err.println(e);
			}
		}
	}
}
