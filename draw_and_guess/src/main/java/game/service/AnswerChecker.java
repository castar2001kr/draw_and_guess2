package game.service;

public class AnswerChecker {

	private final Process pro;
	private final String ANSWER;
	
	AnswerChecker(String ans, Process pro){
		this.pro = pro;
		this.ANSWER = ans;
	}
	
	public boolean check(String ans) {
		
		if(ans.equals(ANSWER)) {
			pro.run();
			return true;
		}else {
			return false;
		}
	}
	
}
