package chapter2_3;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class Hw1 extends Frame implements ActionListener{
   private TextField account, name, balance;
   private Button enter, next;
   private DataOutputStream output;
   private DataInputStream input;
   public Hw1() {
      super("고객파일을 생성");
      try {
         output = new DataOutputStream(new FileOutputStream("client.txt"));
         //input = new DataInputStream(new FileInputStream("client.txt"));
      }catch(IOException e) {
         System.err.println(e.toString());
         System.exit(1);
      }
      setSize(250, 130);
      setLayout(new GridLayout(4, 2));
      add(new Label("구좌번호"));
      account = new TextField();
      add(account);
      add(new Label("이름"));
      name = new TextField(20);
      add(name);
      add(new Label("잔고"));
      balance = new TextField(20);
      add(balance);
      enter = new Button("입력");
      add(enter);
      enter.addActionListener(this);
      next = new Button("출력");
      next.addActionListener(this);
      add(next);
      addWindowListener(new WinListener());
      setVisible(true);
   }
   
   class WinListener extends WindowAdapter {
      public void windowClosing(WindowEvent we) {
         System.exit(0);
      }
   }
   public void addRecord() {
      int accountNo = 0;
      String d;
      if(!account.getText().equals("")) {
         try {
            accountNo = Integer.parseInt(account.getText());
            if(accountNo > 0) {
               output.writeInt(accountNo);
               output.writeUTF(name.getText());
               d = balance.getText();
               output.writeDouble(Double.valueOf(d));
            }
            account.setText(" ");
            name.setText(" ");
            balance.setText(" ");
         }catch(NumberFormatException nfe) {
            System.err.println("정수를 입력해야 합니다.");
         }catch(IOException io) {
            System.err.println(io.toString());
            System.exit(1);
         }
      }
   }
   public void readRecord() {
      int accountNo;
      double d;
      int i = 0;
      String namedata;
      try{
         input = new DataInputStream(new FileInputStream("client.txt"));
         while(true) {
            accountNo = input.readInt();
            namedata = input.readUTF();
           d = input.readDouble();
            System.out.println(Integer.parseInt(account.getText())+ ", " + accountNo);
            if (Integer.parseInt(account.getText()) == accountNo) {
               name.setText(namedata);
                balance.setText(String.valueOf(d));
               break;
            } 
         }
      }catch(EOFException eof) {
         closeFile();
      }catch(IOException io) {
         System.err.println(io.toString());
         //System.exit(1);
      }
   }
   private void closeFile() {
      try {
         input.close();
         System.exit(0);
      }catch(IOException io) {
         System.err.println(io.toString());
         System.exit(1);
      }
   }
   public void actionPerformed(ActionEvent e) {
      if (e.getSource() == next) 
         readRecord();
      else
         addRecord();
   }
   public static void main(String args[]) {
      new Hw1();
   }
}