package bfx.assembly.scaffold.graphstream;

import java.io.IOException;
import java.util.Iterator;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import bfx.assembly.scaffold.bam.BAMReaderMappedReads;
import bfx.assembly.scaffold.edges.AlignEdge;
import bfx.assembly.scaffold.edges.EdgeConsumer;
import bfx.assembly.scaffold.edges.PairsToEdges;
import bfx.assembly.scaffold.edges.SplitEdges;
import bfx.technology.IonTorrent;


public class GSGraphBuilder extends EdgeConsumer {
	private Graph graph;
	private long edgeCount = 0;
	private long invalidOrientationPair=0;
	
	public GSGraphBuilder() {
		graph = new MultiGraph("graph");
		//index = graph.createIndex("nodeIdx", Vertex.class);
		//graph.setStrict(false);
		//graph.setAutoCreate(true);
		//graph.display();
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
	public void callback(AlignEdge aln) {
		Node left = graph.getNode(aln.getLeftNode());		
		if (left == null) {
			left = graph.addNode(aln.getLeftNode());
			left.setAttribute("length", seqs.get(aln.getLeftNode()));
			left.setAttribute("ui.label", aln.getLeftNode());
		}
		
		Node right = graph.getNode(aln.getRightNode());
		if (right == null) {
			right = graph.addNode(aln.getRightNode());
			left.setAttribute("length", seqs.get(aln.getRightNode()));
			left.setAttribute("ui.label", aln.getRightNode());
		}
		boolean leftIsReverse =  aln.isLeftReverse();
		boolean rightIsReverse = aln.isRightReverse(); //((flags & 0x20) == 1);
		
		// Only process edges with --> --> or <-- <-- orientations 
		// TODO: Make this configurable in future
		if ((leftIsReverse ^ rightIsReverse) == false) {
			if (leftIsReverse && rightIsReverse ) {
				addEdgeMulti(left,right,"R",aln.getRightStart(),aln.getMQ());
			} else {
				addEdgeMulti(left,right,"F",aln.getLeftStart(),aln.getMQ());
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
		BAMReaderMappedReads reader = new BAMReaderMappedReads("data/mates.bam");
		PairsToEdges p2e = new PairsToEdges(new IonTorrent(),20);
		GSGraphBuilder builder = new GSGraphBuilder();
		InsertStatCalc inscalc = new InsertStatCalc();
		
		SplitEdges spliter = new SplitEdges(inscalc,builder);
		p2e.setConsumer(spliter);
		reader.read(p2e);
		
		Graph graph = builder.getGraph();
		Iterator<Node> nodes = graph.getNodeIterator();
		while(nodes.hasNext()) {
			Node node = nodes.next();
			System.out.println(String.format("%s %d",node,node.getInDegree()));
		}
	}
}
