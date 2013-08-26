package bfx.assembly.scaffold.edges;

import java.io.IOException;

import bfx.assembly.scaffold.bam.BAMReader;
import bfx.assembly.scaffold.technology.IonTorrentTechnology;
import bfx.assembly.util.Table;

public class TabulateEdges extends EdgeConsumer {

	private Table table;
	
	public TabulateEdges(String outfile) throws IOException {
		table=new Table(outfile);
	}
	
	@Override
	public void callback(AlignEdge edge) {
		table.printRow(edge.getLeftNode(),
					   edge.getRightNode(),
					   edge.getLeftStart(),
					   edge.getRightStart(),
					   edge.getMQ(),
					   edge.isReverse() ? "R" : "F");
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		TabulateEdges tabulator = new TabulateEdges("data/mates.edges.txt");
		BAMReader reader = new BAMReader("data/mates.names.bam");
		PairsToEdges merger = new PairsToEdges(new IonTorrentTechnology(),10);
		merger.setConsumer(tabulator);
		reader.read(merger);

	}

}
