package social_graph;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import org.graphstream.ui.j2dviewer.J2DGraphRenderer;
import org.graphstream.ui.view.Viewer;

public class Test {
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Graph graph = new SingleGraph("Universe");
////		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		TestVisual tv = new TestVisual(graph);
		final String css_dir = "url('file:"+ System.getProperty("user.dir")+"/src/main/java/social_graph/style_sheet.css')";
//		graph.addAttribute("ui.stylesheet", "node { shape: diamond; fill-color: #DEE; size: 20px; stroke-mode: plain; stroke-color: #555; }");
		graph.addAttribute("ui.stylesheet", css_dir);
		graph.display();
		tv.start();
//		Viewer viewer = graph.display();
//		viewer.enableAutoLayout();
//
	    graph.addAttribute("ui.quality");
//		graph.addNode("0");
////
//
//		for (int i = 1; i < 20; i++){
//			graph.addNode(""+i);
//			graph.addEdge(""+i, ""+ (i-1), ""+ i);
//			Node node = graph.getNode(i);
//			node.addAttribute("ui.label", 1);
//		}
//
//		for (Node node : graph) {
//			node.addAttribute("ui.label", node.getId());
//		}

	}
}
