package game.MessageBrokerAndListner;

import org.json.simple.JSONObject;

public interface Router {

	public void submit(JSONObject obj);
}
