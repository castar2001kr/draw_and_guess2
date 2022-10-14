package game.ajax;

import org.json.simple.JSONObject;

public class ResultMsgMaker {

	@SuppressWarnings("unchecked")
	public static String make(String msg) {
		
		JSONObject obj = new JSONObject();
		obj.put("msg", msg);
		
		return obj.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	public static String make(int msg) {
		
		JSONObject obj = new JSONObject();
		obj.put("msg", msg);
		
		return obj.toJSONString();
	}
}
