package chapter2_3;

import java.io.*;
import java.util.*;

public class Hw3 {
   static FileOutputStream fout;
   static FileInputStream fin;
   private static FilterOutputStream filterout;

   public static void main(String args[]) throws FileNotFoundException {
      String inname = null, fname1 = null, fname2 = null;
      inname = args[0].toString();
      fname1 = args[1].toString();
      fname2 = args[2].toString();
      write(inname, fname1, fname2);
   }
   public static void write(String inname, String fname1, String fname2) {
      int byteRead;
      byte[] buffer = new byte[256];
      try {
            fin = new FileInputStream(inname);
            fout = new FileOutputStream(fname1); 
            filterout = new FilterOutputStream(fout);
            copy(fin, fout);
            fin = new FileInputStream(inname);
            fout = new FileOutputStream(fname2); 
            filterout = new FilterOutputStream(fout);
            copy(fin, fout);
         
      }catch(IOException e) {
         System.err.println(e);
      }
   }
   
   public static void copy(InputStream in, OutputStream out) throws IOException {
      synchronized(in) {
         synchronized(out) {
            BufferedInputStream bin = new BufferedInputStream(in);
            BufferedOutputStream bout = new BufferedOutputStream(out);
            while(true) {
               int data = bin.read();
               if(data == -1) break;
               bout.write(data);
            }
            bout.flush();
         }
      }
   }
}
   