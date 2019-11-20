package chap09;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.*;

public class DictionaryS extends Frame{
	TextArea display;
	Label info;
	List<ServerThread> list;

	public ServerThread SThread;

	public DictionaryS() {
		super("서버");
		info = new Label();
		add(info, BorderLayout.CENTER);
		display = new TextArea("", 0, 0, TextArea.SCROLLBARS_VERTICAL_ONLY);
		display.setEditable(false);
		add(display, BorderLayout.SOUTH);
		addWindowListener(new WinListener());
		setSize(300, 250);
		setVisible(true);
	}

	public void runServer() {
		ServerSocket server;
		Socket sock;
		try{
			list = new ArrayList<ServerThread> ();
			server = new ServerSocket(5000, 100);
			try{
				while(true) {
					sock = server.accept();
					SThread = new ServerThread(this, sock, display);
					SThread.start();
					info.setText(sock.getInetAddress().getHostName() + "서버는 클라이언트와 연결됨");
				}
			}catch(IOException e) {
				server.close();
				e.printStackTrace();
			}
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void main(String args[]) {
		DictionaryS ds = new DictionaryS();
		ds.runServer();
	}

	class WinListener extends WindowAdapter {
		public void WindowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
}

class ServerThread extends Thread {
	Socket sock;
	BufferedWriter output;
	BufferedReader input;
	TextArea display;
	TextField text;
	String clientdata;
	DictionaryS ds;
	boolean flag = false;

	private static final String SEPARATOR = "|";
	private static final int REQ_LOGON = 1001;
	private static final int REQ_SENDWORLDS = 1021;

	public ServerThread(DictionaryS d, Socket s, TextArea ta) {
		sock = s;
		display = ta;
		ds = d;
		try{
			input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			output = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void run() {
		ds.list.add(this);
		try{
			flag = false;
			while((clientdata = input.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(clientdata, SEPARATOR);
				int command = Integer.parseInt(st.nextToken());
				int cnt = ds.list.size();
				switch(command) {
					case REQ_LOGON: {
						String ID = st.nextToken();
						display.append("클라이언트가 " + ID + "(으)로 로그인하였습니다.\r\n");
					}
					break;
					case REQ_SENDWORLDS: {
						String ID = st.nextToken();
						String message = st.nextToken();
						display.append(ID + ": " + message + "\r\n");
						FileInputStream fis = new FileInputStream("word.txt");
						InputStreamReader isr = new InputStreamReader(fis);
						BufferedReader br = new BufferedReader(isr);
						String data;
						StringTokenizer s = null;
						while((data = br.readLine()) != null) {
							s = new StringTokenizer(data, SEPARATOR);
							String word = s.nextToken();
							display.append(word + "*"+message+"\r\n");
							if (word.equals(message)) { 
								flag = true;
								break;
							}
						}
						if (!flag) {
							for (int i=0; i<cnt; i++) {
								ServerThread SThread = (ServerThread)ds.list.get(i);
								SThread.output.write(message+"의 뜻은 등록되어 있지 않습니다.\r\n");
								SThread.output.flush();
							}
						}
						else {
							display.append(message +"의 뜻은 ");
							String m = s.nextToken();
							for (int i=0; i<cnt; i++) {
								ServerThread SThread = (ServerThread)ds.list.get(i);
								SThread.output.write(ID+":" + m + "\r\n");
								SThread.output.flush();
							}
							display.append(m+"입니다.");
							flag = false;
						}
						fis.close();
					}
					break;
				}
			}
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		ds.list.remove(this);
		try{
			sock.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}

