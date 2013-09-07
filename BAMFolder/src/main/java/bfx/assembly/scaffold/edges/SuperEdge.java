package bfx.assembly.scaffold.edges;

import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class SuperEdge {
	private int count;
	private int sumMQ;
	private String left;
	private String right;
	private boolean reverse;
	private double distanceMedian;
	private double distanceIQD;
	
	private DescriptiveStatistics stats;
	private Map<String, Integer> seqs;
	
	public double calcDistance(AlignEdge edge) {
		if (edge.isReverse()) {
			return seqs.get(right)-edge.getRightStart()+edge.getLeftEnd();
		} else {
			return seqs.get(left)-edge.getLeftStart()+edge.getRightEnd();
		}
	}
	
	public SuperEdge(AlignEdge edge,Map<String, Integer> seqs) {
		count=1;
		left=edge.getLeftNode();
		right=edge.getRightNode();
		sumMQ=edge.getMQ();
		reverse=edge.isReverse();
		this.seqs = seqs;
		stats = new DescriptiveStatistics();
		stats.addValue(calcDistance(edge));
	}

	
	public double getDistanceMedian() {
		return distanceMedian;
	}


	public void setDistanceMedian(double distanceMedian) {
		this.distanceMedian = distanceMedian;
	}


	public double getDistanceIQD() {
		return distanceIQD;
	}


	public void setDistanceIQD(double distanceIQD) {
		this.distanceIQD = distanceIQD;
	}


	public boolean isReverse() {
		return reverse;
	}


	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}


	public void sumEdge(AlignEdge edge) {
		count++;
		sumMQ+=edge.getMQ();
		stats.addValue(calcDistance(edge));
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSumMQ() {
		return sumMQ;
	}

	public void setSumMQ(int sumMQ) {
		this.sumMQ = sumMQ;
	}

	public String getLeft() {
		return left;
	}

	public void setLeft(String left) {
		this.left = left;
	}

	public String getRight() {
		return right;
	}

	public void setRight(String right) {
		this.right = right;
	}
}