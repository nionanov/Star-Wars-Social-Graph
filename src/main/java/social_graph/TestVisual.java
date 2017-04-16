package social_graph;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.Random;

/**
 * Created by Shepard on 15/04/2017.
 */
public class TestVisual extends Thread {

    Graph graph;

    public TestVisual(Graph graph){
        this.graph = graph;
    }

    @Override
    public void run(){
        Random r = new Random();
        int i = 0;
        while (i<100) {
            try {
                Thread.sleep(100);

                String nameA = ""+i;
                String nameB = ""+(i+1);
                Node nodeA = graph.addNode(nameA);
                Node nodeB = graph.addNode(nameB);

                graph.addEdge(nameA + "-" + nameB, nameA, nameB);

                nodeA.addAttribute("ui.label", nameA);
                nodeB.addAttribute("ui.label", nameB);

                i+= 2;
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
