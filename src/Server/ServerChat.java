package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

// ��Ƽ ������
public class ServerChat extends Thread {
	// �������� Ŭ���̾�Ʈ�� ����ϱ� ���� Ŭ����

	private Socket withClient = null; // null ���� ���� : � Ŭ���̾�Ʈ�� ������� �� �� ���� ������
	private InputStream reMsg = null; // ������ ���ؼ� �޼����� ���� �� �ֵ��� ���� �ڿ�
	private OutputStream sendMsg = null;
	private String id = null;
	private Scanner in = new Scanner(System.in);
	private ClientInfo cInfo = null;

	ServerChat(Socket c) { // ������� ������ �޾ƿ´�.
		this.withClient = c;

//		streamSet();
	}

	public void admin(ClientInfo c) {
		cInfo = c;
	}

	@Override
	public void run() {
		streamSet(); // �̰��� ��Ƽ������� ���� ���̾�, main�����尡 accept���¿��� ��Ƽ�����带 ����� �ٽ� ������ ���ư���.
		receive();
//		send();
	}

//	private void receive() { // receive�� send�� ���� �����带 �����ش�.
//		new Thread(new Runnable() { // �͸��� ������ ��ü
//
//			@Override
//			public void run() { // ��ü�� ���� ����ž�
//				try {
//					System.out.println("receive start~~");
//					while (true) { // ��� �ޱ� ���ؼ� while���� ����
//
//						reMsg = withClient.getInputStream();
//						byte[] reBuffer = new byte[100];
//
////						int n = 0;
////						if ((n = reMsg.read(reBuffer)) == -1) { //���� �����ϱ� ����
////							reMsg.close();
////							sendMsg.close();
////							withClient.close();
////							break;
////						}
//
//						reMsg.read(reBuffer); // reMsg�� ������ reBuffer�� �о��
//						String msg = new String(reBuffer); // reBuffer�� ��Ʈ������ �ٲ��ذž�
//						msg = msg.trim(); // �������� : ����Ʈ�� 80�� �Դٸ� ������ 20�� �������ֱ� ���ؼ���
//
//						if (msg.equals("/bye")) {
//							System.out.println("[" + id + "] �԰��� ������ ����Ǿ����ϴ�.");
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

	private void receive() { // receive�� send�� ���� �����带 �����ش�.
		new Thread(new Runnable() { // �͸��� ������ ��ü

			@Override
			public void run() { // ��ü�� ���� ����ž�
				try {
					System.out.println("receive start~~");
					while (true) { // ��� �ޱ� ���ؼ� while���� ����

						reMsg = withClient.getInputStream();
						byte[] reBuffer = new byte[100];

						reMsg.read(reBuffer); // reMsg�� ������ reBuffer�� �о��
						String msg = new String(reBuffer); // reBuffer�� ��Ʈ������ �ٲ��ذž�
						msg = msg.trim(); // �������� : ����Ʈ�� 80�� �Դٸ� ������ 20�� �������ֱ� ���ؼ���

						if (msg.equals("/bye")) {
							System.out.println("[" + id + "] �԰��� ������ ����Ǿ����ϴ�.");
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

	// ��ü ����
	public void send(String m) {
		try {
			sendMsg = withClient.getOutputStream();
			sendMsg.write(m.getBytes());
		} catch (Exception e) {
		}
	}
	// �ӼӸ� ����
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
			// �������κ��� �޼����� �ޱ� ���� �ڵ�
			reMsg = withClient.getInputStream();
			byte[] reBuffer = new byte[100];
			reMsg.read(reBuffer); // reMsg�� ������ reBuffer�� �о��
			id = new String(reBuffer); // reBuffer�� ��Ʈ������ �ٲ��ذž�
			id = id.trim(); // �������� : ����Ʈ�� 80�� �Դٸ� ������ 20�� �������ֱ� ���ؼ���

			InetAddress c_ip = withClient.getInetAddress(); // withClient�������κ��� �����Ǹ� ������
			String ip = c_ip.getHostAddress();

			System.out.println(id + "�� �α���(" + ip + ")");

			String reMsg = "�������ӵǾ����ϴ�.";
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
