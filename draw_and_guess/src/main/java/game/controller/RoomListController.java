package game.controller;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import game.managers.RoomListManager;
import game.managers.RoomManager;
import game.player.Player;
import game.repository.SearchNode;
import member.dto.MemberDTO;

/**
 * Servlet implementation class RoomListController
 */
@WebServlet("/game/roomlist")
public class RoomListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RoomListController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		Queue<SearchNode> list =  RoomListManager.getRooms();
		
		JSONArray jarr = new JSONArray();
		
		while(!list.isEmpty()) {
			
			SearchNode node = list.poll();
			JSONObject nobj = new JSONObject();
			nobj.put("name", node.getName());
			nobj.put("rid", node.getId());
			
			jarr.add(nobj);
		}
		
		JSONObject obj = new JSONObject();
		obj.put("result", jarr);
		
		response.getWriter().print(obj.toJSONString());
		return;
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		System.out.println("방 생성 요청 감지");
		
		synchronized (request.getSession()) {
			
			if(request.getSession().getAttribute("PLAYER")==null) {
				
				MemberDTO member = (MemberDTO)(request.getSession().getAttribute("MemberDTO"));
				
				if(member==null) {
					
					response.getWriter().print(failedMsg("로그인이 안 되어있습니다."));
					return;
				}
				
				Player player = new Player(member);
				
				request.getSession().setAttribute("PLAYER", player);
				RoomManager room =  RoomListManager.addRoom(player, request.getParameter("title"));
				
				if(room==null) {

					response.getWriter().print(failedMsg("방을 더 이상 생성 할 수 없습니다."));
					request.getSession().removeAttribute("PLAYER");
					return;
				}
				
				player.setRoom(room);
				
				response.getWriter().print(succeeddMsg());
				System.out.println("방 생성 성공!");
				return;
				
			}
			
			response.getWriter().print(failedMsg("진행중인 게임에서 나와서 새로 방을 생성해야 합니다."));
			
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	String failedMsg(String msg) {
		
		JSONObject obj = new JSONObject();
		obj.put("result", false);
		obj.put("msg", msg);
		System.out.println(msg + " 로 인한 방 생성 실패 감지");
		return obj.toJSONString();
	} 
	
	
	@SuppressWarnings("unchecked")
	String succeeddMsg() {
		
		JSONObject obj = new JSONObject();
		obj.put("result", true);
		return obj.toJSONString();
	} 


}
