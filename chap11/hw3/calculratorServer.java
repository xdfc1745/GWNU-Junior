package chap11_server;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class calculratorServer extends Frame {
	private TextArea display;
	private DatagramPacket sendPacket, receivePacket = null;
	private DatagramSocket socket;
	static byte result[] = new byte[100];
	
	public calculratorServer() {
		display = new TextArea();
		add(display, BorderLayout.CENTER);
		setSize(400, 300);
		setVisible(true);
		addWindowListener(new WinListener());
		try {
			socket = new DatagramSocket(13);
		}catch(SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		while(true) {
			try {
				byte data [] = new byte[100];
				receivePacket = new DatagramPacket(data, data.length);
				//System.out.println(data+"\n");
				socket.receive(receivePacket);
				display.append(new String(receivePacket.getData()) + " = \n");
				//s = receivePacket.getData().toString(); 
				display.append("계산 중.....\n");
			}catch(IOException io) {
				display.append(io.toString() + "\n");
				io.printStackTrace();
			}
			String s;
	        result = receivePacket.getData();
			 s = Calculator();
			 System.out.println(s);
			 result = s.getBytes();
			//result = calResult.getBytes();
        sendPacket = new DatagramPacket(result, result.length, receivePacket.getAddress(), receivePacket.getPort());
        try {
			socket.send(sendPacket);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        display.append(new String(sendPacket.getData()) + "\n");
		}
	}

       class WinListener extends WindowAdapter{
    	public void windowClosing(WindowEvent e) {
    		System.exit(0);
    	}
    }
    public static void main(String args[]) {
        calculratorServer cs = new calculratorServer();
    }
    
    public String Calculator()
    {
    	String foo = new String(result);

    	foo = foo.trim();
    	System.out.println("input is " + foo + "!");
    	
    	//foo = "40+2";
    	
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");

        try {
        	foo = engine.eval(foo).toString();
        	System.out.println("result is " + foo);
			
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
        
        return foo;
    }
    
    public void _Calculator(String foo)
    {
        //return engine.eval(foo);

    	  /*String n = "";
    	  String result = null;
          int cnt=0;
          
          // 스트링 to char array가 나을지도 모름.
          //String str1 = new String(data);
          
          // Byte array to char array
          int dataLength = data.length;
          char expr[] = new char[dataLength];
          for (int i=0; i<dataLength; i++)
          {
        	  expr[i] = (char)data[i];
        	  if (expr[i] == '\0')
        	  {
        		  cnt = i;
        		  break;
        	  }
          }
          
          // index 0 != num or '-' or '+'
          //int zeroIndex = (int)expr[0];
          
          // op와  num을 분리
          char op[] = new char[cnt];
          int opCnt = 0;
          for (int i=0; i < cnt; i++) {
        	 char ch = expr[i];
        	  if(ch == '+' || ch =='-' || ch == '*' || ch == '/') {
        		  op[opCnt] = ch;
        		  opCnt++;
        	  }
          }
        	  
          // 분리된 op의 우선순위를 찾음
          
          // num과 op를 연결
          
          // 계산
          
          // 결과
          
          for (int i=0; i < cnt; i++) {
        	  //byte ch = data[i];
        	  char ch = expr[i];       
            	switch(ch) {
            		case '+' : 
            	}
            //s = Integer.toString(re);
            //intToByteArray(re, 4);
        
            return result;
          }*/
    }
}
