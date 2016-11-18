import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ChatClient extends Frame {
	Socket s = null;    					//д�ɾ�̬�������׵���
	DataOutputStream dos = null;
	DataInputStream dis = null;
	private boolean bConnected = false;
	
	TextField tfTet = new TextField();
	TextArea taContent = new TextArea();
	
	Thread tRecv = new Thread(new RecvThread());
	
	public static void main(String[] args) {
		new ChatClient().launchFrame();
	}

	public void launchFrame() {
		setLocation(300, 200);
		setSize(400,400);
		add(tfTet,BorderLayout.SOUTH);
		add(taContent,BorderLayout.NORTH);
		pack();
		addWindowListener(new WindowAdapter() {


			@Override
			public void windowClosing(WindowEvent e) {
				disconnect();        //Ϊ�˶�������ڳ������ǰ�ٵ��÷����رսӿ�
				System.exit(0);
			}
		});
		tfTet.addActionListener(new TFListener());
		setVisible(true);
		connect();                   //ͨ�����÷����ڴ��ڴ򿪺��Զ�����Server��
		
		tRecv.start();
	}
	
	//����һ������������Server��
	public void connect() {                  
		try {
			s = new Socket("127.0.0.1",8888);
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
System.out.println("connected!");
			bConnected = true;
		} catch(UnknownHostException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//Ϊ�˶�����붨���������
	public void disconnect() {             
		try {
			dos.close();
			dis.close();
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//�������������س�����������ݷ���ȥ
	private class TFListener implements ActionListener {   

		@Override
		public void actionPerformed(ActionEvent e) {
			String str = tfTet.getText().trim();
			//taContent.setText(str);
			tfTet.setText("");	
			
			try {
				dos.writeUTF(str);
				dos.flush();
				//dos.close();
			} catch(IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private class RecvThread implements Runnable {

		public void run() {
			try {
				while(bConnected) {
					String str = dis.readUTF();
					//System.out.println(str);
					taContent.setText(taContent.getText() + str + '\n');
				} 
			} catch (SocketException e) {
				System.out.println("�˳��ˣ�bye!");
			}catch (IOException e) {
				e.printStackTrace();
			}
		
		}
	}
}




















