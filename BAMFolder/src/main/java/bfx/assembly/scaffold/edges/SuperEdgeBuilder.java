package bfx.assembly.scaffold.edges;

import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class SuperEdgeBuilder {
	private int count;
	private int sumMQ;
	private String left;
	private String right;
	private boolean reverse;
	
	private DescriptiveStatistics stats;
	private Map<String, Integer> seqs;
	
	public double calcDistance(AlignEdge edge) {
		if (edge.isReverse()) {
			return seqs.get(right)-edge.getRightStart()+edge.getLeftEnd();
		} else {
			return seqs.get(left)-edge.getLeftStart()+edge.getRightEnd();
		}
	}
	
	public SuperEdgeBuilder(AlignEdge edge,Map<String, Integer> seqs) {
		count=1;
		left=edge.getLeftNode();
		right=edge.getRightNode();
		sumMQ=edge.getMQ();
		reverse=edge.isReverse();
		this.seqs = seqs;
		stats = new DescriptiveStatistics();
		stats.addValue(calcDistance(edge));
	}


	public void sumEdge(AlignEdge edge) {
		count++;
		sumMQ+=edge.getMQ();
		stats.addValue(calcDistance(edge));
	}
	
	public SuperEdge build() {
		double median = stats.getPercentile(50);
		double q1 = stats.getPercentile(25);
		double q3 = stats.getPercentile(75);
		
		double iqd = q3-q1;
				
		return new SuperEdge(left,right,reverse,
				count,sumMQ,
				median,iqd);
	}
}