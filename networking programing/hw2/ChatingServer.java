package chap11_server;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class ChatingServer extends Frame implements ActionListener{
	private TextArea display;
	private TextField enter;
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket socket;
	InetAddress inet;
	int port = 15;
	
	public ChatingServer() {
		display = new TextArea();
		add(display, BorderLayout.CENTER);
		setSize(400, 300);
		enter = new TextField();
		enter.addActionListener(this);
		add(enter, BorderLayout.SOUTH);
		addWindowListener(new WinListener());
		setVisible(true);
		try {
			socket = new DatagramSocket(13);
		}catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	public void waitForPackets() {
		// TODO Auto-generated method stub
		while(true) {
			try {
				byte data[] = new byte[100];
				receivePacket = new DatagramPacket(data, data.length);
				socket.receive(receivePacket);
				display.append("client:" + new String(receivePacket.getData()) + "\n");
				display.append("\n");
				inet = receivePacket.getAddress();
				port = receivePacket.getPort();
			}catch(IOException io) {
				display.append(io.toString()+ "\n");
				io.printStackTrace();
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		try {
			//display.append("server:" + e.getActionCommand()+"\n");
			String s = e.getActionCommand();
			byte data[] = s.getBytes();
			sendPacket = new DatagramPacket(data, data.length, inet, port);
			socket.send(sendPacket);
			display.append("server : " + new String(sendPacket.getData()) + "\n");
			display.append("\n");
		}catch(IOException exception) {
			display.append(exception.toString()+"\n");
			exception.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		ChatingServer cs = new ChatingServer();
		cs.waitForPackets();
	}
	
	class WinListener extends WindowAdapter{
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
}
