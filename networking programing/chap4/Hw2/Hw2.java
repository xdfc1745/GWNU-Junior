package chapter4;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Hw2 extends Frame implements ActionListener {
	
	FileInputStream fin = null;
	FileOutputStream fout = null;
	
	private TextField enter;
	private TextArea output;
	
	String buf;
	byte[] buffer = new byte[80];
	
	public Hw2() {
		enter = new TextField("파일명을 입력하세요");
		enter.addActionListener(this);
		output = new TextArea();
		add(enter, BorderLayout.NORTH);
		add(output, BorderLayout.CENTER);
		addWindowListener(new WinListener());
		setSize(400, 400);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		String name = enter.getText();
			try {
				fin = new FileInputStream(name);
				fout = new FileOutputStream("numbered_"+name);
			} catch (Exception e1) {
				System.out.println("1" +e1);
				System.exit(1);
			}
			LineNumberReader read = new LineNumberReader(new InputStreamReader(fin));
			BufferedWriter wrt = new BufferedWriter(new OutputStreamWriter(fout));
			
			while(true) {
				try {
					buf = read.readLine();
					if(buf == null) break;
					buf = read.getLineNumber() + " : " + buf;
					wrt.write(buf+"\r\n");
					wrt.flush();
				
				} catch (IOException e2) {
					System.err.println(e2);
				}
	
			}
			try {
				fin.close();
			} catch (IOException e2) {
				System.err.println(e2);
			}
			try {
				FileReader fr = new FileReader("numbered_"+name);
				BufferedReader br = new BufferedReader(fr);
				String buffer = new String();
				while((buffer = br.readLine()) != null) {
					output.append(buffer + "\n");
				}
				fout.close();
			} catch (IOException e1) {
				System.err.println("3" +e1);
			}
	}
	class WinListener extends WindowAdapter{
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}
	
	public static void main(String args[]) {
		Hw2 hw = new Hw2();
	}
}
