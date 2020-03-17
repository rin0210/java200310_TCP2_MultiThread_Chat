package Server;

import java.util.ArrayList;

public class ClientInfo {
	private ArrayList<ServerChat> sCList = new ArrayList<>();
	private String sendId = null; // 메세지 보낸 사람 아이디
	private String totalMsg = null; // 아이디 + 메세지
	private String whisperMsg = null; // 메세지

	ClientInfo() {

	}

	public void addList(ServerChat s) {
		sCList.add(s);
	}

	// 메세지 보낸 사람 아이디
	public void sendId(String id) {
		this.sendId = "[" + id + "] ";
	}

	// 클라이언트의 메세지 받아옴
	public void multiSend(String m) {
		whisperMsg = m;
		totalMsg = sendId + m;

		if ('/'==(m.charAt(0))) {	// 귓속말
			whisper();
		} 
		else {
				for (ServerChat s : sCList) {	// 전체
				s.send(totalMsg);
			}
		}
	}

	// 귓속말로 보내기
	public void whisper() {
		int ed = whisperMsg.indexOf(" ", 1); // 아이디끝
		String wId = whisperMsg.substring(1, ed); // 아이디부분
		String wMsg = sendId+"님의 귓속말 >> "+whisperMsg.substring(ed+1); // 메세지부분
		
		for (ServerChat s : sCList) {
			s.whisperSend(wId,wMsg);
		}
	}
}
