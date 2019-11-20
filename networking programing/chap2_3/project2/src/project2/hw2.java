package project2;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class hw2 extends Frame implements ActionListener {
	Label infile, outfile;
	TextField ifile, ofile;
	TextArea tadata;
	Button obut, inbut;
	String inname, outname;
	String data = null;
	byte buf[] = new byte[100];
	
	public hw2(String str) {
		super(str);
		setLayout(new FlowLayout());
		infile = new Label("입력파일");
		add(infile);
		ifile = new TextField(20);
		add(ifile);
		obut = new Button("확인");
		obut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				byte buffer[] = new byte[100];
				inname = ifile.getText();
				try {
					FileInputStream fin = new FileInputStream(inname);
					fin.read(buffer);
					data = new String(buffer);
					//tadata.setText("input file text");
					//tadata.setText(data+"\n");
					buf = data.getBytes();
				}catch(IOException ei) {
					System.out.println(ei.toString());
				}
			}
		});
		add(obut);
		outfile = new Label("출력파일");
		add(outfile);
		ofile = new TextField(20);
		add(ofile);
		obut = new Button("확인");
		obut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outname = ofile.getText();
				try {
				FileOutputStream fout = new FileOutputStream(outname);
				fout.write(buf);
				tadata.setText("output file text");
				tadata.setText(data+"\n");
				}catch(IOException ei) {
					System.out.println(ei.toString());
				}
			}
		});
		add(obut);
		tadata = new TextArea(3, 35);
		add(tadata);
		addWindowListener(new WinListener());
	}
	
	public static void main(String args[]) {
		hw2 text = new hw2("");
		text.setSize(350, 200);
		text.setVisible(true);
	}
	
	
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}