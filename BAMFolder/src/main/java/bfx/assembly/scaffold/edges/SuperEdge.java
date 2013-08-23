package bfx.assembly.scaffold.edges;

public class SuperEdge {
	private int count;
	private int sumMQ;
	private String left;
	private String right;
	
	public SuperEdge(GraphEdge edge) {
		count=1;
		left=edge.getLeftNode();
		right=edge.getRightNode();
		sumMQ=edge.getMQ();
	}

	public void sumEdge(GraphEdge edge) {
		count++;
		sumMQ+=edge.getMQ();
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