package game.MessageBrokerAndListner;

import org.json.simple.JSONObject;

public class MsgMaker {
	
	@SuppressWarnings("unchecked")
	public static String make(int header, int action, int pid ,int body) {
		
		JSONObject obj = new JSONObject();
		obj.put("h", header);
		obj.put("a", action);
		obj.put("p", pid);
		obj.put("b", body);
		
		return obj.toJSONString();
		
	}
}
