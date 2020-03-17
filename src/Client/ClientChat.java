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

	ClientChat(Socket c) { // ������ ����ϱ� ���� ������ �Ѱܹ���
		this.withServer = c;

		streamSet();
		send();
		receive();
	}

	private void receive() { // receive�� send�� ���� �����带 �����ش�.
		new Thread(new Runnable() {

			@Override
			public void run() { // ��ü�� ���� ����ž�
				try {
					System.out.println("receive start~~");
					while (true) { // ��� �ޱ� ���ؼ� while���� ����

						reMsg = withServer.getInputStream();
						byte[] reBuffer = new byte[100];
						reMsg.read(reBuffer); // reMsg�� ������ reBuffer�� �о��
						String msg = new String(reBuffer); // reBuffer�� ��Ʈ������ �ٲ��ذž�
						msg = msg.trim(); // �������� : ����Ʈ�� 80�� �Դٸ� ������ 20�� �������ֱ� ���ؼ���
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
						if (rreMsg.equals("/bye")) { // ���� �����ϱ� ����
							System.out.println("������ ����˴ϴ�.");
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
			System.out.println("ID�� �Է��ϼ��� >>");
			id = input.nextLine();
			sendMsg = withServer.getOutputStream(); // ������ getOutputStream�� OutputStream����
			sendMsg.write(id.getBytes());

			reMsg = withServer.getInputStream();
			byte[] reBuffer = new byte[100];
			reMsg.read(reBuffer); // reMsg�� ������ reBuffer�� �о��
			String msg = new String(reBuffer); // reBuffer�� ��Ʈ������ �ٲ��ذž�
			msg = msg.trim(); // �������� : ����Ʈ�� 80�� �Դٸ� ������ 20�� �������ֱ� ���ؼ���

			System.out.println(msg);

		} catch (Exception e) {
		}
	}
}
