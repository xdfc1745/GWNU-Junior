package chapter2_3;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class Hw2 extends Frame implements ActionListener{
   private TextField enter;
   private TextArea out, put;
   
   public Hw2(){
      super("File Ŭ���� �׽�Ʈ");
      enter = new TextField("���� �� �ٷ��丮���� �Է��ϼ���.");
      enter.addActionListener(this);
      out = new TextArea();
      put = new TextArea();
      add(enter, BorderLayout.NORTH);
      add(out, BorderLayout.CENTER);
      add(put, BorderLayout.SOUTH);
      addWindowListener(new WinListener());
      setSize(400, 400);
      setVisible(true);
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      File name = new File(e.getActionCommand());
      if(name.exists()) {
         String pattern = "yyyy-MM-dd (E����) hh:mm aa";
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
         
         Date lastModifiedDate = new Date( name.lastModified() );
         final String str = simpleDateFormat.format( lastModifiedDate );
         try {
            out.setText((name.getName() + "�� �����Ѵ�.\n") + 
            (name.isFile() ? "�����̴�.\n" : "������ �ƴϴ�.\n") + 
            ("������ ������¥�� : " + str) +  
            ("\n������ ���̴� : " + name.length()) + 
            ("\n������ ��δ� : " + name.getPath())+
            ("\n�����δ� : " + name.getAbsolutePath())+ 
            ("\n����δ� : " + name.getCanonicalPath()));
         } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
         put.setText((name.isDirectory() ? "���丮�̴�.\n" : "���丮�� �ƴϴ�.\n")+ 
            (name.isAbsolute() ? "�������̴�.\n" : "�����ΰ� �ƴϴ�.\n") + 
            ("\n���� ���丮�� : " + name.getParent()));
         if(name.isFile()) {
            try {
               RandomAccessFile r = new RandomAccessFile(name, "r");
               StringBuffer buf = new StringBuffer();
               String text;
               put.append("\n\n");
               while((text = r.readLine()) != null)
                  buf.append(text + "\n");
               put.append(buf.toString());
            }catch(IOException e2) { 
               
            }
         }
         else if(name.isDirectory()) {
            String directory[] = name.list();
            put.append("\n\n���丮�� ������ : \n");
            for(int i=0; i<directory.length; i++)
               put.append(directory[i] + "\n");
         }
      }
      else {
         put.setText(e.getActionCommand() + "�� �������� �ʴ´�.\n");
      }
   }

   private char[] simpleDateFormat(Date lastModifiedDate) {
      // TODO Auto-generated method stub
      return null;
   }

   public static void main(String[] args) {
      Hw2 h = new Hw2();
   }
   
   class WinListener extends WindowAdapter {
      public void windowClosing(WindowEvent we) {
         System.exit(0);
      }
   }
}