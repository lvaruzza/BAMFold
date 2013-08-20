package bfx.assembly.scaffold;

import java.io.FileOutputStream;
import java.io.IOException;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

public class TestBAM2Table {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BAMEdgeReader reader = new BAMEdgeReader("data/mates.bam");
		BAM2Table builder = new BAM2Table("data/mates.tbl.txt");
		reader.read(builder);
	}

}
