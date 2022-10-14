package game.managers;

import java.util.Queue;
import java.util.Random;

import game.ajax.hostpid;
import game.player.Player;
import game.repository.IDBoxRepository;
import game.repository.SearchNode;
import game.service.GameService;

public class RoomManager {

	private int rid;
	private final IDBoxRepository<Player> repo;
	private static final int SIZE = 6;
	private final String title;
	private int HostPid;
	private GameService service = new GameService(this);
	private boolean cleared = false;
	private boolean playstate = false;

	
	public RoomManager(String title, int rid){ // make new room
			
		repo=new IDBoxRepository<Player>(SIZE);
		this.title=title;
		this.HostPid=0;
		this.rid=rid;
	}
	
	public void setRid(int rid) {
		this.rid = rid;
	}
	

	synchronized public int in(Player p) { 	
		
		int pid = repo.getId(p);
		
		if(pid<0||cleared) {
			return -1;
		}
		
		p.setPid(pid);
		p.setRoom(this);
		
		repo.unlockInfo(pid, p.getName());
		return pid;
		
	}
	
	
	synchronized public int out(Player p) { //true => out trial proceed safely
	
		repo.returnId(p.getPid());
		
		if(p.getPid()==this.getHostPid()) {
			
			Queue<SearchNode> q = this.repo.getAll();

			int newHostPid = -1;
			
			if(!q.isEmpty()) {
				newHostPid = q.poll().getId();
				if(this.getPlayer(newHostPid).getSession()==null) {
					
					cleared = true;
					clearThisRoom();
					return 2;
				}
			}
			
			if(newHostPid<0) {
				cleared = true;
				clearThisRoom();
				return 2;
			}else {
				hostChange(newHostPid);
				return 1;
			}
		}
			
		return 0;
	}
	
	synchronized public void clearThisRoom() {
		RoomListManager.deleteRoom(rid);
		this.cleared =true;
		System.out.println("방 삭제 작업");
	}
	
	synchronized public void hostChange(int pid) {
		
		if(getPlayer(pid)!=null) {
			System.out.println("호스트 체인지 이벤트 발생.");
			HostPid = pid;
		}
	}
	
	public Queue<SearchNode> getPlayers() {
		
		return this.repo.getAll();
		
	}
	
	public Player getPlayer(int pid){
		
		return repo.getInfo(pid);
	}
	
	
	public int getHostPid() {
	
		return HostPid;
	}
	
	synchronized public void play() { 
		
		if(playstate) {
			return;
		}
		
		RoomListManager.lockRoom(rid);
		
		System.out.println(getPlayers().peek().getId());
		playstate = true;
	}
	
	synchronized public void stop() {
		
		if(!playstate) {
			System.out.println("odd stop event prevent!!");
			return;
		}
		
		RoomListManager.unlockRoom(rid);
		playstate = false;
	}
	
	public GameService getService() {
		return service;
	}
	
}
