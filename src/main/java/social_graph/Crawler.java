package social_graph;
import java.io.BufferedWriter;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.client.fluent.Request;
import org.graphstream.algorithm.*;
import org.graphstream.graph.Graph;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.graphstream.graph.Node;

public class Crawler extends Thread{
	
	SecureQueue<Character> queue;
	HashSet<String> visited;
	BufferedWriter out;
	LinkedList<String> character_names;
	Graph graph;
	private final Lock lock;
	
	public Crawler(SecureQueue<Character> queue, BufferedWriter out, HashSet<String> visited,LinkedList<String> character_names,Graph graph, Lock lock){
		this.queue = queue;
		this.out = out;
		this.visited = visited;
		this.character_names = character_names;
		this.graph = graph;
		this.lock = lock;
	}
	
	public void run(){
		
//		System.out.println(queue.size());
		while(queue.size() != 0)
		{
			//Take a character from the queue
			Character cur = queue.dequeue();
			int cur_depth = cur.getDistance();
			String cur_name = cur.getName();
			
			try{
				String base_uri = "http://starwars.wikia.com/api.php?action=query&format=json&prop=links&titles=" +cur_name+"&pllimit=500";
				String plcontinue = "";
				do{
					
					URI request = new URI(base_uri.replace(" ", "%20"));
					String res = Request.Get(request)
					        .connectTimeout(5000)
					        .socketTimeout(5000)
					        .execute().returnContent().asString();
					
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode rootNode = objectMapper.readTree(res);
					JsonNode linksNode = rootNode.path("query").path("pages").fields().next().getValue().path("links");
					JsonNode contNode = rootNode.path("query-continue").path("links");
					
					
					for(JsonNode link : linksNode){
						String name = link.get("title").asText();
						if(this.character_names.contains(name))
						{
							lock.lock();
							if(!visited.contains(name)){
								visited.add(name);
//								lock.unlock();
//
//								lock.lock();
//								try {
									Node node = this.graph.addNode(name);
//									node.addAttribute("ui.label", name);
									Node parent = this.graph.getNode(cur_name);
									int Id1 = node.getIndex();
									try {
										this.graph.addEdge(""+name, parent, node);
									} catch (Exception e){
										e.printStackTrace();
										System.out.println("Laaaaaaaaaaaaaaaaaaal");
										System.exit(1);
									}
//								}finally {
//									lock.unlock();
//								}

								queue.enqueue(new Character(name, cur_depth+1));
								out.write(name +","+ (cur_depth+1));
								out.newLine();
								lock.unlock();
							}else
							{
								lock.unlock();
							}
						}
					}
					
					if(!contNode.isMissingNode())
					{
						plcontinue = contNode.get("plcontinue").asText();
						base_uri = base_uri + "&plcontinue=" + URLEncoder.encode(plcontinue,"UTF-8");
					}
					else
						plcontinue = "";
					
				}while(plcontinue != "");
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
	}
}