package bfx.assembly.scaffold.bam;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import net.sf.samtools.SAMRecord;
import bfx.assembly.util.Table;

public class BAM2Table extends AlignConsumer {
	private Table table;
	
	BAM2Table(OutputStream out) {
		table = new Table(out);
	}
	
	public BAM2Table(String filename) throws FileNotFoundException {
		this(new FileOutputStream(filename));
	}

	@Override
	public void callback(SAMRecord aln) {
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


	@Override
	public void start() {
	}

	@Override
	public void finish() {
		table.close();
	}
	
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BAMReader reader = new BAMReader("data/mates.bam");
		BAM2Table builder = new BAM2Table("data/mates.tbl.txt");
		reader.read(builder);
	}

}
