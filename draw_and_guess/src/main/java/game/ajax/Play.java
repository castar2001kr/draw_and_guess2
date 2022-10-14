package game.ajax;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import game.player.Player;
import game.service.GameService;

/**
 * Servlet implementation class play
 */
@WebServlet("/ajax/play")
public class Play extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Play() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		try{
			
			Player player = ((Player)request.getSession().getAttribute("PLAYER"));
			
			int pid = player.getPid();
			
			GameService service = player.getRoom().getService();
			
			String quiz = request.getParameter("quiz");
			
			
			if(quiz!=null||quiz!="") {
				
				System.out.println("퀴즈 정답 : "+quiz);
				service.play(pid, quiz);
			}
			
		}catch(Exception e) {
			
			
		}
		
		
		
	}


}
