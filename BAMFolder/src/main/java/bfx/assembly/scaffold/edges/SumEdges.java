package bfx.assembly.scaffold.edges;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import bfx.assembly.scaffold.bam.BAMReader;
import bfx.assembly.scaffold.technology.IonTorrentTechnology;
import bfx.assembly.util.Table;

public class SumEdges extends EdgeConsumer implements Iterable<SuperEdge> {
	private Map<String,Map<String,SuperEdge>> edges;
	
	public void start() {
		edges = new TreeMap<String,Map<String,SuperEdge>>();
	}
	
	@Override
	public void callback(AlignEdge edge) {
		String left=edge.getLeftNode();
		String right=edge.getRightNode();
		String dir=edge.isReverse() ? "R" : "F";
		
		if (!edges.containsKey(left))
			edges.put(left, new TreeMap<String,SuperEdge>());
		Map<String,SuperEdge> inner=edges.get(left);
		
		if (!inner.containsKey(right + dir)) {
			inner.put(right + dir, new SuperEdge(edge,seqs));
		} else {
			inner.get(right + dir).sumEdge(edge);
		}
	}

	public static class SuperEdgeIterator implements Iterator<SuperEdge>{
		private Iterator<SuperEdge> inner;
		private Iterator<Map<String,SuperEdge>> outer;
		
		public SuperEdgeIterator(Map<String, Map<String, SuperEdge>> edges) {
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
			return inner.next();
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
		PairsToEdges merger = new PairsToEdges(new IonTorrentTechnology(),10);		
		merger.setConsumer(sumedges);
		reader.read(merger);
		Table table=new Table("data/super_edges.txt");

		for(SuperEdge se:sumedges) {
			table.printRow(se.getLeft(),
					se.getRight(),
					se.getCount(),
					se.getSumMQ(),
					se.isReverse() ? "R" : "F");
		}
		
	}

}
