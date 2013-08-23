package bfx.assembly.scaffold.edges;

public class GraphEdge {
	private String leftNode;
	private String rightNode;
	private int leftStart;
	private int rightStart;
	private int MQ;
	private boolean reverse;
	public String getLeftNode() {
		return leftNode;
	}
	public void setLeftNode(String leftNode) {
		this.leftNode = leftNode;
	}
	public String getRightNode() {
		return rightNode;
	}
	public void setRightNode(String rightNode) {
		this.rightNode = rightNode;
	}
	public int getLeftStart() {
		return leftStart;
	}
	public void setLeftStart(int leftStart) {
		this.leftStart = leftStart;
	}
	public int getRightStart() {
		return rightStart;
	}
	public void setRightStart(int rightStart) {
		this.rightStart = rightStart;
	}
	public int getMQ() {
		return MQ;
	}
	public void setMQ(int mQ) {
		MQ = mQ;
	}
	public boolean isReverse() {
		return reverse;
	}
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
	@Override
	public String toString() {
		return "GraphEdge [leftNode=" + leftNode + ", rightNode=" + rightNode
				+ ", leftStart=" + leftStart + ", rightStart=" + rightStart
				+ ", MQ=" + MQ + ", reverse=" + reverse + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + MQ;
		result = prime * result
				+ ((leftNode == null) ? 0 : leftNode.hashCode());
		result = prime * result + leftStart;
		result = prime * result + (reverse ? 1231 : 1237);
		result = prime * result
				+ ((rightNode == null) ? 0 : rightNode.hashCode());
		result = prime * result + rightStart;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphEdge other = (GraphEdge) obj;
		if (MQ != other.MQ)
			return false;
		if (leftNode == null) {
			if (other.leftNode != null)
				return false;
		} else if (!leftNode.equals(other.leftNode))
			return false;
		if (leftStart != other.leftStart)
			return false;
		if (reverse != other.reverse)
			return false;
		if (rightNode == null) {
			if (other.rightNode != null)
				return false;
		} else if (!rightNode.equals(other.rightNode))
			return false;
		if (rightStart != other.rightStart)
			return false;
		return true;
	}
	public GraphEdge(String leftNode, 
			String rightNode, 
			int leftStart,
			int rightStart, 
			int MQ, boolean reverse) {
		super();
		this.leftNode = leftNode;
		this.rightNode = rightNode;
		this.leftStart = leftStart;
		this.rightStart = rightStart;
		this.MQ = MQ;
		this.reverse = reverse;
	}
	
	
}
