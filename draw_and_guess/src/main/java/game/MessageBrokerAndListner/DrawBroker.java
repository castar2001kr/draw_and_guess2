package game.MessageBrokerAndListner;

import java.util.Queue;

import org.json.simple.JSONObject;

import game.managers.RoomManager;
import game.repository.SearchNode;

public class DrawBroker {

private final RoomManager room;
private final int HostPid;	

	DrawBroker(RoomManager room, int HostPid) {

		this.room = room;
		this.HostPid=HostPid;
	}
	
	void send(JSONObject obj) {

		if(HostPid!=((Long)obj.get("p")).intValue()) {
			return;
		}
		System.out.println("draw message emit!");
		
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
