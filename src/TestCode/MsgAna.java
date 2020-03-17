package TestCode;

import org.junit.Test;

public class MsgAna {

	@Test
	public void kkk() {
		String msg = "/to kkk hi! good!!";
//		System.out.println(msg.indexOf(" "));
//		System.out.println(msg.indexOf(" ",4));
		int firstInt = msg.indexOf(" ")+1;
		int endInt = msg.indexOf(" ",firstInt);
		String id = msg.substring(firstInt, endInt);
		System.out.println(id);
		String idMsg = msg.substring(endInt+1);
		System.out.println(idMsg);
	}
}
