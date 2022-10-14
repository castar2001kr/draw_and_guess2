package game.managers;

import java.util.Queue;

import game.player.Player;
import game.repository.IDBoxRepository;
import game.repository.SearchNode;

public class RoomListManager { // singleton

	private static final RoomListManager manager = new RoomListManager();
	private final IDBoxRepository<RoomManager> repo;
	private final int SIZE;
	
	
	private RoomListManager() {
		
		SIZE = 100;
		repo = new IDBoxRepository<>(SIZE);
	}
	
	
	public static RoomManager addRoom(Player p, String title) {
		
		System.out.println(title + "제목의 방 생성.");
		RoomManager room = new RoomManager(title, -1);
		int rid = manager.repo.getId(room);
		
		if(rid<0) {
			return null;
		}
		
		room.setRid(rid);
		room.in(p);
		
		manager.repo.unlockInfo(rid, title);
		return room;
		
	}
	
	public static void deleteRoom(int rid) {
		
		manager.repo.returnId(rid);
		
	}
	
	public static Queue<SearchNode> getRooms(){
		
		return manager.repo.getAll();
	}
	
	public static RoomManager getRoom(int rid) {
		
		return manager.repo.getInfo(rid);
	}
	
	public static void lockRoom(int rid) {
		
		manager.repo.lockInfo(rid);
	}
	
	public static void unlockRoom(int rid) {
		
		manager.repo.unlockInfo(rid);
	}
	
}
