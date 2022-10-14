package game.MessageBrokerAndListner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import game.managers.RoomManager;
import game.service.AnswerChecker;

public class Listener {

	JSONParser parser = new JSONParser(); 
	
	private final RoomManager room;
	
	private final Router[] router = new Router[7];
	
	private DrawBroker drawBroker;

	private AnsBroker ansBroker;
	
	private final ChatBroker chatBroker;
	
	private Object[] gameBrokers = new Object[2];
	
	public Listener(RoomManager room) {
		
		this.room = room;
		chatBroker = new ChatBroker(room); //5
		gameBrokers[0] = ansBroker; //0
		gameBrokers[1] = drawBroker; //3
		
		router[0] = new Router() {
			
			@Override
			public void submit(JSONObject obj) {
				// TODO Auto-generated method stub
				synchronized (gameBrokers) {
					
					AnsBroker ans = ((AnsBroker)gameBrokers[0]);

					if(ans!=null) {
						
						ans.send(obj);
					}
				}
			}
		};
		
		router[3] = new Router() {
			
			@Override
			public void submit(JSONObject obj) {
				// TODO Auto-generated method stub
				
				synchronized (gameBrokers) {
				
					DrawBroker draw = ((DrawBroker)gameBrokers[1]);
					
					if(draw!=null) {
						
						draw.send(obj);
					}else {
						System.out.println("잘못된 타이밍에 그림 메시지 보냄.");
					}
				}
				
			}
		};
		
		router[5] = new Router() {
			
			@Override
			public void submit(JSONObject obj) {
				// TODO Auto-generated method stub
				chatBroker.send(obj);
				
			}
		};
		
	}
	
	public void listen(String msg, int pid) {
		
		
		try {
			JSONObject obj = (JSONObject) parser.parse(msg);
			
			if(((Long)obj.get("p")).intValue() != pid) {
				return;
			}
			
			
			int h = ((Long)obj.get("h")).intValue();
			System.out.println("헤더 : "+h);
			router[h].submit(obj);
			
			
		} catch (ParseException | NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void gameStart(int HostPid, AnswerChecker checker) {
		
		synchronized(gameBrokers) {
			
			gameBrokers[0] = new AnsBroker(room, HostPid, checker);
			gameBrokers[1] = new DrawBroker(room, HostPid);
		}
		
	}
	
	public void gameEnd() {
		
		synchronized(gameBrokers) {
			
			gameBrokers[0] = null;
			gameBrokers[1] = null;
		}
		
	}
	
}
