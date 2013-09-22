package bfx.assembly.scaffold.edges;

public class AlignEdge {
	private String leftNode;
	private String rightNode;
	private int leftStart;
	private int leftEnd;
	private int rightStart;
	private int rightEnd;
	private int MQ;
	private boolean leftReverse;
	private boolean rightReverse;
	
	
	
	public String getLeftNode() {
		return leftNode;
	}



	public String getRightNode() {
		return rightNode;
	}



	public int getLeftStart() {
		return leftStart;
	}



	public int getLeftEnd() {
		return leftEnd;
	}



	public int getRightStart() {
		return rightStart;
	}



	public int getRightEnd() {
		return rightEnd;
	}



	public int getMQ() {
		return MQ;
	}



	public boolean isLeftReverse() {
		return leftReverse;
	}



	public boolean isRightReverse() {
		return rightReverse;
	}



	public AlignEdge(String leftNode, 
			String rightNode, 
			int leftStart,
			int leftEnd,
			int rightStart,
			int rightEnd,
			int MQ, boolean leftReverse, boolean rightReferse) {
		super();
		this.leftNode = leftNode;
		this.rightNode = rightNode;
		this.leftStart = leftStart;
		this.leftEnd = leftEnd;
		this.rightStart = rightStart;
		this.rightEnd = rightEnd;
		this.MQ = MQ;
		this.leftReverse = leftReverse;
		this.rightReverse = rightReferse;
	}	
	
}
