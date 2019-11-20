package chap11_Client;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class calculratorClient extends Frame implements ActionListener{
	private TextArea display;
	private TextField enter;
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket socket;
	
	public calculratorClient() {
		enter = new TextField();
		add(enter, BorderLayout.NORTH);
		enter.addActionListener(this);
		display = new TextArea();
		add(display, BorderLayout.CENTER);
		setSize(400, 300);
		setVisible(true);
		addWindowListener(new WinListener());
		try {
			socket = new DatagramSocket(15);
		}catch(SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			byte data[] = new byte[100];
			data = e.getActionCommand().getBytes();
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 13);
			socket.send(sendPacket);
			display.append(new String(sendPacket.getData()) + " = ");
		}catch(IOException ex) {
			display.append(ex.toString() + "\n");
			ex.printStackTrace();
		}
		//while(true) {
			try {
				byte result[] = new byte[100];
				receivePacket = new DatagramPacket(result, result.length);
				socket.receive(receivePacket);
				display.append(new String(receivePacket.getData()));
				display.append("\n");
				
			}catch(IOException io) {
				display.append(io.toString() + "\n");
				io.printStackTrace();
			}
		//}
	}
	
	public static void main(String args[]) {
		calculratorClient cc = new calculratorClient();
	}
	
	class WinListener extends WindowAdapter{
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
}

