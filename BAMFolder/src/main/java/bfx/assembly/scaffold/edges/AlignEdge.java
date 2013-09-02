package bfx.assembly.scaffold.edges;

public class AlignEdge {
	private String leftNode;
	private String rightNode;
	private int leftStart;
	private int leftEnd;
	private int rightStart;
	private int rightEnd;
	private int MQ;
	private boolean reverse;
	
	public int getLeftEnd() {
		return leftEnd;
	}
	public void setLeftEnd(int leftEnd) {
		this.leftEnd = leftEnd;
	}
	public int getRightEnd() {
		return rightEnd;
	}
	public void setRightEnd(int rightEnd) {
		this.rightEnd = rightEnd;
	}
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
	
	public AlignEdge(String leftNode, 
			String rightNode, 
			int leftStart,
			int leftEnd,
			int rightStart,
			int rightEnd,
			int MQ, boolean reverse) {
		super();
		this.leftNode = leftNode;
		this.rightNode = rightNode;
		this.leftStart = leftStart;
		this.leftEnd = leftEnd;
		this.rightStart = rightStart;
		this.rightEnd = rightEnd;
		this.MQ = MQ;
		this.reverse = reverse;
	}	
	
}
