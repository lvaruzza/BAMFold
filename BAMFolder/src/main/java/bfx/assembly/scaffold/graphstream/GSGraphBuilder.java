package bfx.assembly.scaffold.graphstream;

import java.io.IOException;

import net.sf.samtools.SAMRecord;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import bfx.assembly.scaffold.bam.AlignConsumer;
import bfx.assembly.scaffold.bam.BAMReader;


public class GSGraphBuilder extends AlignConsumer {
	private Graph graph;
	private long edgeCount = 0;
	private long invalidOrientationPair=0;
	
	public GSGraphBuilder() {
		graph = new MultiGraph("graph");
		//index = graph.createIndex("nodeIdx", Vertex.class);
		graph.setStrict(false);
		graph.setAutoCreate(true);
		graph.display();
	}
	
	/*private Edge addEdgeCount(Vertex a,Vertex b,String label) {
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
	}*/

	private Edge addEdgeMulti(Node a,Node b,String label,int start, int mq) {
		if (mq > 0) {
			Edge edge=graph.addEdge(Long.toString(edgeCount), a.getId() ,b.getId());
			edge.setAttribute("start", start);
			edge.setAttribute("MQ", mq);
			edgeCount++;
			return edge;
		}
		return null;
	}
	
	@Override
	public void callback(SAMRecord aln) {
		Node left = graph.getNode(aln.getReferenceName());
		Node right = graph.getNode(aln.getMateReferenceName());
		
		if (left == null) {
			left = graph.addNode(aln.getReferenceName());
			left.setAttribute("length", seqs.get(aln.getReferenceName()));
			left.setAttribute("ui.label", aln.getReferenceName());
		}
		
		if (right == null) {
			right = graph.addNode(aln.getMateReferenceName());
			left.setAttribute("length", seqs.get(aln.getMateReferenceName()));
			left.setAttribute("ui.label", aln.getMateReferenceName());
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


	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws IOException {
		BAMReader reader = new BAMReader("data/mates.bam");
		GSGraphBuilder builder = new GSGraphBuilder();
		reader.read(builder);
		
		Graph graph = builder.getGraph();
		
	}
}
