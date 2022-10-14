package game.MessageBrokerAndListner;

import java.util.Queue;

import org.json.simple.JSONObject;

import game.managers.RoomManager;
import game.repository.SearchNode;
import game.service.AnswerChecker;

public class AnsBroker {

	private final RoomManager room;
	private final int HostPid;	
	private final AnswerChecker checker;

	AnsBroker(RoomManager room, int HostPid, AnswerChecker checker) {

		this.room = room;
		this.HostPid = HostPid;
		this.checker = checker;
		
	}
		
	void send(JSONObject obj) {
		
		System.out.println("답 맞추려 시도");
		
		int pid = ((Long)obj.get("p")).intValue();
		
		if(HostPid==pid) {
			return;
		}
		
		if(checker.check((String) obj.get("b"))) {
			
			String msg = MsgMaker.make(0, 0, pid, 0);
			
			Queue<SearchNode> players =  room.getPlayers();
			
			while(!players.isEmpty()) {
				try {
					room.getPlayer(players.poll().getId()).propMsg(msg);
				} catch (NullPointerException e) {
					// TODO: handle exception
					
				}
			}
			
			room.getPlayer(pid).plusScore(10);
			
		}
		
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
