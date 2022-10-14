package game.repository;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class IDBoxRepository<E> {

	private final int SIZE;
	private final InfoBox<E> [] BOX_SET;
	private final Queue<Integer> IDQ;
	private final SearchSet S_SET;
	private final String[] TempNameSave;
	private boolean locked = false;
	

	
	@SuppressWarnings("unchecked")
	public IDBoxRepository(int size) {

		SIZE = size;
		BOX_SET = new InfoBox[size];
		IDQ = new LinkedList<>();
		
		for(int i=0; i<size; i++) {
			IDQ.add(i);
		}
		
		S_SET = new SearchSet();
		TempNameSave = new String[SIZE];
	}
	
	public int getId(E info) {
	
		int id = -1;
		
		synchronized (IDQ) {
			
			if(!IDQ.isEmpty()&&!locked) {
				
				id = IDQ.poll();
			}
			
		}
		
		if(id<0) {
			return -1;
		}
		
		if(BOX_SET[id]==null) {
			
			BOX_SET[id] = new InfoBox<E>();
		}
		
		BOX_SET[id].setInfo(info);

		return id;
	}
	
	public void returnId(int id) {
		
		BOX_SET[id].setState(false);
		BOX_SET[id].setInfo(null);
		
		synchronized (IDQ) {
			
			IDQ.add(id);
			S_SET.delete(id);
		}
		
	}
	
	public E getInfo(int id) {
		
		return BOX_SET[id].getInfo();
	}
	
	public Queue<SearchNode> getAll(){
		
		return S_SET.getAll();
	}
	
	
	public void lockInfo(int id) {
		
		BOX_SET[id].setState(false);
		S_SET.delete(id);
	}
	
	public void unlockInfo(int id) {
		
		System.out.println("unlock info...");
		BOX_SET[id].setState(true);
		S_SET.add(id, TempNameSave[id]);
	}
	
	public void unlockInfo(int id, String name) { // 처음 S_SET에 등록할 때, 사용.
		
		System.out.println("first unlock info...");
		TempNameSave[id] = name;
		BOX_SET[id].setState(true);
			
		S_SET.add(id, name);
	}
	
	synchronized public void lockManger() {
		this.locked=true;
	}
	
	synchronized public void unlockManger() {
		this.locked=false;
	}
	
	
	
}
