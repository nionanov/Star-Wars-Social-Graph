package social_graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

public class SearchManager {

	String start;
	String output;
	Graph graph;
	int depth;

	LinkedList<String> character_names;
	
	public SearchManager(String start, int depth, String output, LinkedList<String> character_names, Graph graph){
		this.start = start;
		this.depth = depth;
		this.output = output;
		this.character_names = character_names;
		this.graph = graph;
	}
	
	public void exec() throws InterruptedException, IOException{
		System.out.println("Search..........");
		System.out.println(this.start);
		System.out.println(this.depth);
		System.out.println(this.output);
		
		int numThreads = 8;
		Crawler[] crawlers = new Crawler[numThreads];
		SecureQueue<Character> queue = new SecureQueue<Character>();
		HashSet<String> visited = new HashSet<String>();
		File f_out = new File(this.output);
		if(!f_out.exists())
		{
			f_out.createNewFile();
		}
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f_out), "utf-8"));
		queue.enqueue(new Character(start, 0));
		visited.add(start);
		Node node = graph.addNode(start);
		final Lock lock = new ReentrantLock();
		queue.proceed();
		try{
			for(int level = 1; level <= depth; level ++) {

				System.out.println("Level: " + level);

				for (int i = 0; i < numThreads; i++)
					crawlers[i] = new Crawler(queue, out, visited, this.character_names, graph, lock);

				for (int i = 0; i < numThreads; i++) {
					//				System.out.println("Thread " + i + " starting...");
					crawlers[i].start();
				}

				for (int i = 0; i < numThreads; i++) {
					crawlers[i].join();
					//				System.out.println("Thread " + i + " terminated");
				}

				queue.proceed(); // move to next level of depth
//				level ++
			}
		} finally{
			out.close();
		}
		
	}
}
