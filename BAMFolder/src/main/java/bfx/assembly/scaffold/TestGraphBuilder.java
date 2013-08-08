package bfx.assembly.scaffold;

import java.io.FileOutputStream;
import java.io.IOException;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

public class TestGraphBuilder {

	@SuppressWarnings("unused")
	private static void saveGraph(Graph graph) throws IOException {		
		GraphMLWriter writer = new GraphMLWriter(graph);
		FileOutputStream out = new FileOutputStream("scaffold.graphml");
		//writer.setNormalize(true);
		writer.outputGraph(out);
		out.close();
		
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BAMEdgeReader reader = new BAMEdgeReader("data/mates.bam");
		BluePrintsGraphBuilder builder = new BluePrintsGraphBuilder();
		reader.read(builder);
		
		Graph graph = builder.getGraph();
		
		System.out.println("Vetices:");
		for(Vertex v: graph.getVertices()) {
			System.out.println(v);
		}
		//saveGraph(graph);
	}

}
