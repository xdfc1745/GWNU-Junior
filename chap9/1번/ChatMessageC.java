package chapter9;

// step3
// ���̵�� ��ȭ�� �޽����� ������
import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.awt.*;
import java.awt.event.*;

public class ChatMessageC extends Frame implements ActionListener, KeyListener {
	
   TextArea display;
   TextField wtext, ltext;
   Label mlbl, wlbl, loglbl;
   Button close;
   BufferedWriter output;
   BufferedReader input;
   Socket client;
   StringBuffer clientdata = new StringBuffer(2048);
   String serverdata;
   String ID;
	
   private static final String SEPARATOR = "|";
   private static final int REQ_LOGON = 1001;
   private static final int REQ_SENDWORDS = 1021;
   private static final int LOGOUT = 1031;
	
   public ChatMessageC() {
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

      Panel plabel = new Panel(new BorderLayout());
      loglbl = new Label("�α׿�");
      ltext = new TextField(20); //������ �����͸� �Է��ϴ� �ʵ�
      ltext.addActionListener(this); //�Էµ� �����͸� �۽��ϱ� ���� �̺�Ʈ ����
      plabel.add(loglbl, BorderLayout.WEST);
      plabel.add(ltext, BorderLayout.CENTER);
      ptotal.add(plabel, BorderLayout.CENTER);
      
      Panel logout = new Panel(new BorderLayout());
      close = new Button("log out");
      close.addActionListener(this);
      logout.add(close, BorderLayout.CENTER);
      ptotal.add(logout, BorderLayout.SOUTH);

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
         //clientdata = new StringBuffer(2048);
         mlbl.setText("���� �Ϸ� ����� ���̵� �Է��ϼ���.");
         while(true) {
            serverdata = input.readLine();
            display.append(serverdata+"\r\n");
         }
      } catch(IOException e) {
         e.printStackTrace();
      }
   }
		
   public void actionPerformed(ActionEvent ae){
      if (ae.getSource() == close) {
    	  try {
			clientdata.setLength(0);
			clientdata.append(LOGOUT);
			clientdata.append(SEPARATOR);
			clientdata.append(ID);
			output.write(clientdata.toString() + "\r\n");
			output.flush();
			display.append("�α׾ƿ�\n");
			ID = "";
			ltext.setVisible(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  
      }
      else {
    	  ID = ltext.getText();
    	  ltext.setText("");
    	  
          if(ID.equals("") != true) {
             mlbl.setText(ID + "(��)�� �α��� �Ͽ����ϴ�.");
          
          try {
              clientdata.setLength(0);
              clientdata.append(REQ_LOGON);
              clientdata.append(SEPARATOR);
              clientdata.append(ID);
              output.write(clientdata.toString()+"\r\n");
              output.flush();
              //display.append("logon" + ID +"\r\n");
              ltext.setVisible(false);
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
      ChatMessageC c = new ChatMessageC();
      c.runClient();
   }
		
   class WinListener extends WindowAdapter {
      public void windowClosing(WindowEvent e){
    	  display.append("�α׾ƿ�\n");
    	  try {
  			clientdata.setLength(0);
  			clientdata.append(LOGOUT);
  			clientdata.append(SEPARATOR);
  			clientdata.append(ID);
  			output.write(clientdata.toString() + "\r\n");
  			output.flush();
  			display.append("�α׾ƿ�\n");
  			ID = "";
  			ltext.setVisible(true);
  		} catch (IOException ioe) {
  			// TODO Auto-generated catch block
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
               clientdata.setLength(0);
               clientdata.append(REQ_SENDWORDS);
               clientdata.append(SEPARATOR);
               clientdata.append(ID);
               clientdata.append(SEPARATOR);
               clientdata.append(message);
               output.write(clientdata.toString()+"\r\n");
               output.flush();
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