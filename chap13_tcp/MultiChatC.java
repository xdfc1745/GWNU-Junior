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
   BufferedWriter output;
   BufferedReader input;
   Socket client;
   Panel plabel;
   String clientdata;
   String serverdata, receive;
   String ID;
   DatagramPacket rpacket, spacket;
   MulticastSocket socket;
   InetAddress group;
   int port = 15;
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
      
//      Panel logout = new Panel(new BorderLayout());
     close = new Button("log out");
//      close.addActionListener(this);
//      logout.add(close, BorderLayout.CENTER);
//      ptotal.add(logout, BorderLayout.SOUTH);

      add(ptotal, BorderLayout.SOUTH);

      addWindowListener(new WinListener());
      setSize(400,250);
      setVisible(true);
   }
	
   public void runClient() {
      try {
         client = new Socket(InetAddress.getLocalHost(), 5000);
         mlbl.setText("����� �����̸� : " + client.getInetAddress().getHostName());
         input = new BufferedReader(new InputStreamReader(client.getInputStream()));
         output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
         mlbl.setText("���� �Ϸ� ����� ���̵� �Է��ϼ���.");
         System.out.println(logon);

         while(true) { // ���� ��Ŷ append
        	 while(!logon)  System.out.println(logon + serverdata);
                 socket = new MulticastSocket(port);
                 group = InetAddress.getByName(serverdata);
                 socket.joinGroup(group);
	        	 byte[] buffer = new byte[256];
	        	 rpacket = new DatagramPacket(buffer, buffer.length);
	        	 socket.receive(rpacket);
	             display.append(new String(rpacket.getData(),0,rpacket.getLength()));   
         }
      } catch(IOException e) {
         e.printStackTrace();
      }
   }
		
   public void actionPerformed(ActionEvent ae){
      if (ae.getSource() == close) {
    	  plabel.removeAll();
          plabel.add(loglbl, BorderLayout.WEST);
          plabel.add(ltext, BorderLayout.EAST);
          plabel.validate();
    	  logon = false;
    	  clientdata = LOGOUT+SEPARATOR+ID+"\r\n";
		 byte [] buf = clientdata.getBytes();
		 spacket = new DatagramPacket(buf, buf.length, group, port);
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
        	  clientdata = REQ_LOGON+SEPARATOR+ID+"\r\n";
              output.write(clientdata+"\r\n");
              output.flush();
              display.append("logon" + ID +"\r\n");
              ltext.setVisible(false);
              serverdata = input.readLine();
              display.append("��Ƽĳ��Ʈ ä�ñ׷��� �ּҴ� " + serverdata+ ":" + port + "�Դϴ�.\r\n");
              System.out.println(serverdata + "  1");
               socket = new MulticastSocket(port);
               group = InetAddress.getByName(serverdata);
               System.out.println(serverdata + "  2");
               socket.joinGroup(group); //��Ƽĳ��Ʈ �׷� ����
               System.out.println("success join group");
               logon = true;
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
    	  try {
     		 clientdata = LOGOUT+SEPARATOR+ID+"\r\n";
  			 output.write(clientdata.toString() + "\r\n");
  			 output.flush();
  			 display.append("�α׾ƿ�\n");
  			 ID = "";
  			 ltext.setVisible(true);
  		} catch (IOException ioe) {
  			ioe.printStackTrace();
  		}
         System.exit(0);
      }
   }

   public void keyPressed(KeyEvent ke) {
      if(ke.getKeyChar() == KeyEvent.VK_ENTER) {
         String message = new String();
         message = wtext.getText();
         if (ID == null) {
            mlbl.setText("�ٽ� �α��� �ϼ���!!!");
            wtext.setText("");
         } else {
            try {

            	clientdata = REQ_SENDWORDS+SEPARATOR+ID+SEPARATOR+message+"\r\n";
               output.write(clientdata.toString()+"\r\n");
               output.flush();

            	System.out.println("send success" + new String(clientdata));
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