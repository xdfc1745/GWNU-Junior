package chap11_Client;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class ChatingClient extends Frame implements ActionListener
{
	private TextArea display;
	private TextField enter;
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket socket;
	
	public ChatingClient() {
		super("클라이언트");
		display = new TextArea();
		add(display, BorderLayout.CENTER);
		setSize(400, 300);
		enter = new TextField();
		enter.addActionListener(this);
		add(enter, BorderLayout.SOUTH);
		try {
			socket = new DatagramSocket();
			byte data[] = new byte[100];
			String s = "client가 입장했습니다.";
			data = s.getBytes();
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 13);
			socket.send(sendPacket);
			display.append("server가 입장했습니다.\n");
		}catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addWindowListener(new WinListener());
		setVisible(true);
	}
	
	public void waitForPackets() {
		// TODO Auto-generated method stub
		//while(true) {
			try {
				byte data[] = new byte[100];
				receivePacket = new DatagramPacket(data, data.length);
				socket.receive(receivePacket);
				display.append("server:" + new String(receivePacket.getData()) + "\n");
				display.append("\n");
			}catch(IOException io) {
				display.append(io.toString()+ "as\n");
				io.printStackTrace();
			}
		//}
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			//display.append("server:" + e.getActionCommand()+"\n");
			String s = e.getActionCommand();
			byte data[] = s.getBytes();
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 13);
			socket.send(sendPacket);
			display.append("client: " + new String(sendPacket.getData()) + "\n");
			display.append("\n");
		}catch(IOException exception) {
			display.append(exception.toString() +"12");
			exception.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		ChatingClient cc = new ChatingClient();
		cc.waitForPackets();
	}
	
	class WinListener extends WindowAdapter{
		public void WindowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
}
