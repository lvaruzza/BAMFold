package bfx.assembly.scaffold.blueprints;

import java.io.FileOutputStream;
import java.io.IOException;

import bfx.assembly.scaffold.bam.BAMReader;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
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
		BAMReader reader = new BAMReader("data/mates.bam");
		BluePrintsGraphBuilder builder = new BluePrintsGraphBuilder();
		reader.read(builder);
		
		Graph graph = builder.getGraph();
		saveGraph(graph);
		
		System.out.println("Vetices:");
		for(Vertex v: graph.getVertices()) {
			System.out.println(v);
			for(Edge e: v.getEdges(Direction.OUT, "F","R")) {
				System.out.println(String.format("\t%s %d %d",e,
						e.getProperty("start"),
						e.getProperty("MQ")));
			}
		}
	}

}
