package project1;
import java.io.*;
import java.util.*;

public class hw1 {
	public static void main(String args[]) {
		int byteRead;
		byte[] buffer = new byte[256];
		FileInputStream fin = null;
		Scanner input = new Scanner(System.in);
		try {
			String name1 = input.nextLine();
			String name2 = input.nextLine();
		
			System.out.println("파일이름 = " + name1);
		
			fin = new FileInputStream(name1);
			while((byteRead = fin.read(buffer)) >= 0) {
				System.out.write(buffer, 0, byteRead);
			}
			System.out.println("\n-----------------------------");
			
			System.out.println("파일이름 = " + name2);
			fin = new FileInputStream(name2);
			while((byteRead = fin.read(buffer)) >= 0) {
				System.out.write(buffer, 0, byteRead);
			}
		}catch(IOException e) {
			System.err.println("스트림으로부터 데이터를 읽을 수 없습니다.");
		}finally{
			try {
				if(fin != null)fin.close();
			}catch(IOException e) {}
		}
		
		
	}
}
