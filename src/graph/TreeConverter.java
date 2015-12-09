package graph;

import java.util.Iterator;
import java.util.Map.Entry;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;

public class TreeConverter {

	public TreeConverter() {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

	}

	private String style = "graph { padding: 50px; fill-color: rgb(240, 240, 240); }\n"
			+ "edge { size: 3px; text-padding: 3px, 2px; text-background-mode: rounded-box; text-style: bold; text-size: 16px; }\n"
			+ "node { stroke-mode: plain; stroke-width: 3px;"
			+ "fill-color: rgb(240, 240, 240); shape: rounded-box; size-mode: fit;"
			+ "padding: 10px; text-padding: 2px; text-style: bold; text-size: 16px;" + "shadow-mode: gradient-radial; }"
			+ "node.leaf { stroke-mode: plain; stroke-width: 3px;"
			+ "fill-color: rgb(240, 240, 0); shape: circle; size-mode: fit;"
			+ "padding: 10px; text-padding: 2px; text-style: bold; text-size: 16px;" + "shadow-mode: gradient-radial; }"
			+ "node.root { stroke-mode: plain; stroke-width: 3px;"
			+ "fill-color: rgb(240, 0, 0); shape: box; size-mode: fit;"
			+ "padding: 10px; text-padding: 2px; text-style: bold; text-size: 16px;"
			+ "shadow-mode: gradient-radial; }";

	public Graph tree2graph(SimpleNode node) {

		Graph graph = new DefaultGraph("Sample graph");
		graph.setStrict(false);
		graph.addAttribute("ui.stylesheet", style);
		graph.addNode(node.getId()).addAttribute("label", node.getName());
		graph.getNode(node.getId()).addAttribute("ui.class", "root");

		this.iterateGraph(node, graph);

		return graph;
	}

	private void iterateGraph(SimpleNode node, Graph graph) {
		if (node.isLeaf()) {
			graph.getNode(node.getId()).addAttribute("ui.class", "leaf");
		} else {
			Iterator<Entry<String, SimpleNode>> childrenIt = node.getChildren().entrySet().iterator();

			while (childrenIt.hasNext()) {
				Entry<String, SimpleNode> next = childrenIt.next();
				graph.addNode(next.getValue().getId()).addAttribute("label", next.getValue().getName());
				graph.addEdge(node.getId() + next.getValue().getId(), node.getId(), next.getValue().getId(), true)
						.addAttribute("label", next.getKey());
				iterateGraph(next.getValue(), graph);
			}
		}

	}

}