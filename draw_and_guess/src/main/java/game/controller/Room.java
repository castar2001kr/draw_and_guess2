package game.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import game.managers.RoomListManager;
import game.managers.RoomManager;
import game.player.Player;
import game.service.GameService;
import member.dto.MemberDTO;

/**
 * Servlet implementation class Room
 */
@WebServlet("/game/room")
public class Room extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		synchronized(request.getSession()) {
			

			int rid = Integer.parseInt(request.getParameter("rid"));
			String name = request.getParameter("title");
			
			if(request.getSession().getAttribute("PLAYER")==null) {
				
				System.out.println("null player");
				
				MemberDTO member = (MemberDTO)(request.getSession().getAttribute("MemberDTO"));
				
				
				if(member!=null) {
					
					System.out.println("memberDTO 있음.");
					Player player = new Player(member);
					RoomManager room = RoomListManager.getRoom(rid);
					
					if(room!=null) {
						
						GameService service = room.getService();
						if(service.enter(player)!=null) {
							
							System.out.println();
							request.getSession().setAttribute("PLAYER",player);
							response.getWriter().print(successMsg());
							return;
						}
					}
					
					response.getWriter().print(failedMsg());
					return;
				}
				
				response.getWriter().print(failedMsg());
				return;
			}
			
			response.getWriter().print(failedMsg());
			return;
			
		}
		
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		synchronized(req.getSession()) {
			
			if(req.getSession().getAttribute("duplicated")!=null && (boolean) req.getSession().getAttribute("duplicated")) {
				return;
			}
			
			System.out.println("방에 입장");
			
			ServletContext context = this.getServletContext();
			RequestDispatcher dispatcher = context.getRequestDispatcher("/WEB-INF/gameroom.html");
			dispatcher.forward(req,resp);
			req.getSession().setAttribute("duplicated", true);
			System.out.println("중복 소켓 연결 방지 처리");
		}
		
	}
	

	@SuppressWarnings("unchecked")
	String failedMsg() {
		
		JSONObject obj = new JSONObject();
		obj.put("result", false);
		return obj.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	String successMsg() {
		
		JSONObject obj = new JSONObject();
		obj.put("result", true);
		return obj.toJSONString();
	} 

}