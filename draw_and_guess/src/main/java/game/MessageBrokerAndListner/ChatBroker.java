package game.MessageBrokerAndListner;

import java.util.Queue;

import org.json.simple.JSONObject;

import game.managers.RoomManager;
import game.repository.SearchNode;

public class ChatBroker {

	private final RoomManager room;
	
	ChatBroker(RoomManager room) {

		this.room = room;
	}
	
	void send(JSONObject obj) {
	
		String msg = obj.toJSONString();
		
		Queue<SearchNode> players =  room.getPlayers();
		
		while(!players.isEmpty()) {
			try {
				
				room.getPlayer(players.poll().getId()).propMsg(msg);
			}catch (NullPointerException e) {
				// TODO: handle exception
			}
		}
			
			
		
	}
	
}
