package bfx.assembly.scaffold.edges;

import java.util.Map;

public abstract class EdgeConsumer {
	protected Map<String, Integer> seqs;
	
	public void setSequences(Map<String, Integer> seqs) {
		this.seqs = seqs;
	}

	public void start() {};
	public abstract void callback(AlignEdge edge);
	public void finish() {};
}