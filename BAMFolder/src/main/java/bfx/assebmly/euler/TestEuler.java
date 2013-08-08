package bfx.assebmly.euler;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

import static java.lang.System.out;

public class TestEuler {

	public void euler(Vertex v) {
		for(Edge e:v.getEdges(Direction.OUT,"F")) {
			System.out.print('\t');
			System.out.print(e);
			System.out.print('\t');
			System.out.println(e.getVertex(Direction.IN));
		}
	}
	
	public void run() {
		Graph graph = new TinkerGraph();
		Vertex a = graph.addVertex("a");
		Vertex b = graph.addVertex("b");
		Vertex c = graph.addVertex("c");
		graph.addEdge("ab", a, b, "F");
		graph.addEdge("bc", b, c, "F");
		graph.addEdge("ca", c, a, "F");
		
		for(Vertex v:graph.getVertices()) {
			System.out.println(v);
			euler(v);
		}		
	}
	
	public static void main(String[] args) {
		TestEuler test = new TestEuler();
		test.run();
	}
}
