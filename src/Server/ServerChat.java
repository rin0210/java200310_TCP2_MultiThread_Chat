package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

// 멀티 스레드
public class ServerChat extends Thread {
	// 서버에서 클라이언트와 통신하기 위한 클래스

	private Socket withClient = null; // null 잡은 이유 : 어떤 클라이언트와 통신할지 알 수 없기 때문에
	private InputStream reMsg = null; // 소켓을 통해서 메세지가 들어올 수 있도록 만든 자원
	private OutputStream sendMsg = null;
	private String id = null;
	private Scanner in = new Scanner(System.in);
	private ClientInfo cInfo = null;

	ServerChat(Socket c) { // 만들어진 소켓을 받아온다.
		this.withClient = c;

//		streamSet();
	}

	public void admin(ClientInfo c) {
		cInfo = c;
	}

	@Override
	public void run() {
		streamSet(); // 이것을 멀티스레드로 돌릴 것이야, main스레드가 accept상태에서 멀티스레드를 만들고 다시 본인은 돌아간다.
		receive();
//		send();
	}

//	private void receive() { // receive와 send도 각각 스레드를 돌려준다.
//		new Thread(new Runnable() { // 익명의 스레드 객체
//
//			@Override
//			public void run() { // 객체를 새로 만든거야
//				try {
//					System.out.println("receive start~~");
//					while (true) { // 계속 받기 위해서 while문을 써줘
//
//						reMsg = withClient.getInputStream();
//						byte[] reBuffer = new byte[100];
//
////						int n = 0;
////						if ((n = reMsg.read(reBuffer)) == -1) { //연결 종료하기 위함
////							reMsg.close();
////							sendMsg.close();
////							withClient.close();
////							break;
////						}
//
//						reMsg.read(reBuffer); // reMsg의 내용을 reBuffer에 읽어옴
//						String msg = new String(reBuffer); // reBuffer를 스트링으로 바꿔준거야
//						msg = msg.trim(); // 공백제거 : 바이트가 80만 왔다면 나머지 20은 제거해주기 위해서야
//
//						if (msg.equals("/bye")) {
//							System.out.println("[" + id + "] 님과의 연결이 종료되었습니다.");
//							reMsg.close();
//							sendMsg.close();
//							withClient.close();
//							break;
//
//						} else {
//							System.out.println("[" + id + "] " + msg);
//						}
//					}
//				} catch (Exception e) {
//				}
//			}
//		}).start();
//	}

//	private void send() {
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					System.out.println("send start~~");
//					Scanner in = new Scanner(System.in);
//
//					while (true) {
//						String reMsg = in.nextLine();
//						sendMsg = withClient.getOutputStream();
//						sendMsg.write(reMsg.getBytes());
//					}
//				} catch (Exception e) {
//				}
//			}
//		}).start();
//	}

	private void receive() { // receive와 send도 각각 스레드를 돌려준다.
		new Thread(new Runnable() { // 익명의 스레드 객체

			@Override
			public void run() { // 객체를 새로 만든거야
				try {
					System.out.println("receive start~~");
					while (true) { // 계속 받기 위해서 while문을 써줘

						reMsg = withClient.getInputStream();
						byte[] reBuffer = new byte[100];

						reMsg.read(reBuffer); // reMsg의 내용을 reBuffer에 읽어옴
						String msg = new String(reBuffer); // reBuffer를 스트링으로 바꿔준거야
						msg = msg.trim(); // 공백제거 : 바이트가 80만 왔다면 나머지 20은 제거해주기 위해서야

						if (msg.equals("/bye")) {
							System.out.println("[" + id + "] 님과의 연결이 종료되었습니다.");
							reMsg.close();
							sendMsg.close();
							withClient.close();
							break;

						} else {
							System.out.println("[" + id + "] " + msg);
							cInfo.sendId(id);
							cInfo.multiSend(msg);
						}
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	// 전체 보냄
	public void send(String m) {
		try {
			sendMsg = withClient.getOutputStream();
			sendMsg.write(m.getBytes());
		} catch (Exception e) {
		}
	}
	// 귓속말 보냄
	public void whisperSend(String wId, String m) {
		try {
			if (wId.equals(id)) {
				sendMsg = withClient.getOutputStream();
				sendMsg.write(m.getBytes());
			}
		} catch (Exception e) {
		}
	}

	private void streamSet() {
		try {
			// 소켓으로부터 메세지를 받기 위한 코딩
			reMsg = withClient.getInputStream();
			byte[] reBuffer = new byte[100];
			reMsg.read(reBuffer); // reMsg의 내용을 reBuffer에 읽어옴
			id = new String(reBuffer); // reBuffer를 스트링으로 바꿔준거야
			id = id.trim(); // 공백제거 : 바이트가 80만 왔다면 나머지 20은 제거해주기 위해서야

			InetAddress c_ip = withClient.getInetAddress(); // withClient소켓으로부터 아이피를 가져옴
			String ip = c_ip.getHostAddress();

			System.out.println(id + "님 로그인(" + ip + ")");

			String reMsg = "정상접속되었습니다.";
			sendMsg = withClient.getOutputStream();
			sendMsg.write(reMsg.getBytes());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getID() {
		return id;
	}
}
