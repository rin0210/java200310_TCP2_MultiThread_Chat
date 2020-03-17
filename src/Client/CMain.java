package Client;

import java.net.Socket;

public class CMain {

	public static void main(String[] args) throws Exception {
		Socket withServer = null;
		
		withServer = new Socket("10.0.0.77",9999);
			
		new ClientChat(withServer);	// 서버에 접속한 소켓을 넘겨준다.
	}
}
