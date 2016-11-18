import java.awt.*;

public class ChatClient extends Frame {
	
	public static void main(String[] args) {
		new ChatClient().launchFrame();
	}
	
	public void launchFrame() {
		setLocation(300,200);
		setSize(400,400);
		setVisible(true);
	}
}
