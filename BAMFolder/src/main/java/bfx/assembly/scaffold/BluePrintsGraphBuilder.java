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
	
	private Edge addEdge(Vertex a,Vertex b,String label) {
		String edgeLabel = a.getId().toString()+b.getId().toString();
		Edge edge = graph.getEdge(edgeLabel);
		if (edge == null) {
			edge=graph.addEdge(edgeLabel, a,b,label);
			edge.setProperty("count", 1);
		} else {
			int c=edge.getProperty("count");
			edge.setProperty("count", c+1);
		}
		edgeCount++;
		return edge;
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
		boolean leftIsReverse = ((flags & 0x10) == 1);
		boolean rightIsReverse = ((flags & 0x20) == 1);
		
		// Only process edges with --> --> or <-- <-- orientations 
		// TODO: Make this configurable in future
		if ((leftIsReverse ^ rightIsReverse) == false) {
			if (leftIsReverse && rightIsReverse ) {
				addEdge(right,left,"R");
			} else {
				addEdge(left,right,"F");
			} 
		}
	}

	public Graph getGraph() {
		return graph;
	}


}
