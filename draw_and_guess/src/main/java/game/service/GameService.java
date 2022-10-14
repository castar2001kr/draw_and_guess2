package game.service;

import java.util.Timer;
import java.util.TimerTask;

import org.json.simple.JSONObject;

import game.MessageBrokerAndListner.EnvChangedBroker;
import game.MessageBrokerAndListner.Listener;
import game.ajax.hostpid;
import game.managers.RoomManager;
import game.player.Player;

public class GameService {
	
	private final Listener listener;
	
	private final EnvChangedBroker env;

	private final RoomManager room;
	
	private final Process[] exitPro = new Process[3];
	
	private Process[] playPro = new Process[3];
	private AnswerChecker checker;
	
	
	private Timer timer;
	
	private boolean PlAY_STATE = false;
	
	public GameService(RoomManager room) {
		
		this.room = room;
		
		exitPro[0] = new Process() {
			
			@Override
			public void run() {
				
				env.sendExitMsg(0);
				//메시지 브로커에게 out 이벤트 전달.
			}
		};
		
		exitPro[1] = new Process() {
			
			public void run() {
				
				room.stop();
				if(playPro[2]!=null) {
					playPro[2].run();
				}
				env.sendExitMsg(0);
				env.sendHostChangeMsg();
			}
		};
		
		exitPro[2] = new Process() {
			public void run() {}
		};
		
		listener = new Listener(this.room);
		
		env = new EnvChangedBroker(room);
	
	}
	
	synchronized public void socketSend(String msg, int pid) {
		
		listener.listen(msg, pid);
	}
	
	synchronized public RoomManager enter(Player p) {
		
		if(room.in(p)>=0) {
			System.out.println("서비스에서 입장 허가");
			p.setRoom(room);
			env.sendEnterMsg(p.getPid());
			return room;
		}else {
			return null;
		}
		
	}
	
	synchronized public void exit(Player p) {
		
		int outsig =  room.out(p);
		System.out.println("아웃시그널 : " + outsig);
		
		exitPro[outsig].run();
	}
	
	synchronized public void hostChange(int SessionPid, int pid) { //ajax로 요청해야 함. 요청시, 호스트인지 확인해야 함.
		
		if(!PlAY_STATE && SessionPid == room.getHostPid()) {
			
			room.hostChange(pid);
			env.sendHostChangeMsg();
		}
		
	}
	
	synchronized public void play(int SessionPid, String quiz) { //ajax로 요청해야 함. 호스트인지 확인해야 함.
		
		if(!PlAY_STATE && SessionPid == room.getHostPid()) {
			
			System.out.println("게임시작 요청이 옴.");
			
			PlAY_STATE = !PlAY_STATE;
			room.play();
			
			
			playPro[0] = ()->{
				synchronized (playPro) {
					
					playPro[0]=null;
					playPro[1]=null;
					playPro[2]=null;
					room.stop();
					PlAY_STATE = !PlAY_STATE;
					env.sendStopMsg(0);
					listener.gameEnd();
					//게임 중단.
					// 메시지 브로커에게 playpro 타임아웃 알림
					// 메시지 브로커에게 정상 게임 중단 알림
				}
				
			};
			
			playPro[1] = ()->{
				synchronized (playPro) {
					
					playPro[0]=null;
					playPro[1]=null;
					playPro[2]=null;
					timer.cancel();
					room.stop();
					PlAY_STATE = !PlAY_STATE;
					env.sendStopMsg(0);
					listener.gameEnd();
					//게임 중단.
					//메시지 브로커에게 정상 게임 중단 알림
					//플레이어 점수 증가
					
				}
				
				
			};
			
			playPro[2] = ()->{
				
				synchronized (playPro) {
					
					playPro[0]=null;
					playPro[1]=null;
					playPro[2]=null;
					timer.cancel();
					room.stop();
					PlAY_STATE = !PlAY_STATE;
					env.sendStopMsg(1);
					listener.gameEnd();
					//게임 중단.
					//메시지 브로커에게 호스트가 나가서 게임 중단임을 알림.
					
				}
			};
			
			
			TimerTask task = new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					playPro[0].run();
					
				}
			};
			
			timer=new Timer();
			timer.schedule(task, 1000*60);
			
			
			checker = new AnswerChecker(quiz, playPro[1]);
			listener.gameStart(room.getHostPid(), checker);
			//그림 메시지 브로커 생성.
			//정답 메시지 브로커 생성. checker 참조시킴.
			//정답 메시지 브로커는 checker의 boolean 반환값으로 정답자가 생겼는지 확인한다.
			
			checker = new AnswerChecker(quiz,playPro[1]);
			
			listener.gameStart(room.getHostPid(), checker);
			env.sendStartMsg();//스타트 메시지 생성
			System.out.println("스타트 메시지 출력");
		}
	}
	
	
	
}
