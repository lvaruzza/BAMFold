package bfx.assembly.scaffold;

import net.sf.samtools.SAMRecord;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

public class BluePrintsGraphBuilder implements BAMEdgeReader.EdgeConsumer {
	private KeyIndexableGraph graph;
	private long edgeCount = 0;
	
	public BluePrintsGraphBuilder() {
		graph = new TinkerGraph();
		//index = graph.createIndex("nodeIdx", Vertex.class);
		
	}
	
	@Override
	public void callback(SAMRecord aln) {
		Vertex left = graph.getVertex(aln.getReferenceName());
		Vertex right = graph.getVertex(aln.getMateReferenceName());
		int flags=aln.getFlags();

		if (left == null) {
			left = graph.addVertex(aln.getReferenceName());
		}
		
		if (right == null) {
			right = graph.addVertex(aln.getMateReferenceName());
		}
		Edge edge;
		
		if (((flags & 0x10) == 1)       /* left is reverse */
				&& ((flags & 0x20) ==1) /* right is reverse*/) {
			edge=graph.addEdge(edgeCount, right,left,"R");
			System.out.println(String.format("R %s %s",right.getId(),left.getId()));
			edge.setProperty("right_start", aln.getAlignmentStart());
			edge.setProperty("right_start", aln.getAlignmentStart());
		} else {
			edge=graph.addEdge(edgeCount, left,right,"F");			
			System.out.println(String.format("F %s %s",left.getId(),right.getId()));
			
		}
		edgeCount++;
	}

	public Graph getGraph() {
		return graph;
	}


}
