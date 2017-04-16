package social_graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

public class Main {
	public static void main(String[] args) throws IOException{

		try{
			if(args.length != 3)
				throw new IllegalArgumentException();
		} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
			System.out.println("Illegal number of arguments");
			System.exit(1);
		}
		
		//creates the catalog of characters
		Preprocessor preprocessor = new Preprocessor();
		preprocessor.exec();
		LinkedList<String> character_names = preprocessor.getCharacters();
		
		//Graph initialization
		Graph graph= new SingleGraph("Universe");
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph.addAttribute("ui.quality");
		SearchManager search = new SearchManager(args[0], Integer.parseInt(args[1]), args[2], character_names, graph);
		GraphDrawer gd = new GraphDrawer(graph);
		gd.start();

		//Searcher initialization
		try {
			search.exec();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
