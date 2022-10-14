package game.socket;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

@WebListener
public class Config extends Configurator{

	public void modifyHandshake(ServerEndpointConfig sec,  HandshakeRequest req, HandshakeResponse res) {
		
		HttpSession session = (HttpSession)req.getHttpSession();
		
		synchronized(session) {			
			
			if(session != null) {
				System.out.println("세션 등록됨.");
				sec.getUserProperties().put("SESSION", session);
			}
		}
		
		
	}
}