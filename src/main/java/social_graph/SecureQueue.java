package social_graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SecureQueue<E> {
	
	private LinkedList<E> currentLevel;
	private LinkedList<E> nextLevel;
	private final Lock lock;
	
	public SecureQueue(){
		this.currentLevel = new LinkedList<E>();
		this.nextLevel = new LinkedList<E>();
		this.lock = new ReentrantLock();
	}
	
	public void enqueue(E element)
	{
		lock.lock();
		try{
			this.nextLevel.add(element);
		}finally{
			lock.unlock();
		}
	}
	
	public E dequeue()
	{
		lock.lock();
		try{
			return this.currentLevel.removeFirst();
		}finally{
			lock.unlock();
		}
	}
	
	public void proceed(){
		lock.lock();
		try{
			this.currentLevel = this.nextLevel;
			this.nextLevel = new LinkedList<E>();
		}finally{
			lock.unlock();
		}
	}
	
	public int size(){
		lock.lock();
		try{
			return this.currentLevel.size();
		}
		finally{
			lock.unlock();
		}
	}
	
}
