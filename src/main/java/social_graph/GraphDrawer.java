package social_graph;

import org.graphstream.graph.Graph;
import org.graphstream.ui.view.Viewer;

/**
 * Created by Shepard on 15/04/2017.
 */
public class GraphDrawer extends Thread {

    Graph graph;

    public GraphDrawer(Graph graph){
        this.graph = graph;
    }
    @Override
    public void run() {
        Viewer viewer = this.graph.display();
        final String css_dir = "url('file:"+ System.getProperty("user.dir")+"/src/main/java/social_graph/style_sheet.css')";
        graph.addAttribute("ui.stylesheet", css_dir);
        viewer.enableAutoLayout();
    }
}
