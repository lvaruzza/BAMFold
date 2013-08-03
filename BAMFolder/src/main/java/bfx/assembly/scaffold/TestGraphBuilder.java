package bfx.assembly.scaffold;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

public class TestGraphBuilder {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BAMEdgeReader reader = new BAMEdgeReader("data/mates.bam");
		BluePrintsGraphBuilder builder = new BluePrintsGraphBuilder();
		reader.read(builder);
		
		GraphMLWriter writer = new GraphMLWriter(builder.getGraph());
		FileOutputStream out = new FileOutputStream("scaffold.graphml");
		//writer.setNormalize(true);
		writer.outputGraph(out);
		out.close();

	}

}
