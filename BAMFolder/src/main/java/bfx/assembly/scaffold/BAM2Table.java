package bfx.assembly.scaffold;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

import net.sf.samtools.SAMRecord;
import bfx.assembly.util.Table;

public class BAM2Table implements BAMEdgeReader.EdgeConsumer {
	private Table table;
	
	BAM2Table(OutputStream out) {
		table = new Table(out);
	}
	
	public BAM2Table(String filename) throws FileNotFoundException {
		this(new FileOutputStream(filename));
	}

	@Override
	public void callback(SAMRecord aln, Map<String, Integer> seqs) {
		//int flags=aln.getFlags();
		
		boolean leftIsReverse =  aln.getReadNegativeStrandFlag();  //((flags & 0x10) == 1);
		boolean rightIsReverse = aln.getMateNegativeStrandFlag(); //((flags & 0x20) == 1);


		table.printRow(
				aln.getReadName(),
				aln.getReferenceName(),
				aln.getMateReferenceName(),
				aln.getAlignmentStart(),
				aln.getMappingQuality(),
				leftIsReverse ? "R" : "F",
				rightIsReverse ? "R" : "F");
		
	}

}
