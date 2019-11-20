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
      super("File 클래스 테스트");
      enter = new TextField("파일 및 다렉토리명을 입력하세요.");
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
         String pattern = "yyyy-MM-dd (E요일) hh:mm aa";
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
         
         Date lastModifiedDate = new Date( name.lastModified() );
         final String str = simpleDateFormat.format( lastModifiedDate );
         try {
            out.setText((name.getName() + "이 존재한다.\n") + 
            (name.isFile() ? "파일이다.\n" : "파일이 아니다.\n") + 
            ("마지막 수정날짜는 : " + str) +  
            ("\n파일의 길이는 : " + name.length()) + 
            ("\n파일의 경로는 : " + name.getPath())+
            ("\n절대경로는 : " + name.getAbsolutePath())+ 
            ("\n상대경로는 : " + name.getCanonicalPath()));
         } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
         }
         put.setText((name.isDirectory() ? "디렉토리이다.\n" : "디렉토리가 아니다.\n")+ 
            (name.isAbsolute() ? "절대경로이다.\n" : "절대경로가 아니다.\n") + 
            ("\n상위 디렉토리는 : " + name.getParent()));
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
            put.append("\n\n디렉토리의 내용은 : \n");
            for(int i=0; i<directory.length; i++)
               put.append(directory[i] + "\n");
         }
      }
      else {
         put.setText(e.getActionCommand() + "은 종재하지 않는다.\n");
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