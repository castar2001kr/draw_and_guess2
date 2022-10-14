package game.MessageBrokerAndListner;

import java.util.Queue;

import org.json.simple.JSONObject;

import game.managers.RoomManager;
import game.repository.SearchNode;

public class EnvChangedBroker {

	private final RoomManager room;
	
	public EnvChangedBroker(RoomManager room) {

		this.room = room;
	}
	
	private String makeHostChangeMsg() {
		
		return MsgMaker.make(1, 7, -1, 0);
		
	}
	
	public void sendHostChangeMsg() {
		
		send(makeHostChangeMsg());
	}
	
	
	private String makeEnterMsg(int pid) {
		
		return MsgMaker.make(1, 3, pid, 0);
	}
	
	public void sendEnterMsg(int pid) {
		
		send(makeEnterMsg(pid));
	}
	
	private String makeExitMsg(int pid) {
		
		return MsgMaker.make(1, 5, pid, 0);
	}
	
	public void sendExitMsg(int pid) {
		
		send(makeExitMsg(pid));
	}
	
	private String makeStartMsg() {
		
		return MsgMaker.make(1, 1, -1, 0);
	}
	
	public void sendStartMsg() {
		
		send(makeStartMsg());
	}
	
	
	private String makeStopMsg(int b) { //비정상적인 정지 1, 정상적인 정지 0
		
		return MsgMaker.make(1, 2, -1, b);
	}
	
	public void sendStopMsg(int b) {
		
		send(makeStopMsg(b));
	}
	
	
	void send(String msg) {
		
		System.out.println("prop...");

		Queue<SearchNode> players =  room.getPlayers();
		System.out.println("가장 최근에 온 플레이어 : "+players.peek().getId());
		
		while(!players.isEmpty()) {
			try {
				
				System.out.println( "prop to : "+ players.peek().getId() + "...");
				room.getPlayer(players.poll().getId()).propMsg(msg);
				
			}catch (NullPointerException e) {
				// TODO: handle exception
			}
		}
	}
	
}
