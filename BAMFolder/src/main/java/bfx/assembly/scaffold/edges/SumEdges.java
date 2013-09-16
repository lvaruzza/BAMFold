package bfx.assembly.scaffold.edges;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import bfx.assembly.scaffold.bam.BAMReader;
import bfx.assembly.util.Table;
import bfx.technology.IonTorrent;

public class SumEdges extends EdgeConsumer implements Iterable<SuperEdge> {
	private Map<String,Map<String,SuperEdgeBuilder>> edges;
	
	public void start() {
		edges = new TreeMap<String,Map<String,SuperEdgeBuilder>>();
	}
	
	@Override
	public void callback(AlignEdge edge) {
		String left=edge.getLeftNode();
		String right=edge.getRightNode();
		String dir=edge.isReverse() ? "R" : "F";
		
		if (!edges.containsKey(left))
			edges.put(left, new TreeMap<String,SuperEdgeBuilder>());
		Map<String,SuperEdgeBuilder> inner=edges.get(left);
		
		if (!inner.containsKey(right + dir)) {
			inner.put(right + dir, new SuperEdgeBuilder(edge,seqs));
		} else {
			inner.get(right + dir).sumEdge(edge);
		}
	}

	public static class SuperEdgeIterator implements Iterator<SuperEdge>{
		private Iterator<SuperEdgeBuilder> inner;
		private Iterator<Map<String,SuperEdgeBuilder>> outer;
		
		public SuperEdgeIterator(Map<String, Map<String, SuperEdgeBuilder>> edges) {
			outer = edges.values().iterator();
			if (outer.hasNext()) {
				inner = outer.next().values().iterator();
			}
		}

		@Override
		public boolean hasNext() {
			if(inner.hasNext()) {
				return true;
			} else {
				if (outer.hasNext()) {
					inner = outer.next().values().iterator();
					return hasNext();
				} else {
					return false;
				}
			}
		}

		@Override
		public SuperEdge next() {
			return inner.next().build();
		}

		@Override
		public void remove() {
			throw new RuntimeException("Not Implemented");
		}
		
	}
	@Override
	public Iterator<SuperEdge> iterator() {
		return new SuperEdgeIterator(edges);
	}
	
	
	public static void main(String[ ] args)  throws IOException {
		SumEdges sumedges = new SumEdges();
		BAMReader reader = new BAMReader("data/mates.names.bam");
		PairsToEdges merger = new PairsToEdges(new IonTorrent(),10);		
		merger.setConsumer(sumedges);
		reader.read(merger);
		Table table=new Table("data/super_edges.txt");

		for(SuperEdge se:sumedges) {
			table.printRow(se.getLeft(),
					se.getRight(),
					se.getCount(),
					se.getSumMQ(),
					se.getDistanceMedian(),
					se.getDistanceIQD(),
					se.getDistanceIQD()/se.getDistanceMedian(),
					se.isReverse() ? "R" : "F");
		}
		
	}

}
