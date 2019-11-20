package chap13;

// STEP 3
// 로그온 메시지와 대화말 메시지를 전송함
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

public class MultiChatS extends Frame {
   TextArea display;
   Label info;
   List<ServerThread> list;
	
   public ServerThread SThread;
	
   public MultiChatS() {
      super("서버");
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
      ServerSocket server;
      Socket sock;
      //ServerThread SThread;
      try {
         list = new ArrayList<ServerThread>();
         server = new ServerSocket(5000, 100);
         try {
            while(true) {
               sock = server.accept(); // 클라이언트 연결
               SThread = new ServerThread(this, sock, display);
               SThread.start();
               info.setText(sock.getInetAddress().getHostName() + " 서버는 클라이언트와 연결됨");
            }
         } catch(IOException ioe) {
            server.close();
            ioe.printStackTrace();
         }
      } catch(IOException ioe) {
         ioe.printStackTrace();
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

class ServerThread extends Thread {
   Socket sock;
   BufferedWriter output;
   BufferedReader input;
   TextArea display;
   Label info;
   TextField text;
   int port = 15;
   String clientdata;
   String address = "239.255.10.10";
   DatagramPacket packet;
//   String serverdata = "";
   MultiChatS cs;
   MulticastSocket ms;
   InetAddress group;
   boolean logon = false;
	
   private static final String SEPARATOR = "|";
   private static final int REQ_LOGON = 1001;
   private static final int REQ_SENDWORDS = 1021;
   private static final int LOGOUT = 1031;
	
//   public ServerThread(ChatMessageS c, Socket s, TextArea ta, Label l) {
   public ServerThread(MultiChatS c, Socket s, TextArea ta) {
      sock = s;
      display = ta;
 //     info = l;
      cs = c;
      try {
         input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
         output = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

      } catch(IOException ioe) {
         ioe.printStackTrace();
      }
   }
   public void run() {
    //  cs.list.add(this);
      try {
    	  while((clientdata = input.readLine()) != null) {
//         while(true) {
//        	 if (logon) {
//        		 ms = new MulticastSocket();
//        		 ms.joinGroup(group);
//	        	 byte[] buffer = new byte[256];
//	        	 packet = new DatagramPacket(buffer, buffer.length);
//	        	 ms.receive(packet);
//	        	 if (packet.getData() == null) break;
//	        	 clientdata = new String(packet.getData());
//	        	 System.out.println(clientdata + "  receive");
//        	 }
        	 System.out.println(clientdata);
             StringTokenizer st = new StringTokenizer(clientdata, SEPARATOR);
             int command = Integer.parseInt(st.nextToken());
             int cnt = cs.list.size();
             switch(command) {
               case REQ_LOGON : { // “1001|아이디”를 수신한 경우
            	  cs.list.add(this);
                  String ID = st.nextToken();
                  display.append("클라이언트가 " + ID + "(으)로 로그인 하였습니다.\r\n");
                  this.output.write(address + "\r\n");
                  this.output.flush();
                  logon = true;
                  ms = new MulticastSocket();
       	       	group = InetAddress.getByName(address);
       	       	byte[] buffer = new byte[5000];
       	       	packet = new DatagramPacket(buffer, buffer.length,group,port);
       	       	ms.joinGroup(group);
       	       	ms.send(packet);
                  break;
               }
               case REQ_SENDWORDS : { // “1021|아이디|대화말”를 수신
                  String ID = st.nextToken();
                  String message = st.nextToken();
                  display.append(ID + " : " + message + "\r\n");
                  clientdata = ID + " : " + message + "\r\n";
                  byte[] buf = clientdata.getBytes();
         		  packet = new DatagramPacket(buf, buf.length, group, port);
          		  ms.send(packet);
//                  for(int i=0; i<cnt; i++) { // 모든 클라이언트에 전송
//                     ServerThread SThread = (ServerThread)cs.list.get(i);
//                     SThread.output.write(ID + " : " + message + "\r\n");
//                     SThread.output.flush();
//                  }
                  break;
               }
               case LOGOUT : {
            	   String ID = st.nextToken();
            	   display.append(ID+"(이)가 로그아웃을 했습니다.\r\n");
            	   cs.list.remove(this);
            	   
            	   
               }
            }
         }
      } catch(IOException e) {
         e.printStackTrace();
      }
      cs.list.remove(this);
      try{
         sock.close();
      }catch(IOException ea){
         ea.printStackTrace();
      }
   }
}