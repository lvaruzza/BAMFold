package bfx.assembly.scaffold;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class Simplifier {
	
	private Graph graph;
	
	public Simplifier(Graph graph) {
		this.graph = graph;
	}
	
	public void mergeEdges(Vertex a,Vertex b) {
		for(Edge e:a.query().direction(Direction.OUT).edges()) {
			e.getVertex(Direction.IN);
		}
	}
}
