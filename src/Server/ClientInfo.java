package Server;

import java.util.ArrayList;

public class ClientInfo {
	private ArrayList<ServerChat> sCList = new ArrayList<>();
	private String sendId = null; // �޼��� ���� ��� ���̵�
	private String totalMsg = null; // ���̵� + �޼���
	private String whisperMsg = null; // �޼���

	ClientInfo() {

	}

	public void addList(ServerChat s) {
		sCList.add(s);
	}

	// �޼��� ���� ��� ���̵�
	public void sendId(String id) {
		this.sendId = "[" + id + "] ";
	}

	// Ŭ���̾�Ʈ�� �޼��� �޾ƿ�
	public void multiSend(String m) {
		whisperMsg = m;
		totalMsg = sendId + m;

		if ('/'==(m.charAt(0))) {	// �ӼӸ�
			whisper();
		} 
		else {
				for (ServerChat s : sCList) {	// ��ü
				s.send(totalMsg);
			}
		}
	}

	// �ӼӸ��� ������
	public void whisper() {
		int ed = whisperMsg.indexOf(" ", 1); // ���̵�
		String wId = whisperMsg.substring(1, ed); // ���̵�κ�
		String wMsg = sendId+"���� �ӼӸ� >> "+whisperMsg.substring(ed+1); // �޼����κ�
		
		for (ServerChat s : sCList) {
			s.whisperSend(wId,wMsg);
		}
	}
}
