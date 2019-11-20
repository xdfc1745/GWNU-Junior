package chapter6;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.io.*;

public class ReadServerFile extends Frame implements ActionListener{
	private TextField enter;
	private TextArea infor, contents;
	public ReadServerFile() {
		super("ȣ��Ʈ ���� �б�");
		setLayout(new BorderLayout());
		enter = new TextField("URL�� �Է��ϼ���");
		enter.addActionListener(this);
		add(enter, BorderLayout.NORTH);
		contents = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		add(contents, BorderLayout.CENTER);
		infor = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		add(infor, BorderLayout.SOUTH);
		addWindowListener(new WinListener());
		setSize(400, 450);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e) {
		URL url = null;
		InputStream is;
		BufferedReader input = null;
		String line;
		StringBuffer buffer = new StringBuffer();
		StringBuffer buf = new StringBuffer();
		String location = e.getActionCommand();
		try {
			url = new URL(location);
			is = url.openStream();
			input = new BufferedReader(new InputStreamReader(is));
			contents.setText("������ �д� ���Դϴ�...");
			buffer.append("URL = " + url+'\n').append("protocol = " + url.getProtocol()+'\n');
			buffer.append("host = " + url.getHost()+'\n').append("port num = " + url.getPort()+'\n');
			buffer.append("file name = " + url.getFile()+'\n').append("ref = " + url.getRef()+'\n');
			buffer.append("hash code = " + url.hashCode()+'\n');		
			contents.setText(buffer.toString());
		}catch(MalformedURLException mal) {
			contents.setText("URL������ �߸��Ǿ����ϴ�.");
		}catch(IOException io) {
			contents.setText(io.toString());
		}catch(Exception ex) {
			contents.setText("ȣ��Ʈ ��ǻ���� ���ϸ��� �� �� �ֽ��ϴ�.");
		}
		try {
			Object o = url.getContent();
			URLConnection url_conn = url.openConnection();
			String type = url_conn.getContentType();
			infor.setText(type);
			if(url_conn.getContentType().contains("video")) {
				infor.setText("this url is video");	
			} else if(url_conn.getContentType().contains("audio")) {
				infor.setText("this url is audio\n");
			} else if(o.getClass().getName().contains("Image")) {
				infor.setText("this url is image\n");
			} else if(o.getClass().getName().contains("InputStream")) {
				while((line = input.readLine()) != null)
					buf.append(line).append('\n');
				infor.setText(buf.toString());
			} else infor.setText("��ȯ�� ��ü�� " + url.getClass().getName());
		}catch(MalformedURLException mal) {
			infor.setText("URL������ �߸��Ǿ����ϴ�.");
		}catch(IOException io) {
			infor.setText(io.toString());
		}catch(Exception ex) {
			infor.setText("ȣ��Ʈ ��ǻ���� ���ϸ��� �� �� �ֽ��ϴ�.");
		}
	}
	
	public static void main(String args[]) {
		ReadServerFile read = new ReadServerFile();
	}
	
	class WinListener extends WindowAdapter {
		public void windowClosing(WindowEvent we) {
			System.exit(0);
		}
	}
}
