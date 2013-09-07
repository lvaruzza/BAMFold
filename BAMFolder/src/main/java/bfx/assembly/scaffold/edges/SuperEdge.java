package bfx.assembly.scaffold.edges;


public class SuperEdge {
	private int count;
	private int sumMQ;
	private String left;
	private String right;
	private boolean reverse;
	private double distanceMedian;
	private double distanceIQD;
	
	public SuperEdge(String left, String right, boolean reverse, int count,
			int sumMQ, double distanceMedian, double distanceIQD) {
		super();
		this.left = left;
		this.right = right;
		this.reverse = reverse;
		this.count = count;
		this.sumMQ = sumMQ;
		this.distanceMedian = distanceMedian;
		this.distanceIQD = distanceIQD;
	}


	public double getDistanceMedian() {
		return distanceMedian;
	}


	public double getDistanceIQD() {
		return distanceIQD;
	}


	public boolean isReverse() {
		return reverse;
	}


	public int getCount() {
		return count;
	}

	public int getSumMQ() {
		return sumMQ;
	}

	public String getLeft() {
		return left;
	}


	public String getRight() {
		return right;
	}
}