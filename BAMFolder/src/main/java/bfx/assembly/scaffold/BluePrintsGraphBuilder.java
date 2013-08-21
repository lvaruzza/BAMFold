package bfx.assembly.scaffold;

import java.util.Map;

import net.sf.samtools.SAMRecord;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;

public class BluePrintsGraphBuilder implements BAMEdgeReader.EdgeConsumer {
	private KeyIndexableGraph graph;
	private long edgeCount = 0;
	private long invalidOrientationPair=0;
	
	public BluePrintsGraphBuilder() {
		graph = new TinkerGraph();
		//index = graph.createIndex("nodeIdx", Vertex.class);
		
	}
	
	private Edge addEdgeCount(Vertex a,Vertex b,String label) {
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

	private Edge addEdgeMulti(Vertex a,Vertex b,String label,int start, int mq) {
		if (mq > 0) {
			Edge edge=graph.addEdge(edgeCount, a,b,label);
			edge.setProperty("start", start);
			edge.setProperty("MQ", mq);
			edgeCount++;
			return edge;
		}
		return null;
	}
	
	@Override
	public void callback(SAMRecord aln,Map<String,Integer> seqs) {
		Vertex left = graph.getVertex(aln.getReferenceName());
		Vertex right = graph.getVertex(aln.getMateReferenceName());
		
		if (left == null) {
			left = graph.addVertex(aln.getReferenceName());
			left.setProperty("length", seqs.get(aln.getReferenceName()));
		}
		
		if (right == null) {
			right = graph.addVertex(aln.getMateReferenceName());
			left.setProperty("length", seqs.get(aln.getMateReferenceName()));
		}
		boolean leftIsReverse =  aln.getReadNegativeStrandFlag();  //((flags & 0x10) == 1);
		boolean rightIsReverse = aln.getMateNegativeStrandFlag(); //((flags & 0x20) == 1);
		
		// Only process edges with --> --> or <-- <-- orientations 
		// TODO: Make this configurable in future
		if ((leftIsReverse ^ rightIsReverse) == false) {
			if (leftIsReverse && rightIsReverse ) {
				addEdgeMulti(left,right,"R",aln.getAlignmentStart(),aln.getMappingQuality());
			} else {
				addEdgeMulti(left,right,"F",aln.getAlignmentStart(),aln.getMappingQuality());
			} 
		} else {
			invalidOrientationPair++;
		}
	}

	public Graph getGraph() {
		return graph;
	}


	public void printStat() {
		System.out.println(String.format("Invalid Orientation Pairs %d",invalidOrientationPair));
	}
}
