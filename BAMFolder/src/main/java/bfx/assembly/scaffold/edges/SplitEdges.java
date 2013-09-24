package bfx.assembly.scaffold.edges;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SplitEdges extends EdgeConsumer {
	private static Logger log = LoggerFactory.getLogger(EdgeConsumer.class);
	
	private EdgeConsumer sameNode;
	private EdgeConsumer diffNode;
	private long sameCount = 0;
	private long diffCount = 0;
	
	public SplitEdges(EdgeConsumer sameNode,EdgeConsumer diffNode) {
		this.sameNode = sameNode;
		this.diffNode = diffNode;
	}

	@Override
	public void setSequences(Map<String, Integer> seqs) {
		super.setSequences(seqs);
		sameNode.setSequences(seqs);
		diffNode.setSequences(seqs);
	}
	
	@Override
	public void callback(AlignEdge edge) {
		//System.out.println(String.format("%s\t%s",edge.getLeftNode(),edge.getRightNode()));
		if (edge.getLeftNode().equals(edge.getRightNode())) {
			sameNode.callback(edge);
			sameCount++;
		} else {
			diffNode.callback(edge);
			diffCount++;
		}
	}
	
	public void finish() {
		log.info(String.format("Edges in the same node %d",sameCount));
		log.info(String.format("Edges in different node %d",diffCount));
	}
}
