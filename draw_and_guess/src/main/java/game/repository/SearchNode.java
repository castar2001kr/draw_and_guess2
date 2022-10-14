package game.repository;

public class SearchNode {

	private SearchNode before;
	private SearchNode after;
	private int id=-1;
	private String name;
	
	public SearchNode(SearchNode before, SearchNode after) {
		this.before=before;
		this.after = after;
	}
	
	public SearchNode() {
		
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name=name;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setBefore(SearchNode node) {
		this.before = node;
	}
	
	public SearchNode getBefore() {
		return this.before;
	}
	
	public void setAfter(SearchNode node) {
		this.after = node;
	}

	public SearchNode getAfter() {
		return this.after;
	}
	
	public boolean equals(SearchNode node) {
		
		return this.id == node.getId();
	}
	
}
