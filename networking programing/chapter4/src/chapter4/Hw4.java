package chapter4;

import java.io.*;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class Hw4 {
	
	static FileReader fr, fr2;
	static File f, f2;
	static BufferedReader br, br2;
	

	
	
	public static void main(String args[]) {

		
		String text1, text2;
		boolean equal = true;
		String pattern = "yyyy-MM-dd (E요일) hh:mm aa";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
         
         Date lastModifiedDate;
         String str = null;
         
		try {  
			fr = new FileReader(args[0]);
			fr2 = new FileReader(args[1]);
			f = new File(args[0]);
			f2 = new File(args[1]);
			br = new BufferedReader(fr);
			br2 = new BufferedReader(fr2);
			
			lastModifiedDate = new Date(f.lastModified() );
			str = simpleDateFormat.format( lastModifiedDate );
			
			while((text1 = br.readLine()) != null) {
				if ((text2 = br2.readLine()) == null) break;
				
				if(text1.equals(text2)) {
					equal &= true;
				} else equal &= false;
	

			}
			if (br.readLine() != null || br2.readLine() != null) {
				equal = false;
			}
		}catch(IOException e) {
			System.err.println(e);
		}
			
			if (equal) {
				System.out.println("파일1의 최종수정시간 = " + str + "\n");
				lastModifiedDate = new Date( f2.lastModified() );
				str = simpleDateFormat.format( lastModifiedDate );
				System.out.println("파일1의 최종수정시간 = " + str + "\n");
			}
			else {
				System.out.println("파일 1의 길이 = "+f.length() + "\n");
				System.out.println("파일 2의 길이 = "+f2.length() + "\n");
			}
			
			try {
				fr.close();
				fr2.close();
			
			}catch(IOException e) {
				System.err.println(e);
			}
			
			/*System.out.println("파일1의 최종수정시간 = " + str + "\n");
			lastModifiedDate = new Date( hw.f2.lastModified() );
			str = simpleDateFormat.format( lastModifiedDate );
			System.out.println("파일1의 최종수정시간 = " + str + "\n");*/
			
		
	}
	
}
