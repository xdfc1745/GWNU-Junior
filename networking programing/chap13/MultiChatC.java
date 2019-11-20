package chap13;

// step3
// 아이디와 대화말 메시지를 전송함
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
      super("클라이언트");

      mlbl = new Label("채팅 상태를 보여줍니다.");
      add(mlbl, BorderLayout.NORTH);

      display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
      display.setEditable(false);
      add(display, BorderLayout.CENTER);

      Panel ptotal = new Panel(new BorderLayout());
 
      Panel pword = new Panel(new BorderLayout());
      wlbl = new Label("대화말");
      wtext = new TextField(30); //전송할 데이터를 입력하는 필드
      wtext.addKeyListener(this); //입력된 데이터를 송신하기 위한 이벤트 연결
      pword.add(wlbl, BorderLayout.WEST);
      pword.add(wtext, BorderLayout.CENTER);
      ptotal.add(pword, BorderLayout.NORTH);

      plabel = new Panel(new BorderLayout());
      loglbl = new Label("로그온");
      ltext = new TextField(20); //전송할 데이터를 입력하는 필드
      ltext.addActionListener(this); //입력된 데이터를 송신하기 위한 이벤트 연결
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
         mlbl.setText("연결된 서버이름 : " + client.getInetAddress().getHostName());
         input = new BufferedReader(new InputStreamReader(client.getInputStream()));
         output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
         mlbl.setText("접속 완료 사용할 아이디를 입력하세요.");
         System.out.println(logon);

         while(true) { // 받은 패킷 append
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
		display.append("로그아웃\n");
		ID = "";
		ltext.setVisible(true);
    	  
      }
      else {
    	  ID = ltext.getText();
    	  ltext.setText("");
    	  
          if(ID.equals("") != true) {
             mlbl.setText(ID + "(으)로 로그인 하였습니다.");
          
          try {
        	  clientdata = REQ_LOGON+SEPARATOR+ID+"\r\n";
              output.write(clientdata+"\r\n");
              output.flush();
              display.append("logon" + ID +"\r\n");
              ltext.setVisible(false);
              serverdata = input.readLine();
              display.append("멀티캐스트 채팅그룹의 주소는 " + serverdata+ ":" + port + "입니다.\r\n");
              System.out.println(serverdata + "  1");
               socket = new MulticastSocket(port);
               group = InetAddress.getByName(serverdata);
               System.out.println(serverdata + "  2");
               socket.joinGroup(group); //멀티캐스트 그룹 가입
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
        	  mlbl.setText("다시 로그인 하세요!!!");
          }
      }
   
   }
	
   public static void main(String args[]) {
	   MultiChatC c = new MultiChatC();
      c.runClient();
   }
		
   class WinListener extends WindowAdapter {
      public void windowClosing(WindowEvent e){
    	  display.append("로그아웃\n");
    	  try {
     		 clientdata = LOGOUT+SEPARATOR+ID+"\r\n";
  			 output.write(clientdata.toString() + "\r\n");
  			 output.flush();
  			 display.append("로그아웃\n");
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
            mlbl.setText("다시 로그인 하세요!!!");
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