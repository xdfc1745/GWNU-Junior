package chapter6;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;

public class GetHostInfor extends Frame implements ActionListener{
	TextField hostname, type;
	Button getinfor;
	TextArea display;
	
	public static void main(String args[]) {
		GetHostInfor host = new GetHostInfor("InetAddress 클래스");
		host.setVisible(true);
	}
	
	public GetHostInfor(String str) {
		super(str);
		addWindowListener(new WinListener());
		setLayout(new BorderLayout());
		Panel inputpanel = new Panel();
		inputpanel.setLayout(new BorderLayout());
		inputpanel.add("North", new Label("호스트 이름:"));
		hostname = new TextField("", 30);
		getinfor = new Button("호스트 정보 얻기");
		inputpanel.add("Center", hostname);
		inputpanel.add("South", getinfor);
		getinfor.addActionListener(this);
		add("North", inputpanel);
		Panel outputpanel = new Panel();
		outputpanel.setLayout(new BorderLayout());
		display = new TextArea("", 24, 40);
		display.setEditable(false);
		outputpanel.add("North", new Label("인터넷 주소"));
		outputpanel.add("Center", display);
		add("Center", outputpanel);
		Panel third = new Panel();
		third.setLayout(new BorderLayout());
		type = new TextField("", 30);
		type.setEditable(false);
		third.add("North", new Label("클래스 유형"));
		third.add("Center", type);
		add("South", third);
		setSize(270, 400);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		String name = hostname.getText();
		try {
			InetAddress[] inet = InetAddress.getAllByName(name);
			for (int i=0; i<inet.length; i++) {
				display.append(inet[i] + "\n");
			}
			/*tring ip = inet.getHostName() + "\n";
			display.append(ip);
			ip = inet.getHostAddress()+"\n";
			display.append(ip);*/
		}catch(UnknownHostException ue) {
			String ip = name+": 해당 호스트가 없습니다.\n";
			display.append(ip);
		}
		try {
			InetAddress address = InetAddress.getByName(name);
			type.setText("class유형은 : " + ipClass(address.getAddress()));
		}catch(UnknownHostException ex) {
			System.out.println(ex);
		}
	}
	
	static char ipClass(byte[] ip) { 
		int highByte = 0xff & ip[0];
		return ((highByte<128) ? 'A': (highByte<192) ? 'B': (highByte<224) ? 'C': (highByte<240) ? 'D': 'E');
	}
	
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}
}
