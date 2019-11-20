package chap13;

// STEP 3
// �α׿� �޽����� ��ȭ�� �޽����� ������
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

public class MultiChatS extends Frame {
   TextArea display;
   Label info;
   String address = "239.255.10.10";
   DatagramPacket rpacket, spacket, mpacket;
   int port = 5000;
   boolean logon = false;
   
   private static final String SEPARATOR = "|";
   private static final int REQ_LOGON = 1001;
   private static final int REQ_SENDWORDS = 1021;
   private static final int LOGOUT = 1031;
	
   public MultiChatS() {
      super("����");
      info = new Label();
      add(info, BorderLayout.NORTH);
      display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
      display.setEditable(false);
      add(display, BorderLayout.CENTER);
      addWindowListener(new WinListener());
      
      setSize(300,250);
      setVisible(true);
   }
	
   public void runServer() {
	   DatagramSocket socket = null;
	   MulticastSocket server = null;
	   rpacket = new DatagramPacket(new byte[1], 1);
	   spacket = new DatagramPacket(new byte[65508], 65508);
	
	   try {
		   socket = new DatagramSocket(5000);
		   while(true) {
			   spacket.setLength(spacket.getData().length);
			   socket.receive(spacket);
			   String message = new String (spacket.getData(), 0, spacket.getLength());
			   StringTokenizer st = new StringTokenizer(message, SEPARATOR);
			   int command = Integer.parseInt(st.nextToken());
			   
			   switch(command) {
	               case REQ_LOGON : { // ��1001|���̵𡱸� ������ ���
	                  String ID = st.nextToken();
	                  display.append("Ŭ���̾�Ʈ�� " + ID + "(��)�� �α��� �Ͽ����ϴ�.\r\n");
	                  InetAddress cadr = spacket.getAddress();
	                  int cp = spacket.getPort();
	                  
	                  String text = address + "|" + port;
	                  byte[] buf = text.getBytes();
	                  rpacket.setData(buf);
	                  rpacket.setLength(buf.length);
	                  rpacket.setAddress(cadr);
	                  rpacket.setPort(cp);
	                  socket.send(rpacket);
	                  logon = true;
	                  server = new MulticastSocket(15);
	                break;
	               }
	               case REQ_SENDWORDS : { // ��1021|���̵�|��ȭ������ ����
	                  String ID = st.nextToken();
	                  String text = st.nextToken();
	                  display.append(ID + " : " + text + "\r\n");
	                  String rcv = ID + " : " + text + "\r\n";
	                  byte[] buf = rcv.getBytes("UTF8");
	                  rpacket = new DatagramPacket(buf, buf.length, InetAddress.getByName(address), 15);
	          		  server.send(rpacket);
	                  break;
	               }
	               case LOGOUT : {
	            	   String ID = st.nextToken();
	            	   display.append(ID+"(��)�� �α׾ƿ��� �߽��ϴ�.\r\n");  
	            	   logon = false;
	            	   break;
	               }
			   }
		   }
	   }catch(IOException e) {
		   e.printStackTrace();
	   }
   }
		
   public static void main(String args[]) {
	  MultiChatS s = new MultiChatS();
      s.runServer();
   }
		
   class WinListener extends WindowAdapter {
      public void windowClosing(WindowEvent e) {
         System.exit(0);
      }
   }
}
