package game.ajax;

import java.io.IOException;
import java.util.Queue;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import game.player.Player;
import game.repository.SearchNode;

/**
 * Servlet implementation class players
 */
@WebServlet("/ajax/players")
public class players extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public players() {
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
		
		Player player= (Player) request.getSession().getAttribute("PLAYER");
		
		if(player==null) {
			return;
		}
		
		 Queue<SearchNode> queue= player.getRoom().getPlayers();
		
		 JSONArray arr = new JSONArray();
		 
		 while(!queue.isEmpty()) {
			 JSONObject obj = new JSONObject();
			 obj.put("name", queue.peek().getName());
			 obj.put("pid", queue.peek().getId());
			 queue.poll();
			 arr.add(obj);
		 }
		 
		 JSONObject result = new JSONObject();
		 result.put("result", arr);
		 
		response.getWriter().print(result.toJSONString());
	}



}
