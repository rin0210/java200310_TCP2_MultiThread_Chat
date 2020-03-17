package Client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientChat {
	private Socket withServer = null;
	private InputStream reMsg = null;
	private OutputStream sendMsg = null;
	private String id = null;
	private Scanner input = new Scanner(System.in);

	ClientChat(Socket c) { // 서버에 통신하기 위한 소켓을 넘겨받음
		this.withServer = c;

		streamSet();
		send();
		receive();
	}

	private void receive() { // receive와 send도 각각 스레드를 돌려준다.
		new Thread(new Runnable() {

			@Override
			public void run() { // 객체를 새로 만든거야
				try {
					System.out.println("receive start~~");
					while (true) { // 계속 받기 위해서 while문을 써줘

						reMsg = withServer.getInputStream();
						byte[] reBuffer = new byte[100];
						reMsg.read(reBuffer); // reMsg의 내용을 reBuffer에 읽어옴
						String msg = new String(reBuffer); // reBuffer를 스트링으로 바꿔준거야
						msg = msg.trim(); // 공백제거 : 바이트가 80만 왔다면 나머지 20은 제거해주기 위해서야
						System.out.println(msg);
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	private void send() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("send start~~");
					Scanner in = new Scanner(System.in);

					while (true) {
						String rreMsg = input.nextLine();
						sendMsg = withServer.getOutputStream();
						sendMsg.write(rreMsg.getBytes());
						if (rreMsg.equals("/bye")) { // 연결 종료하기 위함
							System.out.println("연결이 종료됩니다.");
							sendMsg.close();
							reMsg.close();
							withServer.close();
							break;
						}
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	private void streamSet() {
		try {
			System.out.println("ID를 입력하세요 >>");
			id = input.nextLine();
			sendMsg = withServer.getOutputStream(); // 소켓의 getOutputStream을 OutputStream으로
			sendMsg.write(id.getBytes());

			reMsg = withServer.getInputStream();
			byte[] reBuffer = new byte[100];
			reMsg.read(reBuffer); // reMsg의 내용을 reBuffer에 읽어옴
			String msg = new String(reBuffer); // reBuffer를 스트링으로 바꿔준거야
			msg = msg.trim(); // 공백제거 : 바이트가 80만 왔다면 나머지 20은 제거해주기 위해서야

			System.out.println(msg);

		} catch (Exception e) {
		}
	}
}
