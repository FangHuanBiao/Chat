import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	boolean started = false;    //服务端一开始未连接
	ServerSocket ss = null;
	
	List<Client> Clients = new ArrayList<Client>();
	
	public static void main(String[] args) {
		new ChatServer().start();
	}
	
	public void start() {
		try {
			ss = new ServerSocket(8888);
			started = true;     //判断服务端连接然后开始接收
		} catch (BindException e) {
			System.out.println("端口使用中...");
			System.out.println("请关掉相关程序并重新运行服务器！");
			System.exit(0);
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			while (started) {
				Socket s = ss.accept();
				Client c = new Client(s);
System.out.println("a Client has connected!");
				new Thread(c).start();		
				Clients.add(c);
				//dis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class Client implements Runnable {
		private Socket s;
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean bConnected = false;
		
		public Client(Socket s) {
			this.s = s;
			try {
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bConnected = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void send(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				Clients.remove(this);
				System.out.println("对方从List里退出了");
				//e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				while(bConnected) {
					String str = dis.readUTF();
System.out.println(str);
					for(int i=0; i<Clients.size(); i++) {
						Client c = Clients.get(i);
						c.send(str);
					}
				}
			} catch (EOFException e) {
				System.out.println("Client closed!");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(dis != null) dis.close();
					if(dos !=null) dos.close();
					if(s != null) s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}	
		}
	}
}
