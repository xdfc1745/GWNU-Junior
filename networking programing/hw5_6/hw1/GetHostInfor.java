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
		GetHostInfor host = new GetHostInfor("InetAddress Ŭ����");
		host.setVisible(true);
	}
	
	public GetHostInfor(String str) {
		super(str);
		addWindowListener(new WinListener());
		setLayout(new BorderLayout());
		Panel inputpanel = new Panel();
		inputpanel.setLayout(new BorderLayout());
		inputpanel.add("North", new Label("ȣ��Ʈ �̸�:"));
		hostname = new TextField("", 30);
		getinfor = new Button("ȣ��Ʈ ���� ���");
		inputpanel.add("Center", hostname);
		inputpanel.add("South", getinfor);
		getinfor.addActionListener(this);
		add("North", inputpanel);
		Panel outputpanel = new Panel();
		outputpanel.setLayout(new BorderLayout());
		display = new TextArea("", 24, 40);
		display.setEditable(false);
		outputpanel.add("North", new Label("���ͳ� �ּ�"));
		outputpanel.add("Center", display);
		add("Center", outputpanel);
		Panel third = new Panel();
		third.setLayout(new BorderLayout());
		type = new TextField("", 30);
		type.setEditable(false);
		third.add("North", new Label("Ŭ���� ����"));
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
			String ip = name+": �ش� ȣ��Ʈ�� �����ϴ�.\n";
			display.append(ip);
		}
		try {
			InetAddress address = InetAddress.getByName(name);
			type.setText("class������ : " + ipClass(address.getAddress()));
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
