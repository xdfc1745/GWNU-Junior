package chap13;

// step3
// ���̵�� ��ȭ�� �޽����� ������
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.awt.*;
import java.awt.event.*;

public class MultiChatC extends Frame implements ActionListener, KeyListener {
	
   TextArea display;
   TextField wtext, ltext;
   Label mlbl, wlbl, loglbl;
   Button close;
   DatagramSocket client;
   Panel plabel, logout;
   StringBuffer clientdata;
   String data;
   String ID;
   DatagramPacket rpacket, spacket;
   MulticastSocket socket;
   InetAddress group;
   String hostname = "localhost";
   boolean logon = false;
	
   private static final String SEPARATOR = "|";
   private static final int REQ_LOGON = 1001;
   private static final int REQ_SENDWORDS = 1021;
   private static final int LOGOUT = 1031;
	
   public MultiChatC() {
      super("Ŭ���̾�Ʈ");

      mlbl = new Label("ä�� ���¸� �����ݴϴ�.");
      add(mlbl, BorderLayout.NORTH);

      display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
      display.setEditable(false);
      add(display, BorderLayout.CENTER);

      Panel ptotal = new Panel(new BorderLayout());
 
      Panel pword = new Panel(new BorderLayout());
      wlbl = new Label("��ȭ��");
      wtext = new TextField(30); //������ �����͸� �Է��ϴ� �ʵ�
      wtext.addKeyListener(this); //�Էµ� �����͸� �۽��ϱ� ���� �̺�Ʈ ����
      pword.add(wlbl, BorderLayout.WEST);
      pword.add(wtext, BorderLayout.CENTER);
      ptotal.add(pword, BorderLayout.NORTH);

      plabel = new Panel(new BorderLayout());
      loglbl = new Label("�α׿�");
      ltext = new TextField(20); //������ �����͸� �Է��ϴ� �ʵ�
      ltext.addActionListener(this); //�Էµ� �����͸� �۽��ϱ� ���� �̺�Ʈ ����
      plabel.add(loglbl, BorderLayout.WEST);
      plabel.add(ltext, BorderLayout.CENTER);
      ptotal.add(plabel, BorderLayout.CENTER);
      
      logout = new Panel(new BorderLayout());
      close = new Button("log out");
      close.addActionListener(this);
      logout.add(close, BorderLayout.CENTER);
      ptotal.add(logout, BorderLayout.SOUTH);
      logout.setVisible(false);

      add(ptotal, BorderLayout.SOUTH);

      addWindowListener(new WinListener());
      setSize(400,250);
      setVisible(true);
   }
	
   public void runClient() {
      try {
         client = new DatagramSocket();
         spacket = new DatagramPacket(new byte[65508], 65508);
         rpacket = new DatagramPacket(new byte[1], 1, InetAddress.getByName(hostname), 5000);
         mlbl.setText("��Ƽĳ��Ʈ ä�� ������ ���� ��û�մϴ�.");
         System.out.println(logon);

      } catch(IOException e) {
         e.printStackTrace();
      }
   }
		
   public void actionPerformed(ActionEvent ae){
      if (ae.getSource() == close) {
    	  plabel.setVisible(true);
          logout.setVisible(false);
    	  plabel.removeAll();
          plabel.add(loglbl, BorderLayout.WEST);
          plabel.add(ltext, BorderLayout.EAST);
          plabel.validate();
    	  logon = false;
    	  
    	  clientdata.setLength(0);
    	  clientdata.append(LOGOUT);
    	  clientdata.append(SEPARATOR);
    	  clientdata.append(ID);
    	  
		 byte[] buffer;
		try {
			buffer = new String(clientdata).getBytes("UTF8");
			client.send(rpacket);
			
			socket.leaveGroup(group);
			socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		 display.append("�α׾ƿ�\n");
		ID = "";
		ltext.setVisible(true);
    	  
      }
      else {
    	  ID = ltext.getText();
    	  ltext.setText("");
    	  
          if(ID.equals("") != true) {
             mlbl.setText(ID + "(��)�� �α��� �Ͽ����ϴ�.");
          
          try {
        	  data = REQ_LOGON+SEPARATOR+ID;
              byte[] buf = data.getBytes();
        	  rpacket.setData(buf);
        	  rpacket.setLength(buf.length);
        	  client.send(rpacket);
        	  spacket.setLength(spacket.getData().length);
        	  client.receive(spacket);
        	  String message = new String(spacket.getData());
              display.append("logon" + ID +"\r\n");
        	  
        	  StringTokenizer st = new StringTokenizer(message, SEPARATOR);
        	  String addr = st.nextToken();
        	  group = InetAddress.getByName(addr);
        	  String port = st.nextToken();
              display.append("��Ƽĳ��Ʈ ä�ñ׷��� �ּҴ� " + addr+ ":" + port + "�Դϴ�.");
              display.append("\r\n");
              
              int mp = 15;
              if (st.hasMoreTokens())
            	  mp = Integer.parseInt(port);
              socket = new MulticastSocket(mp);
              socket.setTimeToLive(1);
              socket.joinGroup(group);
              
              serverThread servert = new serverThread(socket);
              servert.start();
              
              logon = true;
              logout.setVisible(true);
              plabel.removeAll();
              plabel.add(close, BorderLayout.CENTER);
              plabel.validate();
           } catch(Exception e) {
              e.printStackTrace();
           }
          }
          else {
        	  mlbl.setText("�ٽ� �α��� �ϼ���!!!");
          }
      }
   
   }
	
   public static void main(String args[]) {
	   MultiChatC c = new MultiChatC();
      c.runClient();
   }
		
   class WinListener extends WindowAdapter {
      public void windowClosing(WindowEvent e){
    	  display.append("�α׾ƿ�\n");
    	  ID = "";
    	  ltext.setVisible(true);
         System.exit(0);
      }
   }
   
   class serverThread extends Thread{
	   MulticastSocket ms;
	   private static final String SEPARATOR = "|";
	   public serverThread(MulticastSocket s) {
			   ms = s;

	   }
	   public void run() {
		   try {
			   while(true) {
			       spacket = new DatagramPacket(new byte[65508], 65508);
				   ms.receive(spacket);
				   display.append(new String(spacket.getData()) + "\r\n");
			   }
		   }catch(IOException e) {
			   e.printStackTrace();
		   }
	   }
   }

   public void keyPressed(KeyEvent ke) {
      if(ke.getKeyChar() == KeyEvent.VK_ENTER) {
         String message = new String();
         message = wtext.getText();
         System.out.println("message = " + message + "???");
         if (ID == null) {
            mlbl.setText("�ٽ� �α��� �ϼ���!!!");
            wtext.setText("");
         } else {
            try {

            	data = REQ_SENDWORDS+SEPARATOR+ID+SEPARATOR+message;
               byte[] buf = new String(data).getBytes("UTF8");
               rpacket.setData(buf);
               rpacket.setLength(buf.length);
               client.send(rpacket);

            	wtext.setText("");
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }

   public void keyReleased(KeyEvent ke) {
   }

   public void keyTyped(KeyEvent ke) {
   }
}