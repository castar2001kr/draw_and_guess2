package game.player;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.websocket.Session;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import game.managers.RoomManager;
import member.dto.MemberDTO;


public class Player {

private Session session;
	
	private RoomManager room;
	
	private int pid;
	
	private String name;
	
	private String id;
	
	private int lv;
	
	private int score;
	
	private Queue<String> msgQ = new LinkedList<String>();
	
	private boolean working = false;
	
	public Player(){
	}
	
	
	
	public Player(MemberDTO member) {
		
		this.id = member.getId();
		this.name = member.getName();
		this.lv = member.getLv();
	}
	
	synchronized public void close() throws IOException {
		this.session.close();
		System.out.println(""+id+"가 게임 중 다른 세션에 의해 종료됨.");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	
	public int getPid() {
		return this.pid;
	}
	
	public void setPid(int pid) {
		this.pid=pid;
	}
	
	public boolean propMsg(String msg) {
		
		synchronized(msgQ) {
			
				msgQ.add(msg);
				return true;
			
		}
		
	}
	
	synchronized void setWork(boolean bool) {
		
		this.working = bool;
	}
	
	synchronized public boolean getWork() {
		
		return this.working;
	}
	
	@SuppressWarnings("unchecked")
	public boolean emitMsg() {
			
		synchronized(msgQ) {
			
			if(msgQ.isEmpty()) {
				return true;
			}
			
			System.out.println("work!");
			
			setWork(true);
			
			try {
				System.out.println("메시징 이벤트 처리 player : " + pid);
				
				JSONArray arr= new JSONArray();

				while(!msgQ.isEmpty()) {
					
					arr.add((msgQ.poll()));
					
				}
				
				JSONObject obj = new JSONObject();
				
				obj.put("arr", arr.toJSONString());
				
				this.session.getBasicRemote().sendText(obj.toJSONString());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("ioexception!!1");
				return false;
			}finally {
				setWork(false);
				System.out.println("done...");
			}
			return true;

			
		}
		
					
		
	}

	
	public void plusScore(int s) {
		this.score+=s;
	}

	public void setRoom(RoomManager room) {
		// TODO Auto-generated method stub
		this.room = room;
	}
	
	public RoomManager getRoom() {
		return this.room;
	}

	public void setSession(Session hs) {
		// TODO Auto-generated method stub
		this.session=hs;
	}

	public Session getSession() {
		return this.session;
	}
	
}
