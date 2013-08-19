package bfx.assembly.scaffold;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;

public class TestSimplify {

	public static void main(String[] args) throws IOException {
		Graph graph = new TinkerGraph();
		
		InputStream in = new FileInputStream("scaffold.graphml");
		GraphMLReader.inputGraph(graph, in);
		in.close();
		
		System.out.println(graph);
		
		Simplifier simp = new Simplifier(graph);
		Vertex a = graph.getVertex("frag_c1");
		Vertex b = graph.getVertex("frag_c8");
		simp.mergeEdges(a, b);
	}

}
