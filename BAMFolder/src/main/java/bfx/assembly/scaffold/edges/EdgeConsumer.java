package bfx.assembly.scaffold.edges;

public abstract class EdgeConsumer {
	public void start() {};
	public abstract void callback(GraphEdge edge);
	public void finish() {};
}